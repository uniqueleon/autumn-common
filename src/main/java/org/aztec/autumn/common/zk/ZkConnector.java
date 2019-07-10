package org.aztec.autumn.common.zk;

import java.util.List;
import java.util.Map;

import org.apache.zookeeper.ZooKeeper;
import org.aztec.autumn.common.GlobalConst.META_DATA_CENTER_INFO;
import org.aztec.autumn.common.utils.BasePropertiesConfig;
import org.aztec.autumn.common.utils.DatabasePropertiesConfig;
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
	private static LocalConnectionConfig connectConfig;
	

	static {
		try {
			connectConfig = new LocalConnectionConfig();
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
	
	
	
	
	private static class LocalConnectionConfig extends DatabasePropertiesConfig{
		
		@Property("connectString")
		private String connectString = "jdbc:mysql://127.0.0.1:3306/aztec_db";
		@Property("sessionTimeout")
		private Integer sessionTimeout = 10000;
		@Property("authUser")
		private String authUser = "liming:1234";

		public LocalConnectionConfig() throws Exception {
			super(META_DATA_CENTER_INFO.ZOOKEEPER_CONNECT_QUERY_SQL,
					META_DATA_CENTER_INFO.TABLE_COLUMNS[META_DATA_CENTER_INFO.CONTENT_COLUMN_INDEX]);
			init();
			loadInfoFromDB();
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
