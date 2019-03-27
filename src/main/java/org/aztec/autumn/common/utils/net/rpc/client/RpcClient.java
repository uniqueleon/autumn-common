package org.aztec.autumn.common.utils.net.rpc.client;

import java.io.IOException;
import java.lang.reflect.Method;

import org.aztec.autumn.common.utils.net.rpc.entity.RpcInvokeParam;
import org.aztec.autumn.common.utils.net.rpc.entity.RpcInvokeResult;
import org.aztec.autumn.common.utils.net.rpc.exception.RpcInvokeException;

public class RpcClient {

	private RpcClientSocket socket;
	private String hostName;
	private int port;

	public RpcClient(String hostName, int port) throws IOException {
		socket = new RpcClientSocket(hostName,port);
		this.hostName = hostName;
		this.port = port;
	}

	public Object invoke(Class remoteObjClass, Method method,
			Object[] params) throws Throwable {
		// sendInvokeParam(rpcInvParam);
		socket.connect();
		socket.sendInvokeParam(wrapInvokeParam(remoteObjClass,method,params));
		return getInvokeResult();
	}
	
	public RpcInvokeParam wrapInvokeParam(Class remoteObjectClass,
			Method method, Object[] params) {
		Class[] paramTypes = method.getParameterTypes();
		String[] paramTypeNames = new String[paramTypes.length];
		for (int i = 0; i < paramTypes.length; i++) {
			paramTypeNames[i] = paramTypes[i].getName();
		}
		return new RpcInvokeParam(remoteObjectClass.getName(),
				method.getName(), paramTypeNames, method
						.getReturnType().getName(), params);
	}

	private Object getInvokeResult() throws Throwable {
	
		RpcInvokeResult result = socket.getInvokeResult();
		if (result != null) {
			if (result.getResult() != null)
				return result.getResult();
			else if (result.getException() != null)
				throw result.getException();
			else 
				return null;
		}
		else 
			throw new RpcInvokeException("The server side[hostName= "
					+ hostName + ",port=" + port + "] has nothing to response!There are may be something wrong!");
	}
	
	public void disconnect() throws IOException
	{
		this.socket.close();
	}
	
}
