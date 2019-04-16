package org.aztec.autumn.common.zk;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


public class CallableWatcher implements ChainedWatcher {
	
	//防止起太多线程,导致OOM
	public ExecutorService services = Executors.newFixedThreadPool(1);
	private List<TimeLimitedCallable> callbacks = Lists.newArrayList();
	ChainedWatcher nextWatcher;
	private static final Logger LOG = LoggerFactory.getLogger(CallableWatcher.class);

	public CallableWatcher(List<TimeLimitedCallable> callbacks,ChainedWatcher next) {
		this.callbacks = callbacks;
		this.nextWatcher = next;
	}

	@Override
	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub
		if(!CollectionUtils.isEmpty(callbacks)) {
			for(TimeLimitedCallable callback : callbacks) {
				try {
					Future future = services.submit(callback);
					future.get(callback.getTime(), callback.getUnit());
				} catch (Exception e) {
					LOG.warn(e.getMessage(),e);
				}
			}
		}
	}

	@Override
	public ChainedWatcher next() {
		// TODO Auto-generated method stub
		return nextWatcher;
	}

	@Override
	public ChainedWatcher setNext(ChainedWatcher nextWatcher) {
		this.nextWatcher = nextWatcher;
		return nextWatcher;
	}

}
