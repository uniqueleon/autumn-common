package org.aztec.autumn.common.utils.cache;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class AsyncSubscriber extends Thread{
	
	private Jedis jedis;
	private JedisCluster cluster;
	CacheDataSubscriber subscriber;
	String[] channels ;
	
	public AsyncSubscriber(List<String> hosts,List<Integer> ports,String password,CacheDataSubscriber subscriber,String[] channels) {
		JedisPool pool = new JedisPool();
		this.subscriber = subscriber;
		this.channels = channels;
		if(hosts.size() > 1) {
			cluster = RedisPool.createCluster(hosts, ports, password);
		}
		else {
			jedis = RedisPool.createPool(hosts.get(0), ports.get(0), password).getResource();
		}
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
		if(jedis != null) {
			jedis.subscribe(new DefaultSubscriber(subscriber), channels);
		}
		else if(cluster != null){
			cluster.subscribe(new DefaultSubscriber(subscriber), channels);
		}
	}

}
