package org.aztec.autumn.common.zk;

import org.apache.zookeeper.Watcher;

public interface PathWatcher extends Watcher {

	public String getPath();
}
