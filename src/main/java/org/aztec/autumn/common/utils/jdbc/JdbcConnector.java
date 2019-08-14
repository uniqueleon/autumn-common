package org.aztec.autumn.common.utils.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

public class JdbcConnector {

	private String sqlServerType;
	private String url;
	private String username;
	private String password;
	private Connection connection = null;
	private static Map<String, Queue<Connection>> pools = Maps.newConcurrentMap();
	private final static Logger logger = LoggerFactory.getLogger(JdbcConnector.class);
	private final static int POOL_SIZE = 30;
	private final static int DEFAULT_TRY_TIME = 3;
	private final static long SLEEP_INTERVAL = 100l;

	private JdbcConnector(String filePath) throws ClassNotFoundException {
		loadJdbcProperties(filePath);
		initJdbcDriver(sqlServerType);
	}

	private JdbcConnector(String url, String username, String password, String sqlServerType)
			throws ClassNotFoundException {
		this.url = url;
		this.username = username;
		this.password = password;
		initJdbcDriver(sqlServerType);
	}

	private void loadJdbcProperties(String filePath) {
		try {
			Properties props = new Properties();
			props.load(new FileInputStream(new File(filePath)));
			url = props.getProperty("jdbc.url");
			username = props.getProperty("jdbc.username");
			password = props.getProperty("jdbc.password");
			sqlServerType = props.getProperty("jdbc.sql_server_type");
			initJdbcDriver(sqlServerType);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void initJdbcDriver(String sqlServerType) throws ClassNotFoundException {
		SqlServerType serverType = SqlServerType.getSqlServerType(sqlServerType);
		switch (serverType) {
		case MYSQL:
			Class.forName("com.mysql.jdbc.Driver");
			break;
		case SQLSERVER2000:
			Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
			break;
		case SQLSERVER2005:
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			break;
		case SQLSERVER2008:
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			break;
		default:
			break;
		}
	}

	public enum SqlServerType {
		MYSQL("mysql"), SQLSERVER2000("sqlserver2000"), SQLSERVER2005("sqlserver2005"), SQLSERVER2008("sqlserver2008");

		private String type;

		private SqlServerType(String type) {
			this.type = type;
		}

		public static SqlServerType getSqlServerType(String type) {
			for (SqlServerType serverType : SqlServerType.values()) {
				if (serverType.type.equals(type))
					return serverType;
			}
			return null;
		}
	}

	private String getConnectionKey() {
		return url + "_" + username + "_" + password;
	}
	
	public Queue<Connection> getConnectionPool() throws SQLException {
		String connKey = getConnectionKey();
		if(!pools.containsKey(connKey)) {
			synchronized (pools) {
				if(!pools.containsKey(connKey)) {
					Queue<Connection>  connectionQueue = Queues.newArrayBlockingQueue(POOL_SIZE);
					for(int i = 0;i < POOL_SIZE;i++) {
						Connection conn = DriverManager.getConnection(url, username, password);
						connectionQueue.add(conn);
					}
					pools.put(connKey, connectionQueue);
					return connectionQueue;
				}
			}
		}
		return pools.get(connKey);
	}
	
	private Queue<Connection> checkQueue(Queue<Connection> connectionQueue) throws SQLException {

		Queue<Connection> tmpQueue = Queues.newArrayBlockingQueue(POOL_SIZE);
		if(!connectionQueue.isEmpty()) {
			synchronized (connectionQueue) {
				while(!connectionQueue.isEmpty()) {
					Connection conn = connectionQueue.poll();
					if(conn.isClosed()) {
						tmpQueue.add(DriverManager.getConnection(url, username, password));
					}
					else {
						tmpQueue.add(conn);
					}
				}
				while(!tmpQueue.isEmpty()) {
					connectionQueue.add(tmpQueue.poll());
				}
			}
		}
		return connectionQueue;
	}
	
	public void releaseConnection(Connection connection) throws SQLException {
		Queue<Connection> pool = getConnectionPool();
		pool.add(connection);
	}

	public Connection getConnection() throws SQLException {
		
		Queue<Connection> connectionQueue = getConnectionPool();
		checkQueue(connectionQueue);
		connection = connectionQueue.poll();
		int tryTime = DEFAULT_TRY_TIME;
		while (connection == null) {
			try {
				tryTime--;
				if(tryTime < 0) {
					throw new SQLException("pool is empty!");
				}
				Thread.currentThread().sleep(SLEEP_INTERVAL);
				connectionQueue = checkQueue(connectionQueue);
				connection = connectionQueue.poll();
			} catch (InterruptedException e) {
				throw new SQLException(e.getMessage(),e);
			}
//        connection = DriverManager.getConnection(url, username, password);
//        connectionQueue.add(connection);
		}
		return connection;
	}

	public static JdbcConnector getConnector(String connectionPropFile) throws SQLException, ClassNotFoundException {
		return new JdbcConnector(connectionPropFile);
	}

	public static JdbcConnector getConnector(String url, String username, String password, String sqlServerType)
			throws SQLException, ClassNotFoundException {
		return new JdbcConnector(url, username, password, sqlServerType);
	}

	public static Connection getConnection(String url, String username, String password, String sqlServerType)
			throws SQLException, ClassNotFoundException {
		JdbcConnector connector = new JdbcConnector(url, username, password, sqlServerType);
		return connector.getConnection();
	}

}
