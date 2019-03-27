package org.aztec.autumn.common.zk;

import java.io.IOException;
import java.util.Random;

import org.apache.zookeeper.ZooKeeper;
import org.aztec.autumn.common.utils.BasePropertiesConfig;
import org.aztec.autumn.common.utils.annotation.config.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZkConnector {
	
	private static ZooKeeper zk;
	private static final Logger LOG = LoggerFactory.getLogger(ZkNode.class);
	private static LocalConnectionConfig connectConfig = new LocalConnectionConfig();

	static {
		try {
			zk = new ZooKeeper(connectConfig.getConnectString(), connectConfig.getSessionTimeout(), null);
			//zk = new ZooKeeper(connectConfig.getConnectString(), connectConfig.getSessionTimeout(), null,new Random().nextLong(),password.getBytes());
			//zk = new Zoo
			zk.addAuthInfo("digest", connectConfig.getAuthUser().getBytes());
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);
		}
	}
	
	public static ZooKeeper getKeeper() {
		return zk;
	}
	
	public static String getAuthString() {
		return connectConfig.getAuthUser();
	}
	
	private static class LocalConnectionConfig extends BasePropertiesConfig{
		
		@Property("connectString")
		public String connectString = "127.0.0.1:2181";
		@Property("sessionTimeout")
		public Integer sessionTimeout = 10000;
		@Property("authUser")
		public String authUser = "liming:1234";

		public LocalConnectionConfig() {
			super("conf/zk/connect.properties");
			init();
		}

		public String getConnectString() {
			return connectString;
		}

		public void setConnectString(String connectString) {
			this.connectString = connectString;
		}

		public Integer getSessionTimeout() {
			return sessionTimeout;
		}

		public void setSessionTimeout(Integer sessionTimeout) {
			this.sessionTimeout = sessionTimeout;
		}

		public String getAuthUser() {
			return authUser;
		}

		public void setAuthUser(String authUser) {
			this.authUser = authUser;
		}
		
		
	}
}
