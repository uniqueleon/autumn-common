package org.aztec.autumn.common.zk;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistributedLock extends ZkNode  implements Lock{
	
	
	protected ThreadLocal<Boolean> interruptable = new ThreadLocal<>();
	protected static final Object lockObject = new Object();
	protected AtomicBoolean lock = new AtomicBoolean(true);
	private static final Logger LOG = LoggerFactory.getLogger(DistributedLock.class);

	public DistributedLock(String dataID) throws IOException, KeeperException, InterruptedException {
		super(dataID);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void notifyChanges() throws Exception {
		// TODO Auto-generated method stub
		lock.set(Boolean.parseBoolean(dataStr));
		if(!lock.get()) {
			lockObject.notifyAll();
		}
	}

	@Override
	public void lock() {
		// TODO Auto-generated method stub
		if(interruptable.get() == null) {
			interruptable.set(false);
		}
		if(!lock.get()) {
			if(lock.compareAndSet(false, true)) {
				try {
					write("" + lock.get());
				} catch (Exception e) {
					lock.set(false);
					LOG.error(e.getMessage(),e);
					block();
				}
			}
			else {
				block();
			}
		}
		else {
			block();
		}
	}
	
	private void block() {
		try {
			lockObject.wait();
		} catch (InterruptedException e1) {
			if(interruptable.get()) {
				return ;
			}else {
				LOG.error(e1.getMessage(),e1);
				throw new RuntimeException(e1.getMessage(),e1);
			}
		}
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		interruptable.set(true);
		lock();
	}

	@Override
	public Condition newCondition() {
		throw new UnsupportedOperationException("Not implemented yet!");
		//return null;
	}

	@Override
	public boolean tryLock() {
		if(!lock.get()) {
			if(lock.compareAndSet(false, true)) {
				try {
					write("" + lock.get());
					return true;
				} catch (Exception e) {
					lock.set(false);
					LOG.error(e.getMessage(),e);
				}
			}
		}
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		long countDown = unit.toMillis(time);
		long sleepInterval = 1l;
		while(countDown > 0 && !lock.get()) {
			if(lock.compareAndSet(false, true)) {
				try {
					write("" + lock.get());
					return true;
				} catch (Exception e) {
					lock.set(false);
					LOG.error(e.getMessage(),e);
				}
			}
			Thread.currentThread().sleep(sleepInterval);
			countDown -= sleepInterval;
		}
		return false;
	}

	@Override
	public void unlock() {
		// TODO Auto-generated method stub
		lock.set(false);
		try {
			write("" + lock.get());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(),e);
		}
	}

}
