package org.aztec.autumn.common;

import org.aztec.autumn.common.utils.BasePropertiesConfig;
import org.aztec.autumn.common.utils.annotation.config.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonConfig extends BasePropertiesConfig {

	@Property("LOCALHOST")
	private String localhost = "localhost";
	@Property("RPC.PORT")
	private Integer rpcPort;
	@Property("https.auth.type")
	private Integer httpsAuthType;
	@Property("https.keystore.file")
	private String keyStoreFile;
	@Property("https.keystore.password")
	private String storePassword;
	@Property("https.key.password")
	private String keyPassword;
	@Property("https.protocol")
	private String securityProtocol;
	@Property("https.trust.store.file")
	private String trustStoreFile;
	@Property("https.trust.store.password")
	private String trustStorePassword;
	@Property("REDIS.HOST")
	private String redisHost = "localhost";
	@Property("REDIS.PORT")
	private String redisPort = "6379";

	@Property("SOCKET.SERVER.SLEEP.INTERVAL")
	private static Long socketServerSleepInterval = 20l;
	private static final Logger LOG = LoggerFactory.getLogger(GlobalConfig.class);
	@Property("JMS.CLUSTER.URI")
	private String jmsCluster = "tcp://localhost:61617";
	@Property("SERVER.RPC.THREAD")
	private Integer rpcThreadPoolSize = 1000;
	@Property("REDIS.PASSWORD")
	private String password = "";
	@Property("CONFIG.PREFIX")
	private String configPrefix = "classpath";
	@Property("CONFIG.PATH")
	private String configPath = "/home/cp26/env";
	
	public static final String REDIS_CLUSTER_HOST_SPLIT_CHAR = ",";
	
	public CommonConfig() {
		super("res:/common.properties");
		init();
	}

	public String getLocalhost() {
		return localhost;
	}

	public void setLocalhost(String localhost) {
		this.localhost = localhost;
	}

	public Integer getRpcPort() {
		return rpcPort;
	}

	public void setRpcPort(Integer rpcPort) {
		this.rpcPort = rpcPort;
	}

	public Integer getHttpsAuthType() {
		return httpsAuthType;
	}

	public void setHttpsAuthType(Integer httpsAuthType) {
		this.httpsAuthType = httpsAuthType;
	}

	public String getStorePassword() {
		return storePassword;
	}

	public void setStorePassword(String storePassword) {
		this.storePassword = storePassword;
	}

	public String getKeyPassword() {
		return keyPassword;
	}

	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}

	public String getSecurityProtocol() {
		return securityProtocol;
	}

	public void setSecurityProtocol(String securityProtocol) {
		this.securityProtocol = securityProtocol;
	}

	public String getTrustStoreFile() {
		return trustStoreFile;
	}

	public void setTrustStoreFile(String trustStoreFile) {
		this.trustStoreFile = trustStoreFile;
	}

	public String getTrustStorePassword() {
		return trustStorePassword;
	}

	public void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}

	public String getKeyStoreFile() {
		return keyStoreFile;
	}

	public void setKeyStoreFile(String keyStoreFile) {
		this.keyStoreFile = keyStoreFile;
	}

	public String getRedisHost() {
		return redisHost;
	}

	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}

	public String getRedisPort() {
		return redisPort;
	}

	public void setRedisPort(String redisPort) {
		this.redisPort = redisPort;
	}
	
	
	
	
	public static Long getSocketServerSleepInterval() {
		return socketServerSleepInterval;
	}

	public static void setSocketServerSleepInterval(Long socketServerSleepInterval) {
		CommonConfig.socketServerSleepInterval = socketServerSleepInterval;
	}

	public String getJmsCluster() {
		return jmsCluster;
	}

	public void setJmsCluster(String jmsCluster) {
		this.jmsCluster = jmsCluster;
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

	public String getConfigPrefix() {
		return configPrefix;
	}

	public void setConfigPrefix(String configPrefix) {
		this.configPrefix = configPrefix;
	}

	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	
}
