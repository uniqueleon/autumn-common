package org.aztec.autumn.common.utils.cache.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections.MapUtils;
import org.aztec.autumn.common.utils.cache.LocalCache;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhCache implements LocalCache{
	
	private Cache innerCache;
	private ReentrantLock lock;
	private AtomicBoolean init = new AtomicBoolean(false);
	private boolean enabled = true;
	private String name;
	private static final String DEFAULT_CACHE_NAME = "defaultCacheConfig";
	private static final String NULL_VALUE_KEY = "nullValues";
	private static final ThreadLocal<ConcurrentMap<String, Object>> localMap = new ThreadLocal<>(); 

	@Override
	public boolean aquriedLook() {
		return lock.tryLock();
	}

	@Override
	public void releaseLock() {
		lock.unlock();
	}

	@Override
	public void put(Object key, Object value) {
		innerCache.put(new Element(getCacheKey(key),value));
	}
	
	private String getCacheKey(Object key){
		return name + "_" + key.toString();
	}

	@Override
	public <E> E get(Object key) {
		Element cacheObj =  innerCache.get(getCacheKey(key));
		return cacheObj != null ? (E) cacheObj.getObjectValue() : null;
	}

	@Override
	public void init(Object[] initParams) throws Exception {
		if(init.get()){
			return ;
		}
		synchronized (init) {
			if(init.get())
				return ;
			if(initParams == null || initParams.length < 2){
				throw new Exception("The initial parameter has not provided!");
			}
			CacheManager cacheManager = (CacheManager) initParams[0];
			if(initParams[1] == null){
				innerCache = cacheManager.getCache(DEFAULT_CACHE_NAME);
				return ;
			}
			name = (String)initParams[1];
			innerCache = cacheManager.getCache(name);
			if(innerCache == null){
				innerCache = cacheManager.getCache(DEFAULT_CACHE_NAME);
			}
			init.set(true);
		}
	}

	@Override
	public void setEnable(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public List<Object> batchPut(List<Object> values, String idField) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		List<Object> idObjects = Lists.newArrayList();
		if(values == null)
			return values;
		for(Object value : values){
			Object id = getId(value,idField);
			put(id,value);
			idObjects.add(id);
		}
		return idObjects;
	}
	
	private Object getId(Object value,String idField) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		Field field = value.getClass().getDeclaredField(idField);
		field.setAccessible(true);
		Object fieldObj = field.get(value);
		return fieldObj != null ? fieldObj : null;
	}

	@Override
	public <E> List<E> batchGetAsList(List<Object> keys) {
		List<E> retList = Lists.newArrayList();
		if(keys == null || keys.size() == 0)
			return retList;
		for(Object key : keys){
			Object cacheObj = get(key);
			if(cacheObj != null)
				retList.add((E) cacheObj);
		}
		return retList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void destroy() throws Exception {
		this.innerCache.removeAll();
		
	}

	@Override
	public <T> Map<String, T> hgetAllCache(String... keys) throws Exception {
		// TODO Auto-generated method stub
		Map<String,T> retMap = Maps.newHashMap();
		for(String key : keys){
			Object cacheObj = get(key);
			if(cacheObj != null){
				retMap.put(key, (T) cacheObj);	
			}
		}
		return retMap;
	}

	@Override
	public void deleteCache(String key) {
		// TODO Auto-generated method stub
		innerCache.remove(key);
	}

	@Override
	public <T> T getCache(String key) {
		// TODO Auto-generated method stub
		return get(key);
	}

	@Override
	public <T> Map<String, T> hgetAllCache(String key) {
		List<String> indexs  = get(key);
		Map<String,T> retMap = Maps.newHashMap();
		if(indexs != null){
			for(String index : indexs){
				Element entity = innerCache.get(index);
				retMap.put(index, (T) entity.getObjectValue());
			}
		}
		return retMap;
	}

	@Override
	public void setCache(String key, Object object) {
		// TODO Auto-generated method stub
		put(key, object);
	}

	@Override
	public <T> void batchSetCache(Map<String, T> cacheModel) {
		if(cacheModel == null)
			return;
		for(String key : cacheModel.keySet()){
			innerCache.put(new Element(key,cacheModel.get(key)));
		}
	}
	
	/**
	 * �������ö����ϣ����ֵ
	 * 
	 * @param valueMap
	 * @throws RedisException
	 */
	public <T> void batchHset(Map<String, Map<String, T>> valueMap) {
		if (MapUtils.isEmpty(valueMap))
			return;
		
		for (String key : valueMap.keySet()) {
			Map<String,T> valueObjectMap = valueMap.get(key);
			List<String> valueIndexs =  Lists.newArrayList();
			for(String valueKey : valueObjectMap.keySet()){
				valueIndexs.add(valueKey);
				innerCache.put(new Element(valueKey,valueObjectMap.get(valueKey)));
			}
			innerCache.put(new Element(key,valueIndexs));
		}
	}

	@Override
	public <E> Map<String, E> batchGet(String[] keys) throws Exception {
		// TODO Auto-generated method stub
		Map<String,E> retMap = Maps.newHashMap();
		if(keys == null)
			return retMap;
		for(String key : keys){
			Element element =  innerCache.get(key);
			if(element != null){

				retMap.put(key, (E) element.getObjectValue());
			}
		}
		return retMap;
	}

	public void usingTmpCache() {
		ConcurrentMap<String, Object> temp = Maps.newConcurrentMap();
		localMap.set(temp);
	}

	public void temp2LocalCache() {
		try {
			ConcurrentMap<String, Object> tempMap = localMap.get();
			if (MapUtils.isNotEmpty(tempMap)) {
				//for(Key)
			}
		} finally {
			localMap.remove();
		}
	}

	@Override
	public void initEntityCacheRefresher() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNullValue(String key) {
		// TODO Auto-generated method stub
		Element nullValueKeys =  innerCache.get(NULL_VALUE_KEY);
		List<String> nullValueIndexs = Lists.newArrayList();
		if(nullValueKeys != null){
			nullValueIndexs = (List<String>) nullValueKeys.getObjectValue();
		}
		nullValueIndexs.add(key);
		innerCache.put(new Element(NULL_VALUE_KEY,nullValueIndexs));
	}

	@Override
	public Boolean hasNullValue(String key) {
		Element nullValueKeys =  innerCache.get(NULL_VALUE_KEY);
		List<String> nullValueIndexs = Lists.newArrayList();
		if(nullValueKeys != null){
			nullValueIndexs = (List<String>) nullValueKeys.getObjectValue();
			return nullValueIndexs.contains(key);
		}
		else {
			return false;
		}
	}

	@Override
	public <T> Map<String, T> batchHget(String key, String... fields) {
		Object nullValueKeys =  get(key);
		List<String> indexs = Lists.newArrayList();
		if(nullValueKeys != null){
			indexs = (List<String>) nullValueKeys;
			Map<String, T> retMap = Maps.newHashMap();
			if(fields == null)
				return retMap;
			for(String field : fields){
				if(indexs.contains(field)){
					retMap.put(field, (T) innerCache.get(field));
				}
			}
			return retMap;
		}
		else 
			return Maps.newHashMap();
	}

	@Override
	public void deleteCache(Map<String, ?> cacheModel) {
		// TODO Auto-generated method stub
		if (MapUtils.isEmpty(cacheModel))
			return;

		for (String key : cacheModel.keySet()) {
			innerCache.remove(key);
		}
	}

	@Override
	public <T> long hdelCache(String key, String... fields) {
		Object keys =  get(key);
		List<String> indexs = Lists.newArrayList();
		if(keys != null){
			indexs = (List<String>) keys;
			long count = 0l;
			for(String field : fields){
				if(indexs.contains(field)){
					indexs.remove(field);
					count++;
				}
			}
			return count;
		}
		else 
			return 0l;
	}

}
