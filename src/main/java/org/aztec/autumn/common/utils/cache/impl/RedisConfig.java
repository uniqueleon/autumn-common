package org.aztec.autumn.common.utils.cache.impl;

import org.aztec.autumn.common.utils.BasePropertiesConfig;
import org.aztec.autumn.common.utils.annotation.config.Configuration;
import org.aztec.autumn.common.utils.annotation.config.Property;

@Configuration
public class RedisConfig extends BasePropertiesConfig {
	
	@Property(value = "redis.server")
	private String redisServers;
	@Property(value = "redis.port")
	private String serverPorts;
	@Property(value = "redis.password")
	private String password;
	
	private static final RedisConfig singleton = new RedisConfig();

	private RedisConfig() {
		super("redis-conf.properties");
	}
	
	public static RedisConfig getInstance() {
		return singleton;
	}
	
	public void reload() {
		super.reload();
	}

	public String[] getRedisServersAsArray() {
		return redisServers.split(",");
	}

	public void setRedisServers(String redisServers) {
		this.redisServers = redisServers;
	}

	public Integer[] getServerPortsAsArray() {
		String[] portArray = serverPorts.split(",");
		Integer[] ports = new Integer[portArray.length];
		for(int i = 0;i< portArray.length;i++) {
			ports[i] = Integer.parseInt(portArray[i]);
		}
		return ports;
	}

	public void setServerPorts(String serverPorts) {
		this.serverPorts = serverPorts;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
