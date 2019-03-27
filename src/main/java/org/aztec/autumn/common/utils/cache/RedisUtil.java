package org.aztec.autumn.common.utils.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aztec.autumn.common.utils.CacheException;
import org.aztec.autumn.common.utils.CacheUtils;
import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisUtil implements CacheUtils{


	private static Logger LOG = LoggerFactory.getLogger(RedisUtil.class);
	private static boolean isStarted = false;
	private List<String> hosts = new ArrayList<>();
	private List<Integer> ports = new ArrayList<>();
	//private static ThreadLocal<JedisCommands> redisClient = new ThreadLocal<>();
	//private static JedisCommands redisClient;
	private JedisCommands redisClient;
	private static final int DEFAULT_RETRY_TIME = 3;
	private String password = null;
	
	//private CommonConfig config = new CommonConfig();
	/*private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock rLock = rwl.readLock();
	private final Lock wLock = rwl.writeLock();*/

	public RedisUtil(String host,Integer port){
		this.hosts.add(host);
		this.ports.add(port);
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public RedisUtil(String[] hosts, Integer[] ports){
		if(hosts.length != ports.length)
			throw new IllegalArgumentException("Hosts' number not match with the ports'!");
		for(String host : hosts)
			if(host == null || host.isEmpty())
				throw new IllegalArgumentException("some host information is missing!");
		for(Integer port : ports){
			if(port == null)
				throw new IllegalArgumentException("some port information is missing!");
			if(port < 1)
				throw new IllegalArgumentException("port number must be larger than 0");
		}
		for(String host : hosts){
			this.hosts.add(host);
		}
		for(Integer port : ports){
			this.ports.add(port);
		}
	}
	
	public void connect(){
		if(redisClient == null){
			if(hosts.size() == 1){
				Jedis jc = new Jedis(hosts.get(0), ports.get(0));
				if(password != null && !password.isEmpty())
					jc.auth(password);
				redisClient = jc;
				//redisClient.set(jc);
				isStarted = true;
			}
			else{
				Set<HostAndPort> clusterConfig = new HashSet<>();
				for (int i = 0; i < hosts.size(); i++) {
					clusterConfig.add(new HostAndPort(hosts.get(i), ports.get(i)));
				}
				JedisCluster jc = new JedisCluster(clusterConfig);
				if(password != null && !password.isEmpty())
					jc.auth(password);
				//redisClient.set(jc);
				redisClient = jc;
				isStarted = true;
			}
		}
	}
	
	private boolean reconnect(){
		int tryTime = 3;
		while(tryTime > 0){
			try {
				connect();
				return true;
			} catch (Exception e) {
				tryTime --;
			}
		}
		return false;
	}
	
	public void cache(String key, Object value) throws CacheException {
		cache(key, value, null, DEFAULT_RETRY_TIME);
	}
	

	public void cache(String key, Object value,Integer ttl,int retryTime) throws CacheException {
		
		try {
			//wLock.lock();
			if (isStarted == false)
				return;
			if (value instanceof String) {
				if(ttl == null)
					redisClient.set(key, (String) value);
				else
					redisClient.setex(key, ttl.intValue(), (String) value);
				//redisClient.get().set(key, (String) value);
			} else {
				if(ttl == null)
					redisClient.set(key, getJsonForm(value));
				else
					redisClient.setex(key, ttl.intValue(), getJsonForm(value));
				//redisClient.set
				//redisClient.get().set(key, getJsonForm(value));
			}
		} catch (JedisConnectionException jce){
			if(reconnect()){
				if(retryTime > 0)
					cache(key, value, ttl, (retryTime - 1));
			} else{
				throw new CacheException("Jedis Server disconnected!");
			}
		}
		catch (Exception e) {
			LOG.error(e.getMessage(),e);
		}
		finally{
			//wLock.unlock();
		}
	}
	
	public <T> T get(String key, Class<T> retType) throws CacheException{
		return get(key, retType, DEFAULT_RETRY_TIME);
	}

	public <T> T get(String key, Class<T> retType,int retryTime) throws CacheException {
		try {
			//rLock.lock();
			if (isStarted == false)
				return null;
			String value = redisClient.get(key);
			//String value = redisClient.get().get(key);
			if(value == null || value.isEmpty())
				return null;
			if (retType.equals(String.class)) {
				return (T) value;
			} else {
				JsonUtils jsonUtil = UtilsFactory.getInstance().getJsonUtils();
				return jsonUtil.json2Object(value, retType);
			}
		}  catch (JedisConnectionException jce){
			if(reconnect()){
				if(retryTime > 0)
					return get(key, retType , (retryTime - 1));
			}
			return null;
		}
		catch (Exception e) {
			//throw new CacheException("key=" + key + ",cause Msg:" + e.getMessage(),e);
			LOG.error(e.getMessage(),e);
			return null;
		}
		finally{
			//rLock.unlock();
		}
	}

	public String getJsonForm(Object value) throws Exception {
		JsonUtils jsonUtil = UtilsFactory.getInstance().getJsonUtils();
		return jsonUtil.object2Json(value);
	}

	@Override
	public void remove(String key) throws CacheException {
		//String oldValue = redisClient.get().get(key);
		redisClient.del(key);
		//redisClient.get().del(key);
	}



	@Override
	public void cacheInTTL(String key, Object value, int seconds) throws CacheException {
		cache(key, value, seconds, DEFAULT_RETRY_TIME);
		
	}

}
