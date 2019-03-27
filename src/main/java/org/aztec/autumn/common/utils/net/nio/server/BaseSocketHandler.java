package org.aztec.autumn.common.utils.net.nio.server;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.aztec.autumn.common.GlobalConfig;
import org.aztec.autumn.common.utils.ConcurrentUtils;
import org.aztec.autumn.common.utils.net.SocketHandler;
import org.aztec.autumn.common.utils.net.exception.HandleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseSocketHandler implements SocketHandler{
	

	private static final Logger LOG = LoggerFactory.getLogger(BaseSocketHandler.class);
	private boolean isFinished = false;
	protected static ExecutorService pool = ConcurrentUtils.createExecutorService(GlobalConfig.getInstance().getRpcThreadPoolSize());
	
	@Override
	public void handle(Selector selector) throws HandleException {
		try {
			selector.select();
			Set<SelectionKey> selectKeys = selector.selectedKeys();
			
			for (SelectionKey key : selectKeys) {
				if (key.isAcceptable()) {
					handleAccept(key);
				} else if (key.isReadable()) {
					handleRead(key);
				} else if (key.isWritable()) {
					handleWrite(key);
				} else if (key.isConnectable()) {
					handleConnect(key);
				}
			}
			isFinished = true;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new HandleException(e.getMessage(), e);
		}
	}
	
	

	protected abstract void handleAccept(SelectionKey Key) throws Exception;
	protected abstract void handleRead(SelectionKey Key) throws Exception;
	protected abstract void handleWrite(SelectionKey Key) throws Exception;
	protected abstract void handleConnect(SelectionKey Key) throws Exception;
	
	void shutdownAndAwaitTermination() {
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}
}
