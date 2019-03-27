package org.aztec.autumn.common.utils.net.rpc.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.aztec.autumn.common.utils.ClassScanUtils;
import org.aztec.autumn.common.utils.ReflectionUtil;
import org.aztec.autumn.common.utils.ReflectionUtilImpl;
import org.aztec.autumn.common.utils.ClassScanUtils.ClassSelectStrategy;
import org.aztec.autumn.common.utils.net.rpc.entity.RpcInvokeParam;
import org.aztec.autumn.common.utils.net.rpc.entity.RpcInvokeResult;
import org.aztec.autumn.common.utils.net.rpc.exception.RpcInvokeException;
import org.aztec.autumn.common.utils.persistence.hibernate.BaseDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcServiceHandler implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(RpcServiceHandler.class);
	private Socket clientSocket = new Socket();
	private boolean isFinished = false;
	private final static Logger LOG = LoggerFactory.getLogger(RpcServiceHandler.class);
	private final static List<Socket> garbageSockets = Collections.synchronizedList(new ArrayList<>());
	private final static List<Class> cacheClasses = new ArrayList<>();

	static{
		try {
			Thread gcThread = new Thread(new SocketGarbageCollector());
			gcThread.setName("RPC Server client socket GC");
			gcThread.start();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	public RpcServiceHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public RpcInvokeParam getInvokeParam() throws IOException, InterruptedException, ClassNotFoundException {
		// logger.info("Tring to get request data from client side!");
		ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
		return (RpcInvokeParam) ois.readObject();
	}

	public void sendResult(RpcInvokeResult invResult) throws IOException {
		// logger.info("sending result data to client side!");
		ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
		oos.writeObject(invResult);
		oos.flush();
	}

	public void flush() throws IOException {
		clientSocket.getOutputStream().flush();
	}

	public void close() throws IOException {
		// ss.close();
		clientSocket.close();
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			RpcInvokeParam invParam = getInvokeParam();
			sendResult(invoke(invParam));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if(!clientSocket.isClosed()){
					logger.info("client socket closed!");
					close();
				}
				else{
					garbageSockets.add(clientSocket);
				}
				//
				isFinished = true;
				// logger.info("RPC Invoke successfully complete!");
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public boolean isFinished() {
		return isFinished;
	}

	private RpcInvokeResult invoke(RpcInvokeParam invParam) {
		try {
			Class clazz = Class.forName(invParam.getClassName());
			if (clazz.isInterface()) {
				String defaultPakageName = clazz.getPackage().getName();
				Class cacheClass = findInCache(clazz);
				if(cacheClass != null){
					List<Class> findClasses = new ArrayList<>();
					findClasses.add(cacheClass);
					return invoke(invParam,  getInstantiableClass(findClasses));
				}
				else{
					List implClasses = ClassScanUtils.scan(defaultPakageName.replaceAll("\\.", "/"), clazz, true,ClassSelectStrategy.INTERFACE);
					cacheClasses.addAll(implClasses);
					if (implClasses == null || implClasses.isEmpty() || getInstantiableClass(implClasses) == null) {
						return new RpcInvokeResult(null,
								new RpcInvokeException("No implementation of " + clazz.getName() + " found!"));
					} else {
						return invoke(invParam, getInstantiableClass(implClasses));
					}
				}
			}
			return invoke(invParam, Class.forName(invParam.getClassName()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return new RpcInvokeResult(null, e);
		} finally {
			doCleanUp();
		}
	}
	
	public static Class findInCache(Class targetClass){
		for(Class cacheCls : cacheClasses){
			if(targetClass.isAssignableFrom(cacheCls)
					&& !cacheCls.isInterface()){
				try {
					cacheCls.getConstructor();
					return cacheCls;
				} catch (Exception e) {
					continue;
				}
				
			}
		}
		return null;
	}
	
	private void doCleanUp(){
		logger.info("Rpc thread [" + "] doing clean up!");
		BaseDAO.releaseLock();
		logger.info("Rpc thread [" + "] clean up finished!");
	}
	
	private Class getInstantiableClass(List<Class> implClasses){
		for(Class clazz : implClasses){
			if(Modifier.isAbstract(clazz.getModifiers())){
				continue;
			}
			try {
				clazz.newInstance();
				return clazz;
			} catch (Exception e) {
				logger.warn(e.getMessage(),e);
				continue;
			}
		}
		return null;
	}

	private RpcInvokeResult invoke(RpcInvokeParam invParam, Class invokedClass) {
		RpcInvokeResult invRes;
		try {
			ReflectionUtil reflectionUtil = new ReflectionUtilImpl();
			Object invokeObj = reflectionUtil.newInstance(invokedClass);
			Object resObj = reflectionUtil.invoke(invokeObj, invParam.getMethodName(), invParam.getMethodParamTypes(),
					invParam.getParams(), false);
			invRes = new RpcInvokeResult(resObj, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			invRes = new RpcInvokeResult(null, e);
		}
		return invRes;
	}

	private RpcInvokeResult invoke(RpcInvokeParam invParam, Object invokedObj) {
		RpcInvokeResult invRes;
		try {
			ReflectionUtil reflectionUtil = new ReflectionUtilImpl();
			Object resObj = reflectionUtil.invoke(invokedObj, invParam.getMethodName(), invParam.getMethodParamTypes(),
					invParam.getParams(), false);
			invRes = new RpcInvokeResult(resObj, null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			invRes = new RpcInvokeResult(null, e);
		}
		return invRes;
	}
	
	private static class SocketGarbageCollector implements Runnable{

		private boolean runnable = true;
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(runnable){
				try {
					List<Integer> removeIndex = new ArrayList<>();
					for(int i = 0;i < garbageSockets.size();i++){
						Socket clientSocket = garbageSockets.get(i);
						if(!clientSocket.isClosed()){
							LOG.info("client garbage [" + clientSocket.hashCode() + "] soket closed!");
							clientSocket.close();
							removeIndex.add(i);
						}
					}
					for(int remIndex : removeIndex){
						Socket closedSocket = garbageSockets.get(remIndex);
						garbageSockets.remove(remIndex);
					}
					Thread.sleep(1000);
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
			}
		}
		
	}
}
