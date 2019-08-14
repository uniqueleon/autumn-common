package org.aztec.autumn.common.utils;

import java.io.InputStream;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.aztec.autumn.common.utils.cache.RedisConnectionConfig;
import org.aztec.autumn.common.utils.cache.RedisUtil;
import org.aztec.autumn.common.utils.cache.RedisUtilFactory;
import org.aztec.autumn.common.utils.code.QRcodeUtils;
import org.aztec.autumn.common.utils.code.ZxingCodeUtil;
import org.aztec.autumn.common.utils.jms.IMessengerFactory;
import org.aztec.autumn.common.utils.jms.MessengerFactory;
import org.aztec.autumn.common.utils.job.SchedulerFactory;
import org.aztec.autumn.common.utils.job.jms.JobSchedulerFactory;
import org.aztec.autumn.common.utils.json.JacksonUtils;
import org.aztec.autumn.common.utils.mail.EMailServiceFactory;
import org.aztec.autumn.common.utils.mail.MailServiceFactoryImpl;
import org.aztec.autumn.common.utils.xml.XMLReader;
import org.aztec.autumn.common.utils.xml.XMLReaderFactory;
import org.aztec.autumn.common.utils.xml.XMLReaderFactoryImpl;
import org.aztec.autumn.common.utils.xml.XMLUtils;
import org.aztec.autumn.common.utils.xml.XMLUtilsImpl;

import com.google.common.collect.Maps;

public class UtilsFactory {
	
	private static UtilsFactory singleton = new UtilsFactory();
	private static JacksonUtils jsonUtils = new JacksonUtils();
	private Map<String,GenericObjectPool<RedisUtil>> cacheUtilsPool = Maps.newConcurrentMap();
	private static RedisConnectionConfig config;
	private static final Object redisConfigInitLock = new Object();
	
	private UtilsFactory(){
	}
	
	public static UtilsFactory getInstance(){
		return singleton;
	}

	public JsonUtils getJsonUtils(){
		return jsonUtils;
	}
	
	public ReflectionUtil getReflectUtil(){
		return new ReflectionUtilImpl();
	}
	
	public QRcodeUtils getBarcodeUtils(){
		return new ZxingCodeUtil();
	}
	
	private String getCacheUtilKey(String[] hosts,Integer[] port) {
		StringBuilder builder = new StringBuilder();
		for(String host : hosts) {
			builder.append(host + ":" + port + "_");
		}
		return builder.toString();
	}
	
	private CacheUtils getCacheUtils(String[] hosts,Integer[] port,String password) {
		String cacheKey = RedisUtilFactory.getPoolKey(hosts, port);
		if(!cacheUtilsPool.containsKey(cacheKey)) {
			synchronized (cacheUtilsPool) {
				if(!cacheUtilsPool.containsKey(cacheKey)) {
					
					cacheUtilsPool.put(cacheKey, new GenericObjectPool<>(new RedisUtilFactory(hosts, port, password)));
				}
			}
		}
		try {
			return cacheUtilsPool.get(cacheKey).borrowObject();
		} catch (Exception e) {
			return null;
		}
	}
	
	public CacheUtils getCacheUtils(String cacheServer,Integer port){
		return getCacheUtils(new String[] {cacheServer}, new Integer[] {port}, null);
	}
	
	public CacheUtils getCacheUtils(String cacheServer,Integer port,String password){
		return getCacheUtils(new String[] {cacheServer}, new Integer[] {port}, password);
	}
	
	public CacheUtils getDefaultCacheUtils() throws Exception{
		if(config == null) {
			synchronized (redisConfigInitLock) {
				if(config == null) {
					config = new RedisConnectionConfig();
				}
			}
		}
		String[] cacheServer = config.getHosts().split(",");
		String[] cachePortArr = config.getPorts().split(",");
		Integer[] ports = new Integer[cachePortArr.length];
		for(int i = 0;i < cachePortArr.length;i++){
			ports[i] = Integer.parseInt(cachePortArr[i]);
		}
		if(config.getPassword() != null) {
			return getCacheUtils(cacheServer, ports,config.getPassword());
		}
		else {
			return getCacheUtils(cacheServer, ports);
		}
	}
	
	public void releaseCacheUtils(CacheUtils util) {
		if(cacheUtilsPool.containsKey(util.getPoolKey())) {
			cacheUtilsPool.get(util.getPoolKey()).returnObject((RedisUtil)util);
		}
	}
	
	public CacheUtils getCacheUtils(String[] cacheServers,Integer[] ports){
		return getCacheUtils(cacheServers, ports, null);
	}
	
	
	public CacheUtils getCacheUtils(String cacheServer,String ports){
		if(!cacheServer.contains(",")){
			return getCacheUtils(cacheServer, Integer.parseInt(ports));
		}
		else{
			String[] cacheServers = cacheServer.split(",");
			String[] portStrs = ports.split(",");
			Integer[] portArr = new Integer[portStrs.length];
			for(int i = 0;i < portStrs.length;i++){
				portArr[i] = Integer.parseInt(portStrs[i]);
			}
			return getCacheUtils(cacheServers, portArr);
		}
	}
	
	public XMLReader getReader(byte[] xmlContent) throws Exception{
		
		XMLReaderFactory factory = new XMLReaderFactoryImpl();
		return factory.getReader(xmlContent);
	}
	
	public XMLReader getReader(InputStream in) throws Exception{
		XMLReaderFactory factory = new XMLReaderFactoryImpl();
		return factory.getReader(in);
	}
	
	public EMailServiceFactory getEmailServiceFactory(){
		return new MailServiceFactoryImpl();
	}
	
	public SchedulerFactory getScedulerFactory(){
		return new JobSchedulerFactory();
	}
	
	public TimestampManager getTimestampManager(){
		return new SimpleTimestampManager();
	}
	
	public XMLUtils getXMLUtils() {
		return new XMLUtilsImpl();
	}
	
	public IMessengerFactory getJMSMessengerFactory(){
		return new MessengerFactory();
	}
	
}
