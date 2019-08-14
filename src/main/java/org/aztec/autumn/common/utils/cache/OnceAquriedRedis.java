package org.aztec.autumn.common.utils.cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.aztec.autumn.common.utils.CacheUtils;

public class OnceAquriedRedis implements InvocationHandler {
	
	private String[] hosts;
	private Integer[] port;
	private String password;
	
	public OnceAquriedRedis(String[] hosts,Integer[] port,String password) {
		this.hosts = hosts;
		this.port = port;
		this.password = password;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		CacheUtils util = CachePoolManager.getCacheUtils(hosts, port,password);
		
		try {
			return method.invoke(util, args);
		} catch (Exception e) {
			throw e;
		}finally {
			if(CacheUtils.class.isAssignableFrom(proxy.getClass())) {
				try {
					CachePoolManager.releaseCacheUtils(util);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
