package org.aztec.autumn.common.utils;

import org.aztec.autumn.common.utils.cache.CacheDataSubscriber;

public class RedisTest {

	public RedisTest() {
		// TODO Auto-generated constructor stub
	}

	public static void testPublish() throws CacheException {


		CacheUtils cacheUtil = UtilsFactory.getInstance().getCacheUtils("metacenter.aztec.org", 6379);

		cacheUtil.publish("SUCK_CHANNEL", "OK_MAN!");
	}
	
	public static void startSubscriber() throws CacheException {

		CacheUtils cacheUtil = UtilsFactory.getInstance().getCacheUtils("metacenter.aztec.org", 6379);
		cacheUtil.subscribe(new TestSubscriber(), "SUCK_CHANNEL");
	}
	
	public static void main(String[] args) {
		try {
			//startSubscriber();
			testPublish();
			System.out.println("suck");
		} catch (CacheException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
