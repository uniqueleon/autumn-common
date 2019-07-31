package org.aztec.autumn.common.utils.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;


public class QueryExecutorImpl implements QueryExecutor{

	private Connection connection;
	private JdbcConnector connector;
	
	public QueryExecutorImpl(Connection connection){
		this.connection = connection;
	}
	
	public QueryExecutorImpl(JdbcConnector Connector){
    this.connection = connection;
  }
	
	public String getQueryResultAsString(String sql) throws SQLException{
		Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
		ResultSet resultSet = statement.executeQuery(sql);
		return DataWrapper.wrapAsJsonString(resultSet);
	}
	
	public List<Map<String,String>> getQueryResultAsMap(String sql) throws SQLException{
		Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
		ResultSet resultSet = statement.executeQuery(sql);
		return DataWrapper.wrapResultAsMap(resultSet);
	}
	
	public void reconnect() throws SQLException{
		if(connector != null)
		  connection = connector.getConnection();
	}

	@Override
	public void closeConnection() throws SQLException {
		if(connection != null && !connection.isClosed())
			connection.close();
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		Statement statement = connection.createStatement();
		return statement.executeUpdate(sql);
	}

  @Override
  public List<Map<String, String>> getQueryResultAsMap(String sql,
      Object[] params) throws SQLException {
    PreparedStatement statement = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
    statement = prepare(statement, params);
    ResultSet resultSet = statement.executeQuery();
    return DataWrapper.wrapResultAsMap(resultSet);
  }
  
  private PreparedStatement prepare(PreparedStatement statement,Object[] params) throws SQLException{
    for(int i = 1;i <= params.length;i++){
      Object param = params[i - 1];
      if(param == null)continue;
      if(param instanceof Integer){
        statement.setInt(i, (Integer) param);
      }
      else if(param instanceof Long){
        statement.setLong(i, (Long) param);
      }
      else if(param instanceof Double){
        statement.setDouble(i, (Double) param);
      }
      else if(param instanceof Float){
        statement.setFloat(i, (Float) param);
      }
      else if(param instanceof String){
        statement.setString(i, (String) param);
      }
    }
    return statement;
  }

  @Override
  public String getQueryResultAsString(String sql, Object[] params)
      throws SQLException {

    PreparedStatement statement = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
    statement = prepare(statement, params);
    ResultSet resultSet = statement.executeQuery(sql);
    return DataWrapper.wrapAsJsonString(resultSet);
  }

  @Override
  public int executeUpdate(String sql, Object[] params) throws SQLException {
    PreparedStatement statement = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
    statement = prepare(statement, params);
    return statement.executeUpdate();
  }

  @Override
  public void beginTransaction() throws SQLException {
    connection.setAutoCommit(false);
  }

  @Override
  public void commit() throws SQLException {
    connection.commit();
    connection.setAutoCommit(true);
  }

  @Override
  public void rollback() throws SQLException {
    connection.rollback();
    connection.setAutoCommit(true);
  }
	
}
