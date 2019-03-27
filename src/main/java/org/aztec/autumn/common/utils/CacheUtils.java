package org.aztec.autumn.common.utils;

public interface CacheUtils {

	public void cache(String key, Object value) throws CacheException;
	public void cacheInTTL(String key, Object value, int seconds) throws CacheException;
	public <T> T get(String key, Class<T> retType) throws CacheException;
	public void remove(String key) throws CacheException;
}
