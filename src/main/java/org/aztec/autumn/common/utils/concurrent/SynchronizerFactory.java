package org.aztec.autumn.common.utils.concurrent;

import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.autumn.common.utils.concurrent.impl.InMemoryForest;
import org.aztec.autumn.common.utils.concurrent.impl.InMemorySynchronizer;
import org.aztec.autumn.common.utils.concurrent.impl.RedisForest;

public class SynchronizerFactory {

	public static NoLockDataSynchronizer getSynchronizer(String name,Object... params) {
		switch(name) {
		case SynchronizerNames.MEMORY:
			return new InMemorySynchronizer(new InMemoryForest());
		case SynchronizerNames.DISTRIBUTED:
			return new InMemorySynchronizer(new RedisForest(UtilsFactory.getInstance().getDefaultCacheUtils()));
		default :
			return new InMemorySynchronizer(new InMemoryForest());
		}
	}
	
	public static interface SynchronizerNames{
		public static final String MEMORY = "MEMORY";
		public static final String DISTRIBUTED = "DISTRIBUTED";
	}
}
