package org.aztec.autumn.common.zk;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public interface TimeLimitedCallable<V> extends Callable<V> {

	public Long getTime();
	public TimeUnit getUnit();
}
