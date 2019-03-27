package org.aztec.autumn.common.utils.persistence;

import javax.jms.JMSException;

import org.aztec.autumn.common.utils.persistence.impl.HostNameSource;
import org.aztec.autumn.common.utils.persistence.impl.SynchronizedEventListener;
import org.aztec.autumn.common.utils.persistence.sync.JMSSynchronizer;
import org.aztec.autumn.common.utils.persistence.sync.PersistenceSynchronizer;

public class PersistenceBeanFactory {

	private static final PersistenceBeanFactory factory = new PersistenceBeanFactory();
	
	private PersistenceBeanFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public static PersistenceBeanFactory getInstance(){
		return factory;
	}
	
	public <T> Paginator<T> getSimplePaginator(){
		return new SimplePaginator<T>();
	}
	
	public <T> Paginator<T> getSimplePaginator(int pageNo,int pageSize){
		return new SimplePaginator<T>(pageNo,pageSize);
	}
	
	public PersistenceEventListener getEventListener() throws JMSException{
		return new SynchronizedEventListener();
	}
	
	public PersistenceSynchronizer getSynchronizer(){
		return new JMSSynchronizer();
	}
	
	public EventSource getSource(String name){
		return new HostNameSource(name);
	}

}
