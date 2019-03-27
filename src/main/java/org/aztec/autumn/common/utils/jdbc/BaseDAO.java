package org.aztec.autumn.common.utils.jdbc;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BaseDAO<T> {

  private final static Logger logger = LoggerFactory.getLogger(BaseDAO.class);
  protected DBManager manager;
  protected QueryExecutor queryExec;
  protected Class<T> entityClass;
  protected String tableName;

  public BaseDAO(String tableName, String configFile, Class<T> entityCls)
      throws ClassNotFoundException, SQLException {
    manager = DBManager.getManager(configFile);
    queryExec = manager.getQueryExecutor();
    this.entityClass = entityCls;
  }

  public BaseDAO(String tableName, String connectionUrl, String username,
      String password, String sqlServerType, Class<T> entityCls)
      throws ClassNotFoundException, SQLException {
    manager = DBManager.getManager(connectionUrl, username, password,
        sqlServerType);
    queryExec = manager.getQueryExecutor();
    this.entityClass = entityCls;
  }

  protected List<T> doQuery(String sql, Object[] params,
      DAOQueryExecutor executor, boolean reconnect) throws SQLException,
      InstantiationException, IllegalAccessException {
    if (reconnect) {
      logger.warn("connection may be lost,reconnect to the db server");
      manager.reconnect(0);
    }
    try {

      if (manager.getCache().isHit(sql, params)) {
        logger.debug("Cache is hit, just get data from cache");
        return manager.getCache().getFromCache(sql, params);
      }
      List<T> queryResult = new ArrayList<T>();
      List<Map<String, String>> resultList;
      if (executor == null) {

        if (params != null)
          resultList = queryExec.getQueryResultAsMap(sql, params);
        else
          resultList = queryExec.getQueryResultAsMap(sql);
        if (Map.class.isAssignableFrom(entityClass)) {
          return (List<T>) resultList;
        } else
          return cast(resultList);
      } else {
        return (List<T>) executor.query();
      }
      // AuthenticationDataCache.pushToCache(sql,params, queryResult);
    } catch (SQLException e) {
      // if already retry 1 time, just return null;
      logger.error(e.getMessage(), e);
      // if (reconnect) {
      // logger.warn("Already reconnect to authentication db,the problem still exists!Please check db server status and configurations!");
      // return null;
      // }
      // return doQuery(sql,params, executor, true);
      return null;
    }
  }

  private List<T> cast(List<Map<String, String>> qryResult)
      throws InstantiationException, IllegalAccessException {
    List<T> retList = new ArrayList<T>();
    if (qryResult != null && qryResult.size() > 0) {
      for (Map<String, String> record : qryResult) {
        T newObject = entityClass.newInstance();
        for (String col : record.keySet()) {
          try {
            Field field = entityClass.getDeclaredField(getPropertyName(col));
            field.setAccessible(true);
            field.set(newObject, castValue(field, record.get(col)));
          } catch (Exception e) {
            logger.debug(e.getMessage(), e);
          }

        }
        retList.add(newObject);
      }
    }
    return retList;
  }

  private Object castValue(Field field, String text) throws ParseException {
    Class fieldType = field.getType();
    if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
      return Integer.parseInt(text);
    } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
      return Float.parseFloat(text);
    } else if (fieldType.equals(Long.class) || fieldType.equals(Long.class)) {
      return Long.parseLong(text);
    } else if (fieldType.equals(Double.class) || fieldType.equals(Double.class)) {
      return Double.parseDouble(text);
    } else if (fieldType.equals(String.class)) {
      return new String(text);
    } else if (fieldType.equals(Date.class)) {
      return DateUtils.parseDate(text, new String[] { "yyyy-MM-dd hh:mm:ss",
          "yyyy/MM/dd hh:mm:ss" });
    }
    return null;
  }

  private String getPropertyName(String colName) {
    String[] words = colName.split("_");
    String propName = words[0];
    for (int i = 1; i < words.length; i++) {
      propName += StringUtils.capitalize(words[i]);
    }
    return propName;
  }

  private static String getColumnName(String fieldName) {
    String tmpWord = "";
    for (int i = 0; i < fieldName.length(); i++) {
      char tmpChar = fieldName.charAt(i);
      if (Character.isUpperCase(tmpChar)) {
        tmpWord += "_" + (new String("" + tmpChar).toLowerCase());
      } else {
        tmpWord += fieldName.substring(i, i + 1);
      }
    }
    return tmpWord;
  }

  protected boolean batchSave(List<T> entities) throws SQLException,
      InstantiationException, IllegalAccessException, IllegalArgumentException,
      NoSuchFieldException, SecurityException {
    if(!doInsert(filt(entities,FiltType.INSERT)))
      return false;
    return doUpdate(filt(entities,FiltType.UPDATE));
  }
  
  private static enum FiltType{
    INSERT,UPDATE
  }

  private List<T> filt(List<T> entities,FiltType type) throws SQLException,
      InstantiationException, IllegalAccessException, IllegalArgumentException,
      NoSuchFieldException, SecurityException {
    String sql = "select * from " + tableName + " where id in (";
    List<T> newList = new ArrayList<>();
    newList.addAll(entities);
    List<Object> ids = new ArrayList<>();
    for (T entity : entities) {
      Object id = entityClass.getDeclaredField("id").get(entity);
      if (id != null) {
        sql += "?,";
        ids.add(id);
      }
    }
    sql = sql.substring(0, sql.length() - 1);
    sql += ")";
    List<T> recordInDb = doQuery(sql, ids.toArray(new Object[ids.size()]),
        null, true);
    for (int i = 0; i < newList.size(); i++) {
      T entity = newList.get(i);
      Object id = entityClass.getDeclaredField("id").get(entity);
      boolean notFound = false;
      for (int j = 0;j < recordInDb.size();j++) {
        T record = recordInDb.get(i);
        Object recordId = entityClass.getDeclaredField("id").get(record);
        switch(type){
          case INSERT:
            if (id != null && id.equals(recordId))
              newList.remove(i);
            break;
          case UPDATE:
            if (id.equals(recordId))
              continue;
            if (id == null || (j == (recordInDb.size() - 1) && !id.equals(recordId)))
              newList.remove(i);
            break;
        }
      }
    }
    return newList;
  }
  
  private boolean doUpdate(List<T> entities) throws SQLException {

    try {
      queryExec.beginTransaction();
      StringBuilder sql = new StringBuilder("update " + tableName
          + " set ");
      Field[] fields = entityClass.getDeclaredFields();
      List<Object> params = new ArrayList<>();
      for (Field field : fields) {
        if(isIgnoreInSave(field))continue;
        sql.append(getColumnName(field.getName()) + "=(case id ");
        for (int i = 0; i < entities.size(); i++) {
          Object entity = entities.get(i);
          sql.append(" when ? then ? ");
          Object id = entityClass.getDeclaredField("id").get(entity);
          params.add(id);
          params.add(field.get(entity));
        }
        sql.append(" end),");
      }
      sql.delete(sql.length() - 1, sql.length());
      queryExec.executeUpdate(sql.toString(),
          params.toArray(new Object[params.size()]));
      queryExec.commit();
      return true;
    } catch (Exception e) {
      queryExec.rollback();
      return false;
    }
  }

  private boolean doInsert(List<T> entities) throws SQLException {
    try {
      queryExec.beginTransaction();
      StringBuilder sql = new StringBuilder("insert into " + tableName
          + " values(");
      Field[] fields = entityClass.getDeclaredFields();
      for (Field field : fields) {
        if(isIgnoreInSave(field))continue;
        sql.append(getColumnName(field.getName()) + ",");
      }
      sql.delete(sql.length() - 1, sql.length());
      sql.append(")");
      List<Object> params = new ArrayList<>();
      for (int i = 0; i < entities.size(); i++) {
        Object entity = entities.get(i);
        sql.append("(");
        for (Field field : fields) {
          if(isIgnoreInSave(field))continue;
          sql.append("(?,");
          params.add(field.get(entity));
        }
        sql.delete(sql.length() - 1, sql.length());
        sql.append("),");
      }
      sql.delete(sql.length() - 1, sql.length());
      queryExec.executeUpdate(sql.toString(),
          params.toArray(new Object[params.size()]));
      queryExec.commit();
      return true;
    } catch (Exception e) {
      queryExec.rollback();
      return false;
    }
  }
  
  public boolean isIgnoreInSave(Field field){
    return field.getName().equals("id") || field.getType().equals(Array.class) || Collection.class.isAssignableFrom(field.getType());
  }

  public static void main(String[] args) {
    System.out.println(getColumnName("testNameOrEmail"));
  }
}
