package org.aztec.autumn.common.utils.persistence.hibernate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

import org.apache.commons.lang.StringUtils;
import org.aztec.autumn.common.CommonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate的会话工厂，主要用于生成实体管理器
 * 
 * @author 黎明
 *
 */
public class HiberateSessionFactory {

	public static final String PERSIST_UNIT_NAME = "com.baitian.cp26server";
	private static EntityManagerFactory factory;
	private static ThreadLocal<EntityManager> singleton = new ThreadLocal<EntityManager>();
	private static final Logger LOG = LoggerFactory.getLogger(HiberateSessionFactory.class);

	static{
		try {
			CommonConfig config = new CommonConfig();
			Map<String,Object> dbProps = new HashMap<>();
			InputStream is = null;
			if(!StringUtils.isBlank(config.getConfigPrefix())
					&& config.getConfigPrefix().equals("file")){
				File dbFile = new File(config.getConfigPath() + "db.properties");
				is = new FileInputStream(dbFile);
			}
			else{
				is = HiberateSessionFactory.class.getResourceAsStream("/db.properties");
			}
			Properties props = new Properties();
			props.load(is);
			for(String propName : props.stringPropertyNames()){
				LOG.info("setting jpa properties:" + propName + "--" + props.getProperty(propName));
				dbProps.put(propName, props.get(propName));
			}
			factory = Persistence.createEntityManagerFactory(PERSIST_UNIT_NAME, dbProps);
		} catch (Exception e) {
			LOG.error("[HiberateSessionFactory] 初始化异常,可能是配置文件有误",e);
		}
	}
	
	public HiberateSessionFactory() {
	}

	/**
	 * 获取实体管理器
	 * 
	 * @return
	 */
	public static EntityManager getEntityManager() {
		/*if(singleton.get() == null)
			singleton.set(factory.createEntityManager());*/
		EntityManager manager = factory.createEntityManager();
		manager.setFlushMode(FlushModeType.COMMIT);
		return manager;
		//return singleton.get();
	}
	
	public static synchronized EntityManager reconnect(){
		return getEntityManager();
		/*synchronized (singleton) {
			if(singleton.get() != null ){
				if(singleton.get().isOpen())
					singleton.get().close();
			}
			//EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSIST_UNIT_NAME);
			singleton.set(factory.createEntityManager());
			singleton.get().setFlushMode(FlushModeType.COMMIT);
		}
		return singleton.get();*/
	}

	public static void main(String[] args) {
		System.out.println(getEntityManager());
	}
}
