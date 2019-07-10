package org.aztec.autumn.common.utils.cache;

import org.aztec.autumn.common.utils.CacheUtils;

import redis.clients.jedis.JedisPubSub;

public class DefaultSubscriber extends JedisPubSub{

	CacheDataSubscriber subscriber;
	CacheUtils cacheUtil;
	
	
	public DefaultSubscriber(CacheDataSubscriber subscriber) {
		this.subscriber = subscriber;
	}

	@Override
	public void onMessage(String channel, String message) {
		if(subscriber.isUnsubscribed()) {
			unsubscribe();
			return;
		}
		subscriber.notify(channel,message);	
	}


}
