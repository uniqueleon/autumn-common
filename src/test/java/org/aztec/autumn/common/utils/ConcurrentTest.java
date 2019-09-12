package org.aztec.autumn.common.utils;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.aztec.autumn.common.utils.concurrent.AbstractSynchronizableData;
import org.aztec.autumn.common.utils.concurrent.NoLockDataSynchronizer;
import org.aztec.autumn.common.utils.concurrent.NoLockException;
import org.aztec.autumn.common.utils.concurrent.Synchronizable;
import org.aztec.autumn.common.utils.concurrent.SynchronizerFactory;
import org.aztec.autumn.common.utils.concurrent.SynchronizerFactory.SynchronizerNames;

import com.beust.jcommander.internal.Lists;

public class ConcurrentTest {
	
	private static Integer startData = 0;
	private static AtomicInteger atomData = new AtomicInteger(startData);
	private static NoLockDataSynchronizer synchronizer = SynchronizerFactory.getSynchronizer(SynchronizerNames.MEMORY);
	private static Random random = new Random();
	private static String uuid;
	//1. 完全无锁 . 2.对象锁 3.CAS无锁 4.新无锁并发
	private static int mode = 2;
	private static int modulus = 6;
	private static final long BUSSINESS_COST = 100l;
	public ConcurrentTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		doTest();
	}
	
	public static void doTest() {
		try {
			Long curTime = System.currentTimeMillis();
			uuid = StringUtils.getRamdonNumberString(20);
			int threadNum = 100;
			List<TestSafeThread> tList=  Lists.newArrayList();
			for(int i = 0;i < threadNum;i++) {
				TestSafeThread sat = new TestSafeThread(i % modulus);
				sat.start();
				tList.add(sat);
			}
			for(TestSafeThread t : tList) {
				//t.start();
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
	public static boolean isValid(Integer testData,int index) {
		// 66666 2
		// mod = 1000
		// div = 100
		int mod = new Double(Math.pow(10, (index + 1 ) * 1d)).intValue();
		int div = new Double(Math.pow(10, (index ) * 1d)).intValue();
		int testValue =( testData % mod ) /div;
		return testValue == 0;
	}
	
	// (0 + 3) / 2
	// 666666
	
	//public static 
	
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
				case 3:
					runInCASNoLock();
					break;
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
			Thread.currentThread().sleep(BUSSINESS_COST);
			return targetData + new Double(Math.pow(10, index * 1d) * modulus).intValue();
		}
		
		public static Integer getResult() throws NoLockException {
			switch(mode) {
			case 1 :
				return startData;
			case 2:
				return startData;
			case 3:

				return atomData.get();
			case 4:
				synchronizer.merge(uuid);
				return (Integer) synchronizer.getNewestVersion(uuid).getData();
			}
			return null;
		}
		
		
		public void runInNoLock() throws InterruptedException {
			if(isValid(startData,index)) {
				startData = getNewValue(startData);
				
			}
		}
		
		public void runInSynchronizedLock() throws InterruptedException {
			synchronized (startData) {
				runInNoLock();
			}
		}
		
		
		public void runInCASNoLock() throws InterruptedException {

			int thisValue = atomData.get();
			if(isValid(thisValue,index)) {
				int targetValue = getNewValue(thisValue);
				while(!atomData.compareAndSet(thisValue,targetValue)) {
					thisValue = atomData.get();
					if(!isValid(thisValue,index)) {
						break;
					}
					targetValue = getNewValue(thisValue);
				}
			}
		}
		
		public void runInNewNoLock() throws NoLockException, InterruptedException {
			Synchronizable<Integer> syncData = synchronizer.getNewestVersion(uuid);
			if(syncData == null) {
				syncData = new IntegerMergible(startData.intValue(), uuid,new int[] {index},index);
				syncData = synchronizer.synchronize(syncData);
			}
			//同步过数据后，index值可能被更新了，于是需要重新设置
			IntegerMergible cloneOne = syncData.cast();
			cloneOne.setIndex(index);
			if(syncData != null) {
				if(isValid(cloneOne.getData(), index)) {
					syncData.update(getNewValue(syncData.getData()));
					synchronizer.synchronize(syncData);
				}
			}
		}
		
	}

	public static class IntegerMergible extends AbstractSynchronizableData<Integer>{
		
		private int index;
		

		public IntegerMergible(Integer data,String uuid, int[] slots,int index) {
			super(data, uuid ,slots);
			this.index = index;
		}

		@Override
		public void merge(Synchronizable<Integer> otherNode,Synchronizable<Integer> conflictNode) {
			Integer thisOne = this.getData();
			IntegerMergible othr = otherNode.cast();
			Integer otherOne = othr.getAddValue();
			update(thisOne + otherOne);
		}
		
		public Integer getAddValue() {

			int mod = new Double(Math.pow(10, (index + 1 ) * 1d)).intValue();
			int div = new Double(Math.pow(10, (index ) * 1d)).intValue();
			int testValue =( getData() % mod ) /div;
			testValue *= div;
			return testValue;
		}
		
		

		@Override
		public boolean isMergable(Synchronizable<Integer> otherNode) {
			IntegerMergible int1 = otherNode.cast();
			return int1.index != index && isValid(data,int1.index);
		}


		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		@Override
		public AbstractSynchronizableData<Integer> cloneFromThis() {
			return new IntegerMergible(index, previousVersion, slots, index);
		}
		
	}
	
}
