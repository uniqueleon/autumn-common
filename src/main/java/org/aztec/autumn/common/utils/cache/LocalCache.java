package org.aztec.autumn.common.utils.cache;

import java.util.List;
import java.util.Map;

/**
 * �������ݳ�
 * @author 10064513
 *
 */
public interface LocalCache {

	/**
	 * ����س�ʼ��
	 * @param initParams
	 * @throws Exception
	 */
	public void init(Object[] initParams) throws Exception;
	/**
	 * ��ȡ��
	 * @return
	 */
	public boolean aquriedLook() throws Exception;
	/**
	 * �ͷ���
	 * @return
	 */
	public void releaseLock() throws Exception;
	/**
	 * ��������
	 * @param key
	 * @param value
	 */
	public void put(Object key,Object value) throws Exception;

	/**
	 * ������������
	 * @param key
	 * @param values
	 */
	public List<Object> batchPut(List<Object> values,String idField) throws Exception;
	/**
	 * ��ȡ����
	 * @param key
	 * @return
	 */
	public <E> E get(Object key);
	/**
	 * ������ȡ����
	 * @param keys
	 * @return
	 */
	public <E> List<E> batchGetAsList(List<Object> keys) throws Exception;
	
	public void deleteCache(Map<String, ?> cacheModel);
	
	public <T> long hdelCache(String key, String... fields);

	public <E> Map<String,E> batchGet(String[] keys) throws Exception;
	
	public <T> Map<String, T> hgetAllCache(final String... keys) throws Exception;
	
	public void deleteCache(String key);
	
	public <T> T getCache(String key);
	
	public <T> Map<String, T> hgetAllCache(String key);
	
	public void setCache(String key, Object object);
	
	public <T> void batchSetCache(Map<String, T> cacheModel);
	
	public <T> void batchHset(Map<String, Map<String, T>> valueMap) ;
	
	public void usingTmpCache();
	
	public void temp2LocalCache();
	
	public void initEntityCacheRefresher();
	
	public void setNullValue(String key);
	
	public Boolean hasNullValue(String key);
	
	public <T> Map<String, T> batchHget(String key, String... fields);
	/**
	 * ���õ�ǰ������Ƿ���
	 * @param enabled
	 */
	public void setEnable(boolean enabled) throws Exception;
	/**
	 * ��ȡ��ǰ����صĿ���״̬
	 * @return
	 */
	public boolean isEnabled() throws Exception;
	/**
	 * ��ȡ��ǰ���������
	 * @return
	 */
	public String getName() throws Exception;
	
	/**
	 * ���ٴ����ӳ�
	 * @throws Exception
	 */
	public void destroy() throws Exception;
}
