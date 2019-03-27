package org.aztec.autumn.common.zk;

import org.apache.zookeeper.Watcher;

public interface ChainedWatcher extends Watcher {

	public ChainedWatcher next();
	public ChainedWatcher setNext(ChainedWatcher nextWatcher);
}
