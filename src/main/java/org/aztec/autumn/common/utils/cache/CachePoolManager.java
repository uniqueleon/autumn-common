package org.aztec.autumn.common.utils.cache;

import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.aztec.autumn.common.utils.CacheUtils;

import com.google.common.collect.Maps;

public class CachePoolManager {

	private static final Map<String,GenericObjectPool<RedisUtil>> cacheUtilsPool = Maps.newConcurrentMap();


	public CachePoolManager() {
		// TODO Auto-generated constructor stub
	}

	public static String getCacheUtilKey(String[] hosts,Integer[] port) {
		StringBuilder builder = new StringBuilder();
		for(String host : hosts) {
			builder.append(host + ":" + port + "_");
		}
		return builder.toString();
	}

	
	public static CacheUtils getCacheUtils(String[] hosts,Integer[] port,String password) {
		String cacheKey = RedisUtilFactory.getPoolKey(hosts, port);
		if(!cacheUtilsPool.containsKey(cacheKey)) {
			synchronized (cacheUtilsPool) {
				if(!cacheUtilsPool.containsKey(cacheKey)) {
					
					cacheUtilsPool.put(cacheKey, new GenericObjectPool<>(new RedisUtilFactory(hosts, port, password)));
				}
			}
		}
		try {
			return cacheUtilsPool.get(cacheKey).borrowObject();
		} catch (Exception e) {
			return null;
		}
	}
	

	public  static CacheUtils getCacheUtils(String cacheServer,Integer port){
		return getCacheUtils(new String[] {cacheServer}, new Integer[] {port}, null);
	}
	
	public  static CacheUtils getCacheUtils(String cacheServer,Integer port,String password){
		return getCacheUtils(new String[] {cacheServer}, new Integer[] {port}, password);
	}
	

	public static void releaseCacheUtils(CacheUtils util) {
		if(cacheUtilsPool.containsKey(util.getPoolKey())) {
			GenericObjectPool<RedisUtil> pool = cacheUtilsPool.get(util.getPoolKey());
			pool.returnObject((RedisUtil)util);
		}
	}
	
	public  static CacheUtils getCacheUtils(String[] cacheServers,Integer[] ports){
		return getCacheUtils(cacheServers, ports, null);
	}
	

	public  static CacheUtils getCacheUtils(String cacheServer,String ports){
		if(!cacheServer.contains(",")){
			return getCacheUtils(cacheServer, Integer.parseInt(ports));
		}
		else{
			String[] cacheServers = cacheServer.split(",");
			String[] portStrs = ports.split(",");
			Integer[] portArr = new Integer[portStrs.length];
			for(int i = 0;i < portStrs.length;i++){
				portArr[i] = Integer.parseInt(portStrs[i]);
			}
			return getCacheUtils(cacheServers, portArr);
		}
	}

}
