package org.aztec.autumn.common.utils.ws;

import java.lang.reflect.Proxy;

public class ApiProxyFactory {

	public static <T> T getRestfulApiProxy(String baseUrl, Class<T> proxyClass) {
		if (proxyClass.isInterface())
			return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
					new Class[] { proxyClass }, new RestApiProxy(baseUrl));
		else
			throw new IllegalArgumentException("Unsupported service class[" + proxyClass + "]");
	}

}
