package org.aztec.autumn.common.zk;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.aztec.autumn.common.zk.DataMonitor.DataMonitorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseWatcher implements Watcher, Runnable {
	

	//private static List<DataMonitorListener> listeners = new ArrayList<>(); 

	private static ZooKeeper zk;
	
	private static BaseWatcher singleton = new BaseWatcher();
	
	private static ChainedWatcher otherWatcher;
	
	private static Object lockObj = new Object();
	
	private static final Logger LOG  = LoggerFactory.getLogger(BaseWatcher.class);

	static {
		try {
			regist(ZkConnector.getKeeper());
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
		}
	}

	private BaseWatcher() {
	}
	
	public static void regist(ZooKeeper keeper) {
		zk = keeper;
		zk.register(singleton);
	}
	
	public static void addWatcher(ChainedWatcher watcher) {
		if(otherWatcher == null) {
			synchronized (lockObj) {
				if(otherWatcher == null) {
					otherWatcher = watcher;
					return;
				}else {
					otherWatcher.setNext(watcher);
				}
			}
		}
		else {
			otherWatcher.setNext(watcher);
		}
		
	}
	
	
	public static BaseWatcher getWatcher() {
		return singleton;
	}

	

	/***************************************************************************
	 * We do process any events ourselves, we just need to forward them on.
	 *
	 * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.proto.WatcherEvent)
	 */
	public void process(WatchedEvent event) {
		
		ChainedWatcher thisWatcher = otherWatcher;
		while(thisWatcher != null) {
			thisWatcher.process(event);
			thisWatcher = thisWatcher.next();
		}
	}

	public void run() {
		/*try {
			synchronized (this) {
				while (!dm.dead) {
					wait();
				}
			}
		} catch (InterruptedException e) {
		}*/
	}


}