package org.aztec.autumn.common.utils.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;
import org.aztec.autumn.common.utils.CacheException;
import org.aztec.autumn.common.utils.CacheUtils;
import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisUtil implements CacheUtils{


	private static Logger LOG = LoggerFactory.getLogger(RedisUtil.class);
	private static boolean isStarted = false;
	private List<String> hosts = new ArrayList<>();
	private List<Integer> ports = new ArrayList<>();
	private List<JedisPool> pools = Lists.newCopyOnWriteArrayList();
	//private static ThreadLocal<JedisCommands> redisClient = new ThreadLocal<>();
	//private static JedisCommands redisClient;
	private JedisCommands redisClient;
	private static final int DEFAULT_RETRY_TIME = 3;
	private String password = null;
	private static final Map<String,Object> locks = Maps.newConcurrentMap();
	private static LockChecker checker = null;
	
	

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
				JedisPool pool = new JedisPool(hosts.get(0), ports.get(0));
				pools.add(pool);
			}
			else{
				Set<HostAndPort> clusterConfig = new HashSet<>();
				for (int i = 0; i < hosts.size(); i++) {
					clusterConfig.add(new HostAndPort(hosts.get(i), ports.get(i)));
					JedisPool pool = new JedisPool(hosts.get(i), ports.get(i));
					pools.add(pool);
				}
				JedisCluster jc = new JedisCluster(clusterConfig);
				if(password != null && !password.isEmpty())
					jc.auth(password);
				//redisClient.set(jc);
				redisClient = jc;
				isStarted = true;
			}
		}
		startLockChecker();
	}
	
	private synchronized void startLockChecker() {
		if(checker == null) {
			checker = new LockChecker();
			Thread lockChkThread = new Thread(checker);
			lockChkThread.setName("Redis util lock check Thread");
			lockChkThread.setDaemon(true);
			lockChkThread.start();
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
				throw new CacheException("The redis util has not been initialized!");
			synchronized (redisClient) {
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
			throw new CacheException(e.getMessage());
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
			String value = null;
			synchronized (redisClient) {
				redisClient.get(key);
			}
			//String value = redisClient.get().get(key);
			if(value == null)
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
		synchronized (redisClient) {
			redisClient.del(key);
		}
		//redisClient.get().del(key);
	}



	@Override
	public void cacheInTTL(String key, Object value, int seconds) throws CacheException {
		cache(key, value, seconds, DEFAULT_RETRY_TIME);
		
	}

	@Override
	public void publish(String channel, String msg) throws CacheException {
		
		if(pools.size() == 0) {
			throw new CacheException("No redis pool found!");
		}
		
		int randomIndex = RandomUtils.nextInt(pools.size());
		Jedis jedis = pools.get(randomIndex).getResource();
		jedis.publish(channel, msg);
		jedis.close();
	}

	@Override
	public void subscribe(CacheDataSubscriber subscriber,String...channels) throws CacheException {
		for(JedisPool pool : pools) {
			AsyncSubscriber ayncSub = new AsyncSubscriber(pool, subscriber, channels);
			ayncSub.start();
		}
	}

	@Override
	public void lock(String key,long timeout) throws CacheException {
		String value = System.currentTimeMillis() + "_" + timeout;
		Long ret = 0l;
		synchronized (redisClient) {
			ret = redisClient.setnx(key, value);
		}
		if(ret == 0) {
			Object lockObj = locks.get(key);
			if(lockObj == null) {
				synchronized (locks) {
					if(lockObj == null) {
						lockObj = new Object();
						locks.put(key, lockObj);
					}
				}
			}
			try {
				synchronized (lockObj) {
					lockObj.wait();
				}
				lock(key,timeout);
			} catch (InterruptedException e) {
				throw new CacheException(e.getMessage(), e);
			}
		}
	}

	@Override
	public boolean tryLock(String key,long timeout) throws CacheException {
		Long ret = 0l;
		synchronized (redisClient) {
			ret = redisClient.setnx(key, "lock");
		}
		return ret == 1;
	}

	@Override
	public void unlock(String key) throws CacheException {

		synchronized (redisClient) {
			if(redisClient.exists(key)) {
				redisClient.del(key);
			}
		}
		Object lockObj = locks.get(key);
		if(lockObj != null) {
			synchronized (lockObj) {
				lockObj.notifyAll();
			}
		}
		locks.remove(key);
	}
	
	private class LockChecker implements Runnable{
		private boolean runnable = true;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while(runnable) {
					for(String lockKey : locks.keySet()) {
						String checkValue = null;
						synchronized (redisClient) {
							checkValue = redisClient.get(lockKey);
						}
						if(checkValue == null) {
							locks.get(lockKey).notifyAll();
							locks.remove(lockKey);
						}
						else {
							String[] timeArr = checkValue.split("_");
							Long lockTime = Long.parseLong(timeArr[0]);
							Long timeout = Long.parseLong(timeArr[1]);
							Long curTime = System.currentTimeMillis();
							if(curTime - lockTime > timeout) {
								unlock(lockKey);
							}
						}
					}
					Thread.currentThread().sleep(10);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(),e);
			}
		}
		
		
	}

	@Override
	public void hset(String setName, String field, String value) throws CacheException {
		// TODO Auto-generated method stub
		synchronized (redisClient) {
			redisClient.hset(setName, field , value);
		}
	}

	@Override
	public String hget(String setName, String field) throws CacheException {
		synchronized (redisClient) {
			return redisClient.hget(setName, field);
		}
	}

	@Override
	public void hdel(String setName, String field) throws CacheException {
		synchronized (redisClient) {
			redisClient.hdel(setName, field);
		}
	}

	@Override
	public Map<String, String> hgetAll(String setName) throws CacheException {
		synchronized (redisClient) {
			return redisClient.hgetAll(setName);
		}
	}

	@Override
	public List<String> hkeys(String setName) throws CacheException {
		// TODO Auto-generated method stub
		synchronized (redisClient) {
			return Lists.newArrayList(redisClient.hkeys(setName));
		}
	}

	@Override
	public List<String> hvals(String setName) throws CacheException {
		// TODO Auto-generated method stub
		synchronized (redisClient) {
			return Lists.newArrayList(redisClient.hvals(setName));
		}
	}

	@Override
	public void lset(String list, int index, String value) throws CacheException {
		synchronized (redisClient) {
			redisClient.lset(list, index, value);
		}
	}

	@Override
	public List<String> listAll(String list) throws CacheException {
		synchronized (redisClient) {
			return redisClient.lrange(list, 0, redisClient.llen(list));
		}
	}

	@Override
	public String lget(String list, int index) throws CacheException {
		synchronized (redisClient) {
			return redisClient.lindex(list, index);
		}
	}

	@Override
	public void hdel(String set, String... field) throws CacheException {
		synchronized (redisClient) {
			redisClient.hdel(set, field);
		}
	}

	@Override
	public void hdelAll(String set) throws CacheException {
		List<String> allFields = hkeys(set);
		String[] fieldArrs = allFields.toArray(new String[allFields.size()]);
		synchronized (redisClient) {
			hdel(set, fieldArrs);
		}
	}

	@Override
	public void lrem(String list, int index,String value) throws CacheException {
		synchronized (redisClient) {
			redisClient.lrem(list, index, value);
		}
	}

	@Override
	public void lremAll(String list) throws CacheException {
		List<String> listValues = listAll(list);
		synchronized (redisClient) {
			for(String listVal : listValues) {
				redisClient.lrem(list, 0, listVal);
			}
		}
	}

	@Override
	public void hset(String setName, Map<String, String> setMap) throws CacheException {
		synchronized (redisClient) {
			redisClient.hset(setName, setMap);
		}
	}


	@Override
	public boolean exists(String key) throws CacheException {
		synchronized (redisClient) {
			return redisClient.exists(key);
		}
	}

	@Override
	public void lpush(String list, String value) throws CacheException {
		synchronized (redisClient) {
			redisClient.lpush(list, value);
		}
	}

	@Override
	public boolean hexists(String setName, String field) throws CacheException {
		synchronized (redisClient) {
			return redisClient.hexists(setName, field);
		}
	}
	
}
