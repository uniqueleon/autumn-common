package org.aztec.autumn.common.utils.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class AsyncSubscriber extends Thread{
	
	JedisPool pool;
	CacheDataSubscriber subscriber;
	String[] channels ;
	
	public AsyncSubscriber(JedisPool pool,CacheDataSubscriber subscriber,String[] channels) {
		this.pool = pool;
		this.subscriber = subscriber;
		this.channels = channels;
		this.setName(getThreadName(channels));
	}
	
	public String getThreadName(String[] channels) {
		StringBuilder nameBuilder = new StringBuilder();
		nameBuilder.append("REDIS_SUBSCRIBER_FOR#");
		for(String channel : channels) {
			nameBuilder.append(channel);
		}
		return nameBuilder.toString();
	}

	@Override
	public void run() {
		Jedis jedis = pool.getResource();
		jedis.subscribe(new DefaultSubscriber(subscriber), channels);
	}

}
