package org.aztec.autumn.common.utils.persistence.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;

import org.aztec.autumn.common.constant.CommonConst;
import org.aztec.autumn.common.utils.jms.DestinationType;
import org.aztec.autumn.common.utils.jms.IMessageSubscriber;
import org.aztec.autumn.common.utils.jms.JmsProxy;
import org.aztec.autumn.common.utils.persistence.PersistenceBeanFactory;
import org.aztec.autumn.common.utils.persistence.PersistenceEvent;
import org.aztec.autumn.common.utils.persistence.PersistenceEventListener;
import org.aztec.autumn.common.utils.persistence.PersistenceEvent.EventType;
import org.aztec.autumn.common.utils.persistence.sync.DbSynchronizedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SynchronizedEventListener implements PersistenceEventListener {

	private static IMessageSubscriber subscriber;
	private static Logger LOG = LoggerFactory.getLogger(SynchronizedEventListener.class);
	private static Future<String> future;
	private static ExecutorService service = Executors.newSingleThreadExecutor(new ThreadFactory() {
		
		@Override
		public Thread newThread(Runnable r) {
			Thread runThread =  new Thread(r);
			runThread.setDaemon(true);
			return runThread;
		}
	});
	//newSingleThreadExecutor(ThreadFactory)

	public SynchronizedEventListener() throws JMSException {
		// TODO Auto-generated constructor stub
		if (subscriber != null){
			if(!subscriber.isStarted())
				subscriber.start();
		}
		else {
			subscriber = JmsProxy.getSubscriber(CommonConst.DB_EVENT_TYPE_TOPIC, DestinationType.TOPIC.name());
			subscriber.start();
		}
	}

	@Override
	public List<PersistenceEvent> listen(final boolean async) throws Exception {
		// TODO Auto-generated method stub
		subscriber.start();
		List<PersistenceEvent> events = new ArrayList<>();
		EventType type = null;
		
		future = service.submit(new Callable<String>() {

			@Override
			public String call() throws Exception {
				String eventType = null;
				do {
					eventType = subscriber.getText();
					Thread.currentThread().sleep(1000);
				} while (eventType == null && !async);
				return eventType;
			}
		});
		String jmsMsg = null;
		if (async) {
			try {
				jmsMsg = future.get(3, TimeUnit.SECONDS);
			} catch (Exception e) {
				future.cancel(true);
			}
		} else {
			try {
				jmsMsg = future.get();
			} catch (ExecutionException e) {
				future.cancel(true);
				LOG.warn(e.getMessage());
			}
		}
		if(jmsMsg == null)
			return new ArrayList<>();
		type = EventType.valueOf(jmsMsg.split("-")[1]);
		if (type != null) {
			switch (type) {
			case DB_CACHE_SYNCHORIZED:
				events.add(new DbSynchronizedEvent(PersistenceBeanFactory.getInstance().getSource(jmsMsg.split("-")[0])));
			default:
				break;
			}
		}
		subscriber.stop();
		return events;
	}

	@Override
	public void stopListener() {
		service.shutdown();
		if(future != null){
			future.cancel(true);
		}
		try {
			subscriber.disconnect();
		} catch (JMSException e) {
			LOG.error(e.getMessage(),e);
		}
	}

}
