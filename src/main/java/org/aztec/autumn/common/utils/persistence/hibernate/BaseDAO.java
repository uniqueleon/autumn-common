package org.aztec.autumn.common.utils.persistence.hibernate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.aztec.autumn.common.GlobalConst;
import org.aztec.autumn.common.utils.CollectionUtils;
import org.aztec.autumn.common.utils.ReflectionUtil;
import org.aztec.autumn.common.utils.StringUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.autumn.common.utils.persistence.IBaseDAO;
import org.aztec.autumn.common.utils.persistence.Paginator;
import org.aztec.autumn.common.utils.persistence.PersistenceBeanFactory;
import org.aztec.autumn.common.utils.persistence.PersistenceEvent;
import org.aztec.autumn.common.utils.persistence.PersistenceEventListener;
import org.aztec.autumn.common.utils.persistence.sync.PersistenceSynchronizer;
import org.hibernate.mapping.Collection;
import org.slf4j.Logger;

/**
 * 閸╄櫣顢匘AO鐎电钖�
 * 
 * @author 姒涘孩妲�
 *
 * @param <T>
 */
public abstract class BaseDAO<T> implements IBaseDAO<T> {

	private static final String daoID = UUID.randomUUID().toString();
	// private static String daoID;
	private static Map<String, EntityManager> managerInUsed = new ConcurrentHashMap<>();
	private Class<T> entityCls;
	private String tableName;
	private PersistenceBeanFactory beanFactory = PersistenceBeanFactory.getInstance();
	/*
	 * private static ThreadLocal<EntityTransaction> transaction = new
	 * ThreadLocal<EntityTransaction>();
	 */
	private EntityTransaction thisTransaction;
	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(BaseDAO.class);

	private static ConnectionKeeper keeper;
	private static DbEventListener listener = new DbEventListener();
	private static boolean cacheEnabled = false;
	private static ReentrantLock busiLock = new ReentrantLock();

	static {
		keeper = new ConnectionKeeper();
		//keeper.setDaemon(true);
		boolean deamonStatus = true;
		keeper.setDaemon(deamonStatus);
		listener.setDaemon(deamonStatus);
		keeper.start();
		listener.start();
	}
	
	private static String getCurrentThreadKey(){
		Thread currentThread = Thread.currentThread();
		return currentThread.getName() + "_" + currentThread.getId();
	}
	

	/**
	 * 閺嬪嫰锟界姴鍤遍弫甯礉闂囷拷娴肩姴鍙嗗▔娑樼�风�圭偘缍嬬猾姹囷拷锟�
	 * 
	 * @param entityCls
	 */
	public BaseDAO(Class<T> entityCls) {
		// TODO Auto-generated constructor stub
		this.entityCls = entityCls;
		tableName = findTableName();
		// daoID = UUID.randomUUID().toString();
		// synchronized (managerInUsed) {
		EntityManager manager = null;
		try {
			manager = HiberateSessionFactory.getEntityManager();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!managerInUsed.containsKey(daoID)) {
			managerInUsed.put(daoID, manager);
		}
		// }

	}

	@Override
	protected void finalize() throws Throwable {
		// keeper.stopKeeper();
		// EntityManager manager = managerInUsed.get(daoID);
		// manager.close();
		// managerInUsed.remove(manager);
		releaseLock();
		super.finalize();
	}

