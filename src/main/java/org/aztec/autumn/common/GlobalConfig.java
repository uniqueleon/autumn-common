package org.aztec.autumn.common;

import org.aztec.autumn.common.utils.BasePropertiesConfig;
import org.aztec.autumn.common.utils.annotation.config.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalConfig extends BasePropertiesConfig{

	@Property("SOCKET.SERVER.SLEEP.INTERVAL")
	private static Long socketServerSleepInterval = 20l;
	private static final Logger LOG = LoggerFactory.getLogger(GlobalConfig.class);
	@Property("JMS.CLUSTER.URI")
	private String jmsCluster = "tcp://localhost:61617";
	@Property("LOCALHOST")
	private String localhost = "localhost";
	@Property("SERVER.RPC.THREAD")
	private Integer rpcThreadPoolSize = 1000;
	@Property("REDIS.PASSWORD")
	private String password = "01cfcd4f6b8770febfb40cb906715822";
	private static GlobalConfig singleton = new GlobalConfig();
	@Property("SYSTEM.THREAD.MAX_NUM")
	private Integer maxThreadNum = 100;

	public static GlobalConfig getInstance(){
		return singleton;
	}

	private GlobalConfig() {
		super("/common.properties");
		init();
	}

	public long getSocketServerSleepInterval() {
		return socketServerSleepInterval;
	}

	public String getJmsCluster() {
		return jmsCluster;
	}

	public String getLocalhost() {
		return localhost;
	}

	public Integer getRpcThreadPoolSize() {
		return rpcThreadPoolSize;
	}

	public void setRpcThreadPoolSize(Integer rpcThreadPoolSize) {
		this.rpcThreadPoolSize = rpcThreadPoolSize;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getMaxThreadNum() {
		return maxThreadNum;
	}

	public void setMaxThreadNum(Integer maxThreadNum) {
		this.maxThreadNum = maxThreadNum;
	}

	
}
