package org.aztec.autumn.common.utils;

import java.io.InputStream;
import java.lang.reflect.Proxy;

import org.aztec.autumn.common.utils.cache.OnceAquriedRedis;
import org.aztec.autumn.common.utils.cache.RedisConnectionConfig;
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
	
	public  static CacheUtils getCacheUtils(String cacheServer,Integer port,String password){
		return getCacheUtils(new String[] {cacheServer}, new Integer[] {port}, password);
	}
	
	public static CacheUtils getCacheUtils(String[] hosts,Integer[] port,String password) {
		return (CacheUtils) Proxy.newProxyInstance(UtilsFactory.class.getClassLoader(),
				new Class[] {CacheUtils.class}, new OnceAquriedRedis(hosts, port, password));
	}
	
	public CacheUtils getDefaultCacheUtils(){
		if(config == null) {
			synchronized (redisConfigInitLock) {
				if(config == null) {
					try {
						config = new RedisConnectionConfig();
					} catch (Exception e) {
						return null;
					}
				}
			}
		}
		String[] cacheServer = config.getHosts().split(",");
		String[] cachePortArr = config.getPorts().split(",");
		Integer[] ports = new Integer[cachePortArr.length];
		for(int i = 0;i < cachePortArr.length;i++){
			ports[i] = Integer.parseInt(cachePortArr[i]);
		}
		return getCacheUtils(cacheServer, ports, config.getPassword());
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
