package org.aztec.autumn.common.utils.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface QueryExecutor {
	public String getQueryResultAsString(String sql) throws SQLException;
  public String getQueryResultAsString(String sql,Object[] params) throws SQLException;
	public List<Map<String,String>> getQueryResultAsMap(String sql) throws SQLException;
  public List<Map<String,String>> getQueryResultAsMap(String sql,Object[] params) throws SQLException;
	public int executeUpdate(String sql) throws SQLException;
  public int executeUpdate(String sql,Object[] params) throws SQLException;
  public void beginTransaction() throws SQLException;
  public void commit() throws SQLException;
  public void rollback() throws SQLException;
	public void reconnect() throws SQLException;
	public void closeConnection() throws SQLException;
	public Connection getConnection() throws SQLException;
}
