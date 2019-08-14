package org.aztec.autumn.common.utils;

import java.util.List;
import java.util.UUID;

import org.aztec.autumn.common.utils.cache.CacheDataSubscriber;

import com.beust.jcommander.internal.Lists;

public class RedisTest {
	
	//private static CacheUtils cacheUtil = UtilsFactory.getInstance().getCacheUtils("metacenter.aztec.org", 14785,"bbq$$!@*$pepper1984oni");
	private static CacheUtils cacheUtil = UtilsFactory.getInstance().getCacheUtils("metacenter.aztec.org", 6379,"bbq$$!@*$pepper1984oni");
	//private static CacheUtils cacheUtil = UtilsFactory.getInstance().getCacheUtils("metacenter.aztec.org", 6379);
	
	public RedisTest() {
		// TODO Auto-generated constructor stub
	}

	public static void testPublish() throws CacheException {

		cacheUtil.publish("SUCK_CHANNEL", "OK_MAN!");
	}
	
	public static void startSubscriber() throws CacheException {

		cacheUtil.subscribe(new TestSubscriber(), "SUCK_CHANNEL");
	}
	
	public static void testMergeBit() throws CacheException{

		String cacheKey = "TEST_SET_BIT";
		String cacheKey2 = "TEST_SET_BIT2";
		cacheUtil.remove(cacheKey);
		cacheUtil.remove(cacheKey2);
		cacheUtil.setBit(cacheKey, 5, true);
		cacheUtil.setBit(cacheKey, 6, true);
		cacheUtil.setBit(cacheKey, 7, true);
		cacheUtil.setBit(cacheKey, 12, true);
		cacheUtil.setBit(cacheKey, 14, true);
		cacheUtil.setBit(cacheKey2, 22, true);
		cacheUtil.setBit(cacheKey2, 42, true);
		cacheUtil.setBit(cacheKey2, 43, true);
		cacheUtil.setBit(cacheKey2, 63, true);
		cacheUtil.setBit(cacheKey2, 10785, true);
		cacheUtil.mergeBit(cacheKey, cacheKey2);
		System.out.println(cacheUtil.isBitSet(cacheKey, 10785));
		System.out.println(cacheUtil.bitpos(cacheKey, true));
		System.out.println(cacheUtil.bitcount(cacheKey));
		System.out.println(cacheUtil.getAllSetBits(cacheKey));
		
	}
	
	public static void testSetBit() throws CacheException{

		String cacheKey = "TEST_SET_BIT";
		
		cacheUtil.remove(cacheKey);
		cacheUtil.setBit(cacheKey, 5, true);
		cacheUtil.setBit(cacheKey, 6, true);
		cacheUtil.setBit(cacheKey, 7, true);
		cacheUtil.setBit(cacheKey, 12, true);
		cacheUtil.setBit(cacheKey, 14, true);
		cacheUtil.setBit(cacheKey, 22, true);
		cacheUtil.setBit(cacheKey, 42, true);
		cacheUtil.setBit(cacheKey, 43, true);
		cacheUtil.setBit(cacheKey, 63, true);
		cacheUtil.setBit(cacheKey, 10785, true);
		
		System.out.println(cacheUtil.isBitSet(cacheKey, 10785));
		System.out.println(cacheUtil.bitpos(cacheKey, true));
		System.out.println(cacheUtil.bitcount(cacheKey));
		System.out.println(cacheUtil.getAllSetBits(cacheKey));
		
	}
	
	public static void main(String[] args) {
		try {
			//startSubscriber();
			//testPublish();
			//testSetBit();
//			testMergeBit();
//			System.out.println(0 | 8);
			//System.setProperty("system","res:base.properties");
			PerformanceTest(100,200);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void PerformanceTest(int testTime,int testThread) throws InterruptedException {
		Long beginTime = System.currentTimeMillis();
		List<PerformanceTester> perThreads = Lists.newArrayList();
		for(int i = 0;i < testThread;i++) {
			PerformanceTester tester = new PerformanceTester(testTime);
			tester.start();
			perThreads.add(tester);
		}
		for(PerformanceTester tester : perThreads) {
			tester.join();
		}
		System.out.println("use time:" + (System.currentTimeMillis() - beginTime));
	}
	
	public static class PerformanceTester extends Thread{
		
		
		String cacheKey;
		private int testTime;
		
		public PerformanceTester(int testTime) {
			cacheKey = UUID.randomUUID().toString();
			this.testTime = testTime;
		}

		@Override
		public void run() {
			try {
				CacheUtils util = UtilsFactory.getInstance().getDefaultCacheUtils();
				for(int i = 0;i < testTime;i++) {
					util.cache(cacheKey, StringUtils.getRandomCharNumberString(100));
					//Thread.sleep(1);
					util.remove(cacheKey);
				}
				UtilsFactory.getInstance().releaseCacheUtils(util);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	private static  class TestSubscriber implements CacheDataSubscriber{

		@Override
		public void notify(String channel, String newMsg) {
			// TODO Auto-generated method stub
			System.out.println(channel + "_" + newMsg);
		}

		@Override
		public void unsubscribe() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isUnsubscribed() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
}
