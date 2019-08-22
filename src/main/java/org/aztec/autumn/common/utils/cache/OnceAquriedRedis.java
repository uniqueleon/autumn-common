package org.aztec.autumn.common.utils.cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.aztec.autumn.common.utils.CacheUtils;

public class OnceAquriedRedis implements InvocationHandler {
	
	private String[] hosts;
	private Integer[] port;
	private String password;
	private static ThreadLocal<CacheUtils> lockUtils = new ThreadLocal<>();
	
	public OnceAquriedRedis(String[] hosts,Integer[] port,String password) {
		this.hosts = hosts;
		this.port = port;
		this.password = password;
	}
	
	public boolean isReleasable(Object proxy, Method method, Object[] args) {
		return lockUtils.get() == null && CacheUtils.class.isAssignableFrom(proxy.getClass());
	}
	
	public void markLock(Object proxy, Method method, Object[] args,CacheUtils util) {
		if(method.getName().equals("lock")) {
			lockUtils.set(util);
		}
	}
	
	public void markUnLock(Object proxy, Method method, Object[] args) {
		if(method.getName().equals("unlock")) {
			lockUtils.set(null);
		}
	}
	
	public CacheUtils getCacheUtils() {
		if(lockUtils.get() != null) {
			return lockUtils.get();
		}
		return  CachePoolManager.getCacheUtils(hosts, port,password);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		CacheUtils util = getCacheUtils();
		try {
			Object retObj = method.invoke(util, args);
			markLock(proxy, method, args,util);
			markUnLock(proxy, method, args);
			return retObj;
		} catch (Exception e) {
			throw e;
		}finally {
			if(isReleasable(proxy, method, args)) {
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
