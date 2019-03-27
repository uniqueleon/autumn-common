package org.aztec.autumn.common.utils.net.rpc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.aztec.autumn.common.GlobalConfig;
import org.aztec.autumn.common.utils.ConcurrentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcServer extends Thread {
	private final ServerSocket serverSocket;
	private final ExecutorService pool;
	private RequestInfoChecker checker = new RequestInfoChecker();
	private boolean isRunnable = true;
	private Logger logger = LoggerFactory.getLogger(RpcServer.class);

	public RpcServer(int port, int poolSize) throws IOException {
		serverSocket = new ServerSocket(port);
		//pool = Executors.newWorkStealingPool();
		pool = ConcurrentUtils.createExecutorService(GlobalConfig.getInstance().getRpcThreadPoolSize());
	}
	

	public Integer getAcceptNumer() {
		return checker.handlers.size();
	}

	public void stopRpcServer() {
		logger.warn("Rpc server begin to stop!please wait a moment for the fully stop!");
		this.isRunnable = false;
		this.interrupt();
	}

	public void run() { // run the service
		logger.info( "RPC Server has started, listening port:"
				+ serverSocket.getLocalPort());
		try {
			//checker.start();
			while (isRunnable) {
				RpcServiceHandler handler = new RpcServiceHandler(
						serverSocket.accept());
				//checker.addHandler(handler);
				pool.execute(handler);
			}
		} catch (IOException ex) {
			pool.shutdown();
		}
		finally{
			//checker.stopChecker();
		}
	}

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

	class RequestInfoChecker extends Thread {

		public List<RpcServiceHandler> handlers = new ArrayList();
		private boolean isRunnable = true;

		@Override
		public void run() {
			while (isRunnable) {
				try {
					//logger.info("Checking finished request!");
					while (handlers.size() > 0) {
						RpcServiceHandler handler = handlers.get(0);
						if (handler.isFinished()) {
							//logger.info("remove the finished thread!");
							handlers.remove(0);
						}
					}
					this.sleep(30000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public void addHandler(RpcServiceHandler handler) {
			handlers.add(handler);
		}

		public void stopChecker() {
			this.interrupt();
			this.isRunnable = false;
		}

	}

}
