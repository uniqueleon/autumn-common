package org.aztec.autumn.common.utils.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcConnector {

  private String sqlServerType;
  private String url;
  private String username;
  private String password;
  private Connection connection = null;
  private final static Logger logger = LoggerFactory
      .getLogger(JdbcConnector.class);

  private JdbcConnector(String filePath) throws ClassNotFoundException {
    loadJdbcProperties(filePath);
    initJdbcDriver(sqlServerType);
  }

  private JdbcConnector(String url, String username, String password,
      String sqlServerType) throws ClassNotFoundException {
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

  private void initJdbcDriver(String sqlServerType)
      throws ClassNotFoundException {
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
    MYSQL("mysql"), SQLSERVER2000("sqlserver2000"), SQLSERVER2005(
        "sqlserver2005"), SQLSERVER2008("sqlserver2008");

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

  public Connection getConnection() throws SQLException {
    if (connection == null)
      connection = DriverManager.getConnection(url, username, password);
    return connection;
  }

  public static JdbcConnector getConnector(String connectionPropFile)
      throws SQLException, ClassNotFoundException {
    return new JdbcConnector(connectionPropFile);
  }

  public static JdbcConnector getConnector(String url, String username,
      String password, String sqlServerType) throws SQLException,
      ClassNotFoundException {
    return new JdbcConnector(url, username, password, sqlServerType);
  }

  public static Connection getConnection(String url, String username,
      String password, String sqlServerType) throws SQLException,
      ClassNotFoundException {
    JdbcConnector connector = new JdbcConnector(url, username, password,
        sqlServerType);
    return connector.getConnection();
  }

  
}
