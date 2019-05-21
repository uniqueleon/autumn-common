package org.aztec.autumn.common.utils.jdbc;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBManager {

	private QueryExecutor queryExec = null;
	private static Logger logger = LoggerFactory
			.getLogger(DBManager.class);
	private DataCache cache;
	private JdbcConnector connector;
	private ConnectionListener conectionListener = new ConnectionListener();
	private final static int RECONNECT_TRY_TIME = 3;

	public static DBManager getManager(String configFilePath) throws ClassNotFoundException, SQLException{
	  return new DBManager(configFilePath);
	}

  public static DBManager getManager(String url, String user, String password,
      String sqlType) throws ClassNotFoundException, SQLException {
    return new DBManager(url, user, password, sqlType);
  }
	
	private DBManager(String configFilePath) throws ClassNotFoundException, SQLException{
	  connector = JdbcConnector.getConnector(configFilePath);
	  cache = new DataCache();
	  connect();
	}
	

  private DBManager(String url,String user,String password,String sqlType) throws ClassNotFoundException, SQLException{
    connector = JdbcConnector.getConnector(url, user, password, sqlType);
    cache = new DataCache();
    connect();
  }

	private void connect() throws SQLException, ClassNotFoundException {
	  
	  queryExec = new QueryExecutorImpl(
				connector.getConnection());
    conectionListener.start();
	}
	
	public void disconnect() throws SQLException, ClassNotFoundException{
		queryExec.closeConnection();
		conectionListener.stopListener();
	}
	
	public QueryExecutor reconnect(int tryTime) throws SQLException{
		if(tryTime >= RECONNECT_TRY_TIME)
			throw new SQLException("Reconnect try time has reach " + RECONNECT_TRY_TIME + " times!"
					+ "Please confirm your database is OK and the configuration is right!");
		queryExec.closeConnection();
		try {
			queryExec = new QueryExecutorImpl(connector.getConnection());
		} catch (SQLException e) {
			reconnect(tryTime+1);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return queryExec;
	}
	
	public QueryExecutor getQueryExecutor(){
		return queryExec;
	}
	
	public void clearCache(){
		cache.clearCache();
	}
	
	public DataCache getCache(){
	  return cache;
	}
	

	/*
	 * private static final
	 * 
	 * public AuthenticationDBManager() { }
	 */

	class ConnectionListener extends Thread {

		private final static long SLEEP_INTERVAL = 1800 * 1000;
		private boolean running = false;

		@Override
		public void run() {
			running = true;
			while (running) {
				try {
					if (queryExec == null)
						connect();
					logger.info("Listener is tring to activate the connection!");
					queryExec.getQueryResultAsString("select 1");
					Thread.currentThread().sleep(SLEEP_INTERVAL);
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
					try {
						reconnect(0);
					} catch (SQLException e1) {
						logger.error(e.getMessage(), e);
						try {
							Thread.currentThread().sleep(1000);
						} catch (InterruptedException e2) {
							break;
						}
					}
				} catch (InterruptedException e2) {
					break;
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}

		}

		public void stopListener() {
			this.running = false;
			this.interrupt();
		}

		public boolean isRunning() {
			return running;
		}
	}

}
