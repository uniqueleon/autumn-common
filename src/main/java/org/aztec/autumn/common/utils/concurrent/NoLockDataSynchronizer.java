package org.aztec.autumn.common.utils.concurrent;

public interface NoLockDataSynchronizer {

	/**
	 * 将数据对象进行不加锁同步操作
	 * @param target
	 * @return
	 * @throws NoLockException
	 */
	public Synchronizable synchronize(Synchronizable target) throws NoLockException;

	/**
	 * 将目标对象从同步对象池中释放
	 * @param target
	 * @throws NoLockException
	 */
	public void release(Synchronizable target) throws NoLockException;

	/**
	 * 获取最新的同步对象，在这个过程会自动计算所有同步对象的版本号，一般在merge方法后执行，非线程安全，请慎用
	 * @return
	 * @throws NoLockException
	 */
	public Synchronizable getNewestVersion(String uuid) throws NoLockException;
	/**
	 * 合并多个差异版本,非线程安全的，请慎用
	 * @return
	 * @throws NoLockException
	 */
	public void merge(String uuid) throws NoLockException;

	public static final int DEFAULT_VERSION_LENGTH = 36;
}
