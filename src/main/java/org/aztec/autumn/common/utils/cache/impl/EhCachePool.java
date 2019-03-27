package org.aztec.autumn.common.utils.cache.impl;

import java.io.File;
import java.net.URL;
import java.util.Map;

import org.aztec.autumn.common.utils.cache.CachePool;
import org.aztec.autumn.common.utils.cache.LocalCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;

public class EhCachePool implements CachePool{
	
	private CacheManager myCacheManager;
	private static Logger LOG = LoggerFactory.getLogger(EhCachePool.class);
	private static String DEFAULT_BEAN_CACHE_NAME = "ehCachePool";
	//private static EhCachePool  singleton = new EhCachePool();
	private static Map<String,LocalCache> cachePools = Maps.newConcurrentMap();
	private LocalCache defaultCache;
	
	

	/*static {
		try {
			URL myUrl = EhCachePool.class.getResource("/ehcache.xml");
			myCacheManager = CacheManager.create(myUrl);
			defaultCache = new EhCache();
			defaultCache.init(new Object[]{myCacheManager,null});
		} catch (CacheException e) {
			LOG.error(e.getMessage(),e);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
		}
	}*/
	
	public EhCachePool(File configFile) throws Exception{
		try {
			URL myUrl = configFile.toURI().toURL();
			myCacheManager = CacheManager.create(myUrl);
			defaultCache = new EhCache();
			defaultCache.init(new Object[]{myCacheManager,null});
		} catch (CacheException e) {
			LOG.error(e.getMessage(),e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			throw e;
		}
	}
	
	
	/*public static EhCachePool getSingleton(){
		return singleton;
	}*/
	
	public LocalCache getDefaultCache() throws Exception{

		return defaultCache;
	}
	
	@Override
	public LocalCache getCache(String poolName) throws Exception {
		LocalCache pool = cachePools.get(poolName);
		if(pool == null){
			pool = new EhCache();
			cachePools.put(poolName,pool);
		}
		pool.init(new Object[]{myCacheManager,poolName});
		return pool;
	}

	@Override
	public LocalCache copy(LocalCache pool) {
		return null;
	}

	@Override
	public LocalCache createTemp(LocalCache sample) throws Exception {
		LocalCache pool = cachePools.get(sample.getName());
		if(pool != null){
			LocalCache tmpPool = new EhCache();
			tmpPool.init(new Object[]{myCacheManager,sample.getName()});
			return tmpPool;
		}
		return null;
	}



}
