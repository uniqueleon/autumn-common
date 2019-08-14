package org.aztec.autumn.common.utils.cache;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class RedisUtilFactory implements PooledObjectFactory<RedisUtil> {
	
	private String[] hosts;
	private Integer[] ports;
	private String password;
	private static final AtomicInteger makeCount = new AtomicInteger(0);

	public RedisUtilFactory(String[] hosts, Integer[] port,String password) {
		// TODO Auto-generated constructor stub
		this.hosts = hosts;
		this.ports = port;
		this.password = password;
	}
	
	public static String getPoolKey(String[] hosts, Integer[] ports) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0;i < hosts.length;i++) {
			String host = hosts[i];
			Integer port = ports[i];
			builder.append(host + ":" + port + "_");
		}
		return builder.toString();
	}
	
	public static String getPoolKey(List<String> hosts, List<Integer> ports) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0;i < hosts.size();i++) {
			String host = hosts.get(i);
			Integer port = ports.get(i);
			builder.append(host + ":" + port + "_");
		}
		return builder.toString();
	}
	
	@Override
	public PooledObject<RedisUtil> makeObject() throws Exception {
		RedisUtil util = new RedisUtil(hosts, ports, password);
		int currentCount = makeCount.get();
		while(!makeCount.compareAndSet(currentCount, currentCount + 1)) {
			currentCount = makeCount.get();
		}
		System.out.println("current make size:" + makeCount.get());
		return new DefaultPooledObject<RedisUtil>(util);
	}

	@Override
	public void destroyObject(PooledObject<RedisUtil> p) throws Exception {
		p.getObject().close();
	}

	@Override
	public boolean validateObject(PooledObject<RedisUtil> p) {
		return p.getObject().isUsable();
	}

	@Override
	public void activateObject(PooledObject<RedisUtil> p) throws Exception {
		p.getObject().connect();
	}

	@Override
	public void passivateObject(PooledObject<RedisUtil> p) throws Exception {
		//p.getObject().disconnect();
	}

}
