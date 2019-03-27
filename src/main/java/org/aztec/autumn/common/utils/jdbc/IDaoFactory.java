package org.aztec.autumn.common.utils.jdbc;


public interface IDaoFactory {

  public <T> BaseDAO<T> getBaseDao(String configFile,Class<T> entityCls) throws JdbcException;
  public <T> BaseDAO<T> getBaseDao(String url,String username,String pwd,String sqlServerType,Class<T> entityCls) throws JdbcException;
}
