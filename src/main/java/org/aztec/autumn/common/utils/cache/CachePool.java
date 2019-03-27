package org.aztec.autumn.common.utils.cache;

/**
 * ���ػ��������
 * @author 10064513
 *
 */
public interface CachePool {

	public LocalCache getDefaultCache() throws Exception;
	/**
	 * ��ȡ�����
	 * @param poolName ���������
	 * @return
	 */
	public LocalCache getCache(String poolName) throws Exception ;
	/**
	 * ����һ�ݻ��������
	 * @param pool
	 * @return
	 */
	public LocalCache copy(LocalCache pool) throws Exception ;
	/**
	 * ���ݵ�ǰ����ش���һ����ʱ�����
	 * @param sample
	 * @return
	 */
	public LocalCache createTemp(LocalCache sample) throws Exception ;
}
