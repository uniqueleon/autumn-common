package org.aztec.autumn.common.utils;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.utils.cache.CacheDataSubscriber;

public interface CacheUtils {

	public boolean exists(String key) throws CacheException;
	public void cache(String key, Object value) throws CacheException;
	public void cacheInTTL(String key, Object value, int seconds) throws CacheException;
	public void hset(String setName,Map<String,String> setMap) throws CacheException;
	public void hset(String setName,String field,String vaule) throws CacheException;
	public String hget(String setName,String field) throws CacheException;
	public void hdel(String setName,String field) throws CacheException;
	public Map<String,String> hgetAll(String setName) throws CacheException;
	public List<String> hkeys(String setName) throws CacheException;
	public List<String> hvals(String setName)  throws CacheException;
	public boolean hexists(String setName,String field) throws CacheException;
	public void hdel(String set,String... field) throws CacheException;
	public void hdelAll(String set) throws CacheException;
	public void lset(String list,int index,String value) throws CacheException;
	public void lpush(String list,String value)  throws CacheException;
	public List<String> listAll(String list) throws CacheException;
	public void lrem(String list,int index,String value) throws CacheException;
	public void lremAll(String list) throws CacheException;
	public String lget(String list,int index) throws CacheException;
	public <T> T get(String key, Class<T> retType) throws CacheException;
	public void remove(String key) throws CacheException;
	public void publish(String channel,String msg) throws CacheException;
	public void subscribe(CacheDataSubscriber subscriber,String... channel) throws CacheException;
	public void lock(String key,long timeout) throws CacheException;
	public boolean tryLock(String key,long timeout) throws CacheException;
	public void unlock(String key) throws CacheException;
	public void setBit(String key,long offset,boolean value) throws CacheException;
	public boolean isBitSet(String key,long offset) throws CacheException;
	public boolean setBitWhileUnset(String key,long offset,boolean value) throws CacheException;
	public Long bitpos(String key,boolean value)throws CacheException;
	public Long bitcount(String key) throws CacheException;
	public List<Long> getAllSetBits(String key) throws CacheException;
	public boolean mergeBit(String key,String otherKey)throws CacheException;
	public boolean isIntersected(String key,String otherKey)throws CacheException;
}
