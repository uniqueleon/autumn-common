package org.aztec.autumn.common.zk;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class BaseWatcher implements Watcher {
	

	//private static List<DataMonitorListener> listeners = new ArrayList<>(); 

	private static ZooKeeper zk;
	
	private static BaseWatcher singleton = new BaseWatcher();
	
	
	private static Map<String,Watcher> watchers = Maps.newConcurrentMap();
	
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
	
	
	public static void addWatcher(PathWatcher watcher) {
		if(watcher.getPath() == null || watcher.getPath().isEmpty()) {

			throw new IllegalArgumentException("The path "  + watcher.getPath() + " is needed!");
		}

		if(watchers.containsKey(watcher.getPath())) {
			throw new IllegalArgumentException("The path "  + watcher.getPath() + " is exists!");
		}
	}
	
	public static void removeWatcher(PathWatcher watcher) {
		watchers.remove(watcher.getPath());
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

		String path = event.getPath();
		if(!StringUtils.isEmpty(path)) {
			Watcher watcher = watchers.get(path);
			if(watcher == null) {
				return;
			}
			watcher.process(event);			
			if(watcher instanceof ChainedWatcher) {
				ChainedWatcher thisWatcher = (ChainedWatcher) watcher;
				ChainedWatcher nextWatcher = thisWatcher.next();
				while(nextWatcher != null) {
					nextWatcher.process(event);
					nextWatcher = nextWatcher.next();
				}
			}
		}
		
	}



}