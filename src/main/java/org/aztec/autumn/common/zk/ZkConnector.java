package org.aztec.autumn.common.zk;

import java.util.List;
import java.util.Map;

import org.apache.zookeeper.ZooKeeper;
import org.aztec.autumn.common.GlobalConst.META_DATA_CENTER_INFO;
import org.aztec.autumn.common.utils.BasePropertiesConfig;
import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.autumn.common.utils.annotation.config.Property;
import org.aztec.autumn.common.utils.jdbc.DBManager;
import org.aztec.autumn.common.utils.jdbc.QueryExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.util.AssertException;

public class ZkConnector {
	
	private static ZooKeeper zk;
	private static final Logger LOG = LoggerFactory.getLogger(ZkNode.class);
	private static LocalConnectionConfig connectConfig = new LocalConnectionConfig();
	

	static {
		try {
			if(connectConfig.getConnectType().equals("db")) {
				connectConfig = loadInfoFromDB();
			}
			zk = new ZooKeeper(connectConfig.getConnectString(), connectConfig.getSessionTimeout(), null);
			//zk = new ZooKeeper(connectConfig.getConnectString(), connectConfig.getSessionTimeout(), null,new Random().nextLong(),password.getBytes());
			//zk = new Zoo
			zk.addAuthInfo("digest", connectConfig.getAuthUser().getBytes());
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
		}
	}
	
	public static ZooKeeper getKeeper() {
		return zk;
	}
	
	public static String getAuthString() {
		return connectConfig.getAuthUser();
	}
	
	public static LocalConnectionConfig loadInfoFromDB() throws Exception {
		String dbUrl = connectConfig.getConnectString();
		String[] authUser = connectConfig.getAuthUser().split(":");
		DBManager dbManager =  DBManager.getManager(dbUrl, authUser[0], authUser[1], "mysql");
		if(dbManager != null) {
			QueryExecutor executor = dbManager.getQueryExecutor();
			List<Map<String,String>> retList = executor.getQueryResultAsMap(
					META_DATA_CENTER_INFO.ZOOKEEPER_CONNECT_QUERY_SQL);
			if(retList == null || retList.size() == 0) {
				throw new AssertException("No zookeeper config in db!");
			}
			Map<String, String> targetRow = retList.get(0);
			String jsonContent = targetRow.get(
					META_DATA_CENTER_INFO.TABLE_COLUMNS[META_DATA_CENTER_INFO.CONTENT_COLUMN_INDEX]);
			JsonUtils jsonUtils = UtilsFactory.getInstance().getJsonUtils();
			LocalConnectionConfig config = jsonUtils.json2Object(jsonContent, LocalConnectionConfig.class);
			return config;
		}
		return null;
	}
	
	
	private static class LocalConnectionConfig extends BasePropertiesConfig{
		
		
		@Property("connectType")
		private String connectType = "db"; // local | db
		@Property("connectString")
		//private String connectString = "127.0.0.1:2181";
		private String connectString = "jdbc:mysql://127.0.0.1:3306/aztec_db";
		@Property("sessionTimeout")
		private Integer sessionTimeout = 10000;
		@Property("authUser")
		private String authUser = "liming:1234";

		public LocalConnectionConfig() {
			super("conf/zk/connect.properties");
			init();
		}

		public String getConnectType() {
			return connectType;
		}

		public String getConnectString() {
			return connectString;
		}


		public Integer getSessionTimeout() {
			return sessionTimeout;
		}

		public String getAuthUser() {
			return authUser;
		}
		
	}
	
	public static void main(String[] args) {
		try {
			LocalConnectionConfig lcc = new LocalConnectionConfig();
			
			JsonUtils jsonUtil = UtilsFactory.getInstance().getJsonUtils();
			System.out.println(jsonUtil.object2Json(lcc));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}