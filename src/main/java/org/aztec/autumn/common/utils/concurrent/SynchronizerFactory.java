package org.aztec.autumn.common.utils.concurrent;

import org.aztec.autumn.common.utils.concurrent.impl.InMemoryNodeFactory;
import org.aztec.autumn.common.utils.concurrent.impl.InMemorySynchronizer;
import org.aztec.autumn.common.utils.concurrent.impl.InMemoryForest;

public class SynchronizerFactory {

	public static NoLockDataSynchronizer getSynchronizer(String name,Object... params) {
		switch(name) {
		case SynchronizerNames.MEMORY:
			return new InMemorySynchronizer(new InMemoryForest(), new InMemoryNodeFactory());
		default :
			return new InMemorySynchronizer(new InMemoryForest(), new InMemoryNodeFactory());
		}
	}
	
	public static interface SynchronizerNames{
		public static final String MEMORY = "MEMORY";
		public static final String DISTRIBUTED = "DISTRIBUTED";
	}
}
