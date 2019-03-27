package org.aztec.autumn.common.utils.net.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcProxy {

	public final static String DEFAULT_HOST_NAME = "192.168.1.102";
	public final static int DEFAULT_PORT = 1984;
	private final static Logger LOG = LoggerFactory.getLogger(RpcProxy.class);
	// public final static String[] candidateHosts = ;
	// public final static Integer[] ports;

	public static <T> T createProxy(Class<T> serviceCls, String hostName, Integer port) {
		if (serviceCls.isInterface()) {
			List<Class> proxyInfs = getAllInterfaces(serviceCls);
			return (T) Proxy.newProxyInstance(serviceCls.getClassLoader(), proxyInfs.toArray(new Class[proxyInfs.size()]),
					new RpcInvocationHandler(serviceCls, hostName == null ? DEFAULT_HOST_NAME : hostName,
							port == null ? DEFAULT_PORT : port));
		} else
			return (T) Proxy.newProxyInstance(serviceCls.getClassLoader(), serviceCls.getInterfaces(),
					new RpcInvocationHandler(serviceCls, hostName == null ? DEFAULT_HOST_NAME : hostName,
							port == null ? DEFAULT_PORT : port));
	}

	private static List<Class> getAllInterfaces(Class serviceCls) {
		List<Class> retClass = new ArrayList<>();
		retClass.add(serviceCls);
		Class[] interfaces = serviceCls.getInterfaces();
		if (interfaces != null && interfaces.length > 0) {
			for (Class interfaze : interfaces) {
				retClass.addAll(getAllInterfaces(interfaze));
			}
		}
		return retClass;
	}

	private static class RpcInvocationHandler implements InvocationHandler {

		private String hostName;
		private int port;
		private Class implementCls;
		private RpcClient client;

		public RpcInvocationHandler(Class implementCls, String hostName, int port) {
			this.hostName = hostName;
			this.port = port;
			this.implementCls = implementCls;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			
			client = new RpcClient(hostName, port);
			Object result = client.invoke(implementCls, method, args);
			//LOG.info("client hash code:" + client.hashCode());
			return result;
		}

		@Override
		protected void finalize() throws Throwable {
			super.finalize();
			client.disconnect();
			LOG.info("disconnect");
		}
		
	}

}
