package org.aztec.autumn.common.utils;

import java.util.List;

import org.aztec.autumn.common.utils.concurrent.AbstractSynchronizableData;
import org.aztec.autumn.common.utils.concurrent.NoLockDataSynchronizer;
import org.aztec.autumn.common.utils.concurrent.NoLockException;
import org.aztec.autumn.common.utils.concurrent.Synchronizable;
import org.aztec.autumn.common.utils.concurrent.SynchronizerFactory;
import org.aztec.autumn.common.utils.concurrent.SynchronizerFactory.SynchronizerNames;

import com.beust.jcommander.internal.Lists;

/**
 * 分布式累加计数器.多个分布式线程同时并发，去读取共享的计数对象，并进行+1操作，最终结果应该等于所有并发线程的总数
 * 
 * 
 * @author 10064513
 *
 */
public class ConcurrentTest2 {
	
	private static final String REDIS_TEST_COUNT_KEY = "TEST_CONCURRENT_COUNT";
	private static final String REDIS_TEST_LOCK_KEY = "TEST_CONCURRENT_COUNT_LOCK";
	private static final int mode = 4;
	private static final int startData = 0;
	private static final long BUSSINESS_COST = 100l;
	private static String uuid;

	private static NoLockDataSynchronizer synchronizer = SynchronizerFactory.getSynchronizer(SynchronizerNames.DISTRIBUTED);
	private static CacheUtils cacheUtil = UtilsFactory.getInstance().getDefaultCacheUtils();

	public ConcurrentTest2() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		try {
			cacheUtil.unlock(REDIS_TEST_LOCK_KEY);
//			uuid = UUID.randomUUID().toString();
//			RedisTreeNode rtn = new RedisTreeNode(new ConcurrentCountNumber(uuid,0, new int[] {}));
//			JsonUtils jsonUtil = UtilsFactory.getInstance().getJsonUtils();
//			System.out.println(jsonUtil.object2Json(rtn));;
			doTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void doTest() {
		try {
			cacheUtil.cache(REDIS_TEST_COUNT_KEY,"" + startData);
			Long curTime = System.currentTimeMillis();
			uuid = StringUtils.getRamdonNumberString(20);
			int threadNum = 100;
			List<TestSafeThread> tList=  Lists.newArrayList();
			for(int i = 0;i < threadNum;i++) {
				TestSafeThread sat = new TestSafeThread(i);
				sat.start();
				tList.add(sat);
			}
			for(TestSafeThread t : tList) {
				t.join();
			}
			Integer result = TestSafeThread.getResult();
			Long useTIme = System.currentTimeMillis() - curTime;
			System.out.println("finalResult:" + result + " use time:" + useTIme);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static class ConcurrentCountNumber extends AbstractSynchronizableData<Integer>{
		
		
		public ConcurrentCountNumber() {
			super(null,null, new int[] {});
		}
		
		public ConcurrentCountNumber(String uuid,Integer data, int[] slots) {
			super(data,uuid, slots);
			// TODO Auto-generated constructor stub
		}
		
		public ConcurrentCountNumber(String uuid, int[] slots, Integer data, String version, String nextVersion,
				String previousVersion, int dept) {
			super(uuid, slots, data, version, nextVersion, previousVersion, dept);
			// TODO Auto-generated constructor stub
		}


		@Override
		public void merge(Synchronizable<Integer> otherNode,Synchronizable<Integer> branchNode) {
			// TODO Auto-generated method stub
			int supplement  = 0;
			if(branchNode != null) {
				supplement = -branchNode.getData();
			}
			this.data += otherNode.getData() + supplement;
		}

		@Override
		public AbstractSynchronizableData<Integer> cloneFromThis() {
			return new ConcurrentCountNumber(uuid, data, slots);
		}
		
	}
	
	
	public static class TestSafeThread extends Thread {
		
		private int index;
		
		public TestSafeThread(int index) {
			this.index = index;
		}

		@Override
		public void run() {
			try {
				switch(mode) {
				case 1 :
					runInNoLock();
					break;
				case 2:
					runInSynchronizedLock();
					break;
//				case 3:
//					runInCASNoLock();
//					break;
				case 4:
					runInNewNoLock();
					break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public Integer getNewValue(Integer targetData) throws InterruptedException {
			//模拟执行业务的消耗
			//Thread.currentThread().sleep(BUSSINESS_COST);
			return targetData + 1;
		}
		
		public static Integer getResult() throws NoLockException, NumberFormatException, CacheException {
			Integer startData = getFinalResult();
			switch(mode) {
			case 1 :
				return startData;
			case 2:
				return startData;
			case 4:
				synchronizer.merge(uuid);
				return (Integer) synchronizer.getNewestVersion(uuid).getData();
			}
			return null;
		}
		
		public static Integer getFinalResult() throws NumberFormatException, CacheException {
			Integer count = Integer.parseInt(cacheUtil.get(REDIS_TEST_COUNT_KEY,String.class));
			return count;
		}
		
		
		public void runInNoLock() throws InterruptedException, NumberFormatException, CacheException {
			Integer count = Integer.parseInt(cacheUtil.get(REDIS_TEST_COUNT_KEY,String.class));
			count ++;
			cacheUtil.cache(REDIS_TEST_COUNT_KEY, "" + count);
		}
		
		public void runInSynchronizedLock() throws InterruptedException, CacheException {
			cacheUtil.lock(REDIS_TEST_LOCK_KEY, 3000);
			Integer count = Integer.parseInt(cacheUtil.get(REDIS_TEST_COUNT_KEY,String.class));
			count ++;
			cacheUtil.cache(REDIS_TEST_COUNT_KEY, "" + count);
			cacheUtil.unlock(REDIS_TEST_LOCK_KEY);
		}
		
		
		public void runInCASNoLock() throws InterruptedException {

			/*
			 * int thisValue = atomData.get(); if(isValid(thisValue,index)) { int
			 * targetValue = getNewValue(thisValue);
			 * while(!atomData.compareAndSet(thisValue,targetValue)) { thisValue =
			 * atomData.get(); if(!isValid(thisValue,index)) { break; } targetValue =
			 * getNewValue(thisValue); } }
			 */
		}
		
		public void runInNewNoLock() throws NoLockException, InterruptedException {
			Synchronizable<Integer> syncData = synchronizer.getNewestVersion(uuid);
			if(syncData == null) {
				 syncData = new ConcurrentCountNumber( uuid,startData,new int[] {index});
				 syncData = synchronizer.synchronize(syncData);
			}
			syncData.update(getNewValue(syncData.getData()));
			synchronizer.synchronize(syncData);
		}
		
	}
}
