package org.aztec.autumn.common.utils.jdbc;


public class DaoFactory implements IDaoFactory{

  public DaoFactory() {
    // TODO Auto-generated constructor stub
  }
  
  public <T> BaseDAO<T> getBaseDao(String configFile,Class<T> entityCls) throws JdbcException{
    try {
      return new BaseDAO<T>(entityCls.getName(),configFile, entityCls);
    } catch (Exception e) {
      throw new JdbcException(e.getMessage(),e);
    }
  }

  @Override
  public <T> BaseDAO<T> getBaseDao(String url, String username, String pwd,String sqlServerType,
      Class<T> entityCls) throws JdbcException {
    try {
      return new BaseDAO<T>(entityCls.getName(),url, username,pwd, sqlServerType,
          entityCls);
    } catch (Exception e) {
      throw new JdbcException(e.getMessage(),e);
    }
  }

}
