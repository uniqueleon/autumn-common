package org.aztec.autumn.common.utils.cache;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;

public class RedisPool {
	private static final Map<HostAndPort,JedisPool> pools = Maps.newConcurrentMap();
	private static final int DEFAUL_CAPACITY = 10;
	private static final Map<String,Object> locks = Maps.newConcurrentMap();
	//private static final Queue<JedisCluster> clusters = Queues.newArrayBlockingQueue(DEFAUL_CAPACITY);

	public RedisPool() {
		// TODO Auto-generated constructor stub
	}
	
	public static void createPool(List<String> hosts,List<Integer> ports,String password) {
		if(hosts.size() != ports.size()) {
			throw new IllegalArgumentException("hosts and ports information not matched!");
		}
		for(int i = 0;i < hosts.size();i++) {
			String host = hosts.get(i);
			Integer port = ports.get(i);
			HostAndPort hp = new HostAndPort(host, port);
			if(!pools.containsKey(hp)) {
				synchronized (pools) {
					if(!pools.containsKey(hp)){
						JedisPool pool = new JedisPool(new GenericObjectPoolConfig(),host,port,10000,password);
						pools.put(hp,pool);
					}
				}
			}
		}
	}
	
	public static JedisCommands getRedisClient(List<String> hosts,List<Integer> ports,String password) {
		if(hosts.size() == 1) {
			HostAndPort hp = new HostAndPort(hosts.get(0), ports.get(0));
			JedisPool pool = pools.get(hp);
			if(pool == null) {
				createPool(hosts, ports, password);
			}
			return pools.get(hp).getResource();
		}
		else {
			Set<HostAndPort> clusterConfig = new HashSet<>();
			for (int i = 0; i < hosts.size(); i++) {
				clusterConfig.add(new HostAndPort(hosts.get(i), ports.get(i)));
			}
			JedisCluster jc = new JedisCluster(clusterConfig);
			if(password != null && !password.isEmpty())
				jc.auth(password);
			return jc;
		}
	}

}