	/**
	 * 娣囨繂鐡ㄧ�圭偘缍嬮敍宀冾嚉閺傝纭剁�甸�涚艾閸楁洑閲淒AO鐎电钖勯弶銉嚛閿涘瞼鍤庣粙瀣暔閸忥拷
	 * 
	 * @param entity
	 *            鐎圭偘缍�
	 * @param flush
	 *            閺勵垰鎯佹す顑跨瑐閸掑嘲鍟撻崚鐗堟殶閹诡喖绨遍敍灞筋洤閺嬫粈绗夐崚宄板晸閸掔増鏆熼幑顔肩氨閿涘苯鍨崣顖欎簰閻€劋绨ㄩ崝鈩冩降鏉╂稖顢戠粻锛勬倞
	 * @throws com.baiao.cp26.common.persistence.PersistenceException
	 */
	private void save(T entity, boolean flush) throws PersistenceException {
		try {
			EntityManager manager = getUsableManager();
			// synchronized (manager) {
			T saveTarget = null;
			try {
				saveTarget = attach(entity);
			} catch (Exception e1) {
				// LOG.warn(e1.getMessage());
			}
			if (flush) {
				beginTransaction();
			} else {
				manager.joinTransaction();
			}
			try {
				if (manager.contains(entity)) {
					manager.merge(entity);
				} else if (saveTarget != null) {
					manager.merge(saveTarget);
				} else {
					manager.persist(entity);
					// manager.refresh(entity);
				}
				if (flush) {
					//manager.flush();
					commit();

					// manager.refresh(entity);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(),e);
				boolean retry = retrySave(3, entity, flush);
				if (!retry && flush) {
					if(flush){
						rollback();
					}
					throw new PersistenceException(e.getMessage(), e);
				}
			}
			// }
		} catch (Throwable e) {
			throw e;
		} finally {
			releaseManager();
		}
	}
	
	private boolean retrySave(int retryTime,T entity, boolean flush) throws PersistenceException {
		try {
			if(retryTime == 0){
				return false;
			}
			save(entity,flush);
			return true;
		} catch (Exception e) {
			retryTime --;
			refresh();
			return retrySave(retryTime, entity, flush);
		}
	}

	/**
	 * 閹跺﹤鍞寸�涙ê顕挒鈥冲煕閸掔増鏆熼幑顔肩氨,鐎甸�涚艾閸楁洑閲淒AO鐎电钖勭痪璺ㄢ柤鐎瑰鍙�
	 */
	private void flush() {
		EntityManager manager = getUsableManager();
		manager.flush();
		releaseLock();
	}

	/**
	 * 閹跺﹦绱︾�涙ɑ鏆熼幑顔界閹哄绱濇担璺ㄦ暏閺佺増宓佹惔鎾舵埂鐎圭偞鏆熼幑顕嗙礉,鐎甸�涚艾閸楁洑閲淒AO鐎电钖勭痪璺ㄢ柤鐎瑰鍙�
	 */
	public void refresh() {
		/*
		 * EntityManager manager = getUsableManager(); try {
		 * 
		 * } catch (Exception e) { throw e; } finally{ releaseManager(); }
		 */
		clearCacheSilence();
	}

	public void refresh(T entitty) {
		EntityManager manager = getUsableManager();
		try {
			manager.refresh(entitty);
		} catch (Exception e) {
			throw e;
		} finally {
			releaseManager();
		}
	}

	public void reconnect() {
		EntityManager manager = getUsableManager();
		try {
			// synchronized (manager) {
			manager.close();
			managerInUsed.remove(daoID);
			manager = HiberateSessionFactory.reconnect();
			// }
		} catch (Exception e) {
			throw e;
		} finally {
			releaseManager();
		}
	}

	public static void clearAll() {
		try {
			PersistenceSynchronizer synchronizer = PersistenceBeanFactory.getInstance().getSynchronizer();
			synchronizer.synchronize();
		} catch (Exception e) {
			LOG.warn(e.getMessage(), e);
		}
	}

	public static synchronized void clearCache(Object... params) throws PersistenceException {
		/*
		 * manager.clear(); Cache cache =
		 * manager.getEntityManagerFactory().getCache(); cache.evictAll();
		 * Session session = manager.unwrap(Session.class); SessionFactoryImpl
		 * sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
		 * 
		 * sessionFactory.getCache().evictCollectionRegions();
		 * sessionFactory.getCache().evictQueryRegions();
		 * 
		 * session.clear();
		 */
		/*
		 * SessionFactoryImplementor implementor =
		 * manager.unwrap(SessionFactoryImplementor.class);
		 * implementor.getCache().evictAllRegions();
		 * implementor.getCache().evictCollectionRegions();
		 * implementor.getQueryCache().clear();
		 * implementor.getQueryPlanCache().cleanup();
		 * implementor.getCache().evictDefaultQueryRegion();
		 * implementor.getCache().evictEntityRegions();
		 */
		// System.out.println("Clear cache begin");
		try {
			while (!busiLock.tryLock())
				Thread.sleep(1000l);
			for (String id : managerInUsed.keySet()) {
				EntityManager manager = managerInUsed.get(id);
				if (manager != null) {
					if (manager.isOpen())
						manager.close();
					manager = HiberateSessionFactory.reconnect();
					managerInUsed.put(id, manager);
				} else {
					managerInUsed.put(id, HiberateSessionFactory.getEntityManager());
				}
				// manager.
			}
			// }
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			releaseLock();
		}
		// System.out.println("Clear cache finished!");
		// session.reconnect(manager.unwrap(Connection.class));
		// session = sessionFactory.openSession();

	}

	private boolean isInTransaction() {
		EntityManager manager = getUsableManager();
		EntityTransaction thisTransaction = manager.getTransaction();
		if (thisTransaction != null && thisTransaction.isActive()) {
			return true;
		}
		return false;
	}

	public static synchronized void clearCacheSilence() {
		clearCache(false);
	}

	private List<T> doFinal(List<T> queryList) {
		/*
		 * if(!GlobalConfig.isDBCacheEnabled()){
		 * manager.getEntityManagerFactory().getCache().evictAll(); for(Object
		 * qryObj : queryList){ manager.refresh(qryObj);
		 * 
		 * } }
		 */
		releaseManager();
		return queryList;
	}

	public void release() {
		releaseManager();
	}

	private void releaseManager() {
		if (!isInTransaction()) {
			releaseLock();
		}
	}

	public static void releaseLock() {

		while (busiLock.isHeldByCurrentThread()) {
			// System.out.println("release lock!");
			//LOG.info("Thread " + Thread.currentThread().getName() + " release the lock!");
			busiLock.unlock();
		}
		//managerInUsed.remove(getCurrentThreadKey());
	}

	private static void acquiredLock() {
		if (!busiLock.isHeldByCurrentThread()) {
			int tryTime = 10 * 60 * 100;
			while (!busiLock.tryLock()) {
				try {
					Thread.currentThread().sleep(1);
					tryTime--;
					if (tryTime < 0) {
						// LOG.warn("There are may be dead lock in the threads
						// group!Just to renew the manager and lock!");
						throw new PersistenceException(
								"There are may be dead lock in the threads group!Just to renew the manager and lock!");
					}
				} catch (InterruptedException e) {
					LOG.warn(e.getMessage(), e);
				}
			}
			//LOG.info("Thread" + Thread.currentThread().getName() + " quried the lock!");
		}
	}

	private EntityManager getUsableManager() {
		/*
		 * EntityManager manager = HiberateSessionFactory.getEntityManager();
		 * Thread.currentThread().get
		 */
		/*String threadKey = getCurrentThreadKey();
		EntityManager manager = managerInUsed.get(threadKey);
		if(manager == null){
			manager = HiberateSessionFactory.getEntityManager();
			managerInUsed.put(getCurrentThreadKey() , manager);
		}*/
		acquiredLock();
		//EntityManager manager = HiberateSessionFactory.getEntityManager();
		EntityManager manager = managerInUsed.get(daoID);
		if (manager == null) {
			manager = HiberateSessionFactory.getEntityManager();
			managerInUsed.put(daoID, manager);
		}
		if (!manager.isOpen()) {
			manager = HiberateSessionFactory.getEntityManager();
			managerInUsed.put(daoID, manager);
		}
		return manager;
	}

	/**
	 * 閺屻儲澹橀幍锟介張澶屾畱鐎圭偘缍嬮弫鐗堝祦.
	 * 
	 * @return
	 */
	public List<T> findAll() {
		EntityManager manager = getUsableManager();
		Query query = manager.createQuery("from " + tableName);
		query.setHint("org.hibernate.cacheable", true);
		return doFinal(query.getResultList());
	}

	public List<T> queryByCondition(String condition) {
		EntityManager manager = getUsableManager();
		Query query = manager.createQuery("from " + tableName + " " + condition);
		query.setHint("org.hibernate.cacheable", true);
		return doFinal(query.getResultList());
	}

	public List<T> queryByCondition(Map<String, Object> conditionMaps) {
		EntityManager manager = getUsableManager();
		String hql = "from " + tableName + " ";
		int cursor = 1;
		List<Object> qryParams = new ArrayList<>();
		for (String colName : conditionMaps.keySet()) {
			Object paramObj = conditionMaps.get(colName);
			if (paramObj == null)
				continue;
			qryParams.add(paramObj.getClass().isArray() ? CollectionUtils.arrayToList(paramObj) : paramObj);
			String connector = (paramObj.getClass().isArray() || Collection.class.isAssignableFrom(paramObj.getClass()))
					? " in " : "=";
			if (!hql.contains("where"))
				hql += " where ";
			else
				hql += " and ";
			hql += colName + " " + connector + "?" + qryParams.size() + " ";
		}
		Query query = manager.createQuery(hql);
		query.setHint("org.hibernate.cacheable", true);
		for (int i = 1; i <= qryParams.size(); i++) {
			query.setParameter(i, qryParams.get(i - 1));
		}
		return doFinal(query.getResultList());
	}

	public List<T> queryByHql(String hql) {
		EntityManager manager = getUsableManager();
		Query query = manager.createQuery(hql);
		query.setHint("org.hibernate.cacheable", true);
		return doFinal(query.getResultList());
	}

	public Paginator<T> queryNamedQueryByPage(String queryName, Object[] params, int pageNo, int pageSize) {
		EntityManager manager = getUsableManager();
		try {
			Query query = manager.createNamedQuery(queryName);
			query.setHint("org.hibernate.cacheable", true);
			if (params != null) {
				for (int i = 0; i < params.length; i++)
					query.setParameter((i + 1), params[i]);
			}
			return getPaginator(query, pageNo, pageSize);
		} catch (Exception e) {
			throw e;
		} finally {
			releaseManager();
		}
	}

	public List<T> queryBySql(String sql, Object... params) {
		EntityManager manager = getUsableManager();
		try {
			Query query = manager.createNativeQuery(sql);
			query.setHint("org.hibernate.cacheable", true);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					query.setParameter((i + 1), params[i]);
				}
			}
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		} finally {
			releaseManager();
		}
	}

	public List<T> namedQuery(String qryName, Object[] params) {
		EntityManager manager = getUsableManager();
		Query query = manager.createNamedQuery(qryName);
		query.setHint("org.hibernate.cacheable", true);
		if (params != null) {
			for (int i = 0; i < params.length; i++)
				query.setParameter((i + 1), params[i]);
		}
		return doFinal(query.getResultList());
	}

	private String findTableName() {
		// Annotation[] annos = entityCls.getAnnotations();
		// for(Annotation anno : annos){
		// if(anno.annotationType().equals(Table.class)){
		// Table tableAnno = (Table) anno;
		// return tableAnno.name();
		// }
		// }
		return entityCls.getSimpleName();
	}

	/**
	 * 閹靛綊鍣烘穱婵嗙摠閹碉拷閺堝鏆熼幑锟�.鐎甸�涚艾閸楁洑閲淒AO鐎电钖勭痪璺ㄢ柤鐎瑰鍙�
	 * 
	 * @return
	 * @throws com.baiao.cp26.common.persistence.PersistenceException
	 */
	public List<T> batchSave(List<T> entities) throws PersistenceException {
		EntityManager manager = getUsableManager();
		try {
			boolean isFlush = !isInTransaction();
			if (isFlush) {
				beginTransaction();
			}
			for (T entity : entities) {
				save(entity, false);
			}
			if (isFlush) {
				manager.flush();
				commit();
			}
			releaseManager();
			// manager.refresh(arg0);
			return entities;
			// }
		} catch (Exception e) {
			throw e;
		} finally {
			releaseManager();
		}
	}

	@Override
	public void beginTransaction() {
		if (isInTransaction())
			return;
		EntityManager manager = getUsableManager();

		EntityTransaction thisTransaction = manager.getTransaction();
		if (thisTransaction == null) {
		} else if (thisTransaction.isActive()) {

		} else {
			thisTransaction.begin();
		}

	}

	@Override
	public void commit() {
		try {
			EntityTransaction transaction = getUsableManager().getTransaction();
			if (transaction.isActive()) {
				transaction.commit();
			}
		} catch (Exception e) {
			//refresh();
			throw e;
		} finally {
			releaseLock();
		}
	}
	

	@Override
	public void rollback() {
		try {
			EntityTransaction transaction = getUsableManager().getTransaction();
			if (transaction.isActive()) {
				transaction.rollback();
			}
		} catch (Exception e) {
			// LOG.error(e.getMessage(),e);
			//refresh();
			throw e;
		} finally {
			releaseLock();
		}
	}

	@Override
	public List<T> find(Map<String, Object> params) {
		return findByPage(params, 0, GlobalConst.DEFAULT_PAGE_SIZE).getData();
	}

	@Override
	public List<T> find(T sample) {
		return findByPage(sample).getData();
	}

	@Override
	public T save(T entity) throws PersistenceException {
		boolean isFlush = !isInTransaction();
		save(entity, isFlush);
		// clearCacheSilence();
		return entity;
	}

	@Override
	public T delete(T entity, boolean logical, String propName)
			throws PersistenceException {
		EntityManager manager = getUsableManager();
		try {
			T deleteTarget = attach(entity);
			// synchronized (manager) {
			if (logical) {
				try {
					Method setter = entityCls.getDeclaredMethod("set" + StringUtils.capitalize(propName),
							new Class[] { Boolean.class });
					setter.invoke(deleteTarget, new Object[] { new Boolean(false) });
					save(deleteTarget);
				} catch (Exception e) {
					throw new PersistenceException(e.getMessage(), e);
				}
			} else {
				try {
					beginTransaction();
					// manager.remove(deleteTarget);
					Query query = manager.createQuery(
							"DELETE FROM " + entityCls.getName() + " en where en.id=" + getId(deleteTarget));
					if (query.executeUpdate() <= 0) {
						throw new PersistenceException("Delete fail!");
					}
					// manager.flush();
					commit();
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
					rollback();
					throw new PersistenceException(e.getMessage(), e);
				}
			}
			// }
		} catch (Throwable e) {
			throw e;
		} finally {
			releaseManager();
		}
		// clearCacheSilence();
		return entity;
	}

	@Override
	public Paginator<T> findAllByPage(int page, int pageSize) {
		EntityManager manager = getUsableManager();
		try {
			List<T> results = new ArrayList<T>();
			String hql = " from " + tableName;
			Query query = manager.createQuery(hql);
			query.setHint("org.hibernate.cacheable", true);
			return getPaginator(query, page, pageSize);
		} catch (Exception e) {
			throw e;
		} finally {
			releaseManager();
		}
	}

	private Paginator<T> getPaginator(Query query, int page, int pageSize) {

		Paginator<T> paginator = beanFactory.getSimplePaginator(page, pageSize);
		int maxSize = query.getResultList().size();
		paginator.setTotalSize(maxSize);
		paginator.setLastPage(maxSize / pageSize);
		query.setFirstResult(page * pageSize);
		query.setMaxResults(pageSize);
		paginator.setData(doFinal(query.getResultList()));
		return paginator;
	}

	@Override
	public Paginator<T> findByPage(Map<String, Object> params, int page, int pageSize) {
		EntityManager manager = getUsableManager();
		try {
			Paginator<T> paginator = beanFactory.getSimplePaginator(page, pageSize);
			if (params.size() == 0)
				return paginator;
			String hql = " from " + tableName;
			for (String columnName : params.keySet()) {
				if (!hql.endsWith(tableName))
					hql += " and ";
				hql += columnName + "=" + params.get(columnName);
			}
			Query query = manager.createQuery(hql);
			query.setHint("org.hibernate.cacheable", true);
			return getPaginator(query, page, pageSize);
		} catch (Exception e) {
			throw e;
		} finally {
			releaseManager();
		}
	}

	@Override
	public Paginator<T> findByPage(T sample) {
		try {
			ReflectionUtil reflectUtil = UtilsFactory.getInstance().getReflectUtil();
			Map<String, Object> props = reflectUtil.getProperties(sample, true);
			return findByPage(props, 0, GlobalConst.DEFAULT_PAGE_SIZE);
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}

	@Override
	public T findById(Object id) throws PersistenceException {
		EntityManager manager = getUsableManager();
		T entity = manager.find(entityCls, id);
		doFinal(new ArrayList<>());
		return entity;
	}

	private T attach(T targetObj) throws PersistenceException {
		try {
			return findById(getId(targetObj));
		} catch (Exception e) {
			throw new PersistenceException("The target entity is not in db!");
		}
	}

	private Object getId(T targetObj) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = targetObj.getClass().getDeclaredFields();
		for (Field field : fields) {
			Annotation[] annos = field.getAnnotations();
			for (Annotation anno : annos) {
				if (anno.annotationType().equals(Id.class)) {
					field.setAccessible(true);
					return field.get(targetObj);
				}
			}
		}
		return null;
	}

	private static class ConnectionKeeper extends Thread {

		private boolean runnable = true;

		public ConnectionKeeper() {
			super("BaseDAO Connection keeper");
		}

		@Override
		public void run() {
			while (runnable) {
				try {
					LOG.info("Connection keeper begin to keep connnection activate!");
					while (!busiLock.tryLock())
						Thread.sleep(1000l);
					for (String id : managerInUsed.keySet()) {
						try {
							EntityManager manager = managerInUsed.get(id);
							if (manager != null) {
								try {
									if (manager != null && manager.isOpen()) {
										Query query = manager.createNativeQuery("select 1");
										query.getResultList();
									}
								} catch (Exception e) {
									LOG.warn(e.getMessage());
									if (manager.isOpen())
										manager.close();
									manager = HiberateSessionFactory.reconnect();
									managerInUsed.put(id, manager);
								}
							} else {
								managerInUsed.put(id, HiberateSessionFactory.getEntityManager());
							}
						} catch (Exception e) {
							LOG.warn(e.getMessage());
						}
					}
					LOG.info("keep finished!");
				} catch (InterruptedException ie){
					stopKeeper();
				}
				catch (Exception e) {
					stopKeeper();
					LOG.warn(e.getMessage(), e);
				} finally {
					try {
						// Thread.currentThread().sleep(3 * 1000l);
						busiLock.unlock();
						if(runnable){
							Thread.currentThread().sleep(60 * 60 * 1000l);
						}
					} catch (InterruptedException e) {
						stopKeeper();
						LOG.warn(e.getMessage(), e);
					}
				}
			}
		}

		public void stopKeeper() {
			LOG.warn("stop keeper!");
			this.runnable = false;
			this.interrupt();
		}

	}

	private static class DbEventListener extends Thread {
		private boolean runnable = true;
		private PersistenceEventListener listener;

		public DbEventListener() {
			super("BaseDAO Database event listener!");
		}

		@Override
		public void run() {
			while (runnable) {
				try {
					LOG.info("Db synchronizer begin to keep db data synchronized!");
					if(listener == null){
						 listener = PersistenceBeanFactory.getInstance().getEventListener();
					}
					List<PersistenceEvent> events = listener.listen(false);
					for (PersistenceEvent event : events) {
						switch (event.getEventType()) {
						case DB_CACHE_SYNCHORIZED:
							// if
							// (!event.getSource().getSourceID().equals(GlobalConfig.getInstance().getLocalhost()))
							clearCacheSilence();
						default:
							break;
						}
					}
					LOG.info("synchronized finished!");
					Thread.currentThread().sleep(3 * 1000l);
				} catch (InterruptedException e) {
					stopEventListener();
					LOG.warn(e.getMessage(), e);
				} catch (Exception e) {
					stopEventListener();
					LOG.warn(e.getMessage(), e);
				}
			}
		}

		public void stopEventListener() {
			LOG.warn("stop listener!");
			if(listener != null){
				listener.stopListener();
			}
			this.runnable = false;
			this.interrupt();
		}
	}

	public static void setCacheEnabled(boolean enabled) {
		cacheEnabled = enabled;
	}
	
	public static void stopInternalThread(){
		keeper.stopKeeper();
		listener.stopEventListener();
	}
	
	public static void closeDbConnection(){
		while(!busiLock.tryLock()){
			for(EntityManager manager : managerInUsed.values()){
				manager.close();
			}
		}
		busiLock.unlock();
	}

	@Override
	public int executeUpdate(String sql, Object[] params)
			throws PersistenceException {
		try {
			EntityManager em = getUsableManager();
			if (isInTransaction()) {
				em.joinTransaction();
			}
			Query query = em.createNativeQuery(sql);
			for (int i = 0; i < params.length; i++) {
				query.setParameter((i + 1), params[i]);
			}
			return query.executeUpdate();
		} catch (PersistenceException e) {
			throw e;
		} finally {
			releaseManager();
		}
	}

	@Override
	public void detach(T entity) throws PersistenceException {
		// TODO Auto-generated method stub
		try {
			EntityManager em = getUsableManager();
			em.detach(entity);
		} catch (PersistenceException e) {
			throw e;
		} finally {
			releaseManager();
		}
	}

	@Override
	public <E> Paginator<E> executeNativeQuery(String sql, Object[] params, Class<E> entityCls, int pageNo,
			int pageSize) {
		try {
			EntityManager em = getUsableManager();
			Query query = em.createNativeQuery(sql);
			for (int i = 0; i < params.length; i++) {
				query.setParameter((i + 1), params[i]);
			}
			Paginator<E> paginator = beanFactory.getSimplePaginator(pageNo, pageSize);

			int maxSize = query.getResultList().size();
			paginator.setTotalSize(maxSize);
			paginator.setLastPage(maxSize / pageSize);
			query.setFirstResult(pageNo * pageSize);
			query.setMaxResults(pageSize);
			List dataList = query.getResultList();
			if (entityCls.isAssignableFrom(Map.class)) {
				List<Object[]> objArrList = dataList;
				List<String> colNames = getColumnNames(sql);
				List mapDataList = new ArrayList<>();
				for (Object[] data : objArrList) {

					Map<String, Object> retMap = new HashMap<>();
					for (int i = 0; i < data.length; i++) {
						retMap.put(colNames.get(i), data[i]);
					}
					mapDataList.add(retMap);
				}
				paginator.setData(mapDataList);
			} else {
				paginator.setData(dataList);
			}
			return paginator;
		} catch (PersistenceException e) {
			throw e;
		} finally {
			releaseManager();
		}
	}

	private List<String> getColumnNames(String sql) {
		String columnNames = sql.substring(sql.indexOf("select") + 6, sql.indexOf("from")).trim();
		if (columnNames.trim().equals("*") || columnNames.contains(",*")) {
			throw new IllegalArgumentException("column name is missing!");
		}
		String[] columnNameArr = columnNames.split(",");
		List<String> retNames = new ArrayList<>();
		for (String colName : columnNameArr) {
			if (colName.contains("as")) {
				retNames.add(colName.split("as")[1].trim());
			} else {
				retNames.add(colName);
			}
		}
		return retNames;
	}

	@Override
	public List<T> findByIds(List ids) throws PersistenceException {
		// TODO Auto-generated method stub
		EntityManager manager = getUsableManager();
		if (ids != null && ids.size() > 0) {
			String hql = "from " + tableName + " where id in ?1";
			Query query = manager.createQuery(hql);
			query.setHint("org.hibernate.cacheable", true);
			query.setParameter(1, ids);
			List<T> qryResult = query.getResultList();
			qryResult = doFinal(qryResult);
			return qryResult;
		} else {
			releaseManager();
			return new ArrayList<>();
		}
	}

}
