package org.aztec.autumn.common.utils;

import org.aztec.autumn.common.utils.cache.CacheDataSubscriber;

public interface CacheUtils {

	public void cache(String key, Object value) throws CacheException;
	public void cacheInTTL(String key, Object value, int seconds) throws CacheException;
	public <T> T get(String key, Class<T> retType) throws CacheException;
	public void remove(String key) throws CacheException;
	public void publish(String channel,String msg) throws CacheException;
	public void subscribe(CacheDataSubscriber subscriber,String... channel) throws CacheException;
	public void lock(String key) throws CacheException;
	public boolean tryLock(String key) throws CacheException;
	public void unlock(String key) throws CacheException;
}
