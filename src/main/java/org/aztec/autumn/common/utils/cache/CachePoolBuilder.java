package org.aztec.autumn.common.utils.cache;

import org.aztec.autumn.common.utils.cache.impl.EhCachePool;

public class CachePoolBuilder {
	
	
	public static class CacheType{
		public static final String EHCACHE = "ehcache";
		public static final String FAST_CACHE = "fastCache";
		public static final String REDIS = "redis";
	}

	public static CachePool getCacheManagerByType(LocalCacheConfig config) throws LocalCacheException{
		try {
			switch(config.getCacheType()){
			case CacheType.EHCACHE:
				//return null;
				//return EhCachePool.getSingleton();
				return new EhCachePool(config.getConfigFile());
			default : 
				return null;
			}
		} catch (Exception e) {
			throw new LocalCacheException(e.getMessage(), e);
		}
	}
}
