package org.aztec.autumn.common.utils;

import java.io.InputStream;

import org.aztec.autumn.common.CommonConfig;
import org.aztec.autumn.common.utils.cache.RedisUtil;
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

public class UtilsFactory {
	
	private static UtilsFactory singleton = new UtilsFactory();
	private static JacksonUtils jsonUtils = new JacksonUtils();
	
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
	
	public CacheUtils getCacheUtils(String cacheServer,Integer port){
		RedisUtil redis = new RedisUtil(cacheServer, port);
		redis.connect();
		return redis;
	}
	
	public CacheUtils getCacheUtils(String cacheServer,Integer port,String password){
		RedisUtil redis = new RedisUtil(cacheServer, port);
		redis.setPassword(password);
		redis.connect();
		return redis;
	}
	
	public CacheUtils getDefaultCacheUtils(){
		CommonConfig config = new CommonConfig();
		String[] cacheServer = config.getRedisHost().split(",");
		String[] cachePortArr = config.getRedisPort().split(",");
		Integer[] ports = new Integer[cachePortArr.length];
		for(int i = 0;i < cachePortArr.length;i++){
			ports[i] = Integer.parseInt(cachePortArr[i]);
		}
		return getCacheUtils(cacheServer, ports);
	}
	
	public CacheUtils getCacheUtils(String[] cacheServers,Integer[] ports){
		return new RedisUtil(cacheServers, ports);
	}
	
	public CacheUtils getCacheUtils(String[] cacheServers,Integer[] ports,String password){
		RedisUtil redis = new RedisUtil(cacheServers, ports);
		redis.setPassword(password);
		redis.connect();
		return redis;
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
