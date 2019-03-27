package org.aztec.autumn.common.utils.server.sh;

import java.util.concurrent.locks.ReentrantLock;

import org.aztec.autumn.common.CommonConfig;
import org.aztec.autumn.common.utils.net.rpc.client.RpcProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasedServerStopper implements ServerStopper {

	private static final Logger LOG = LoggerFactory.getLogger(BasedServerStopper.class);
	private static final long SYSTEM_EXIT_COUNTDOWN_INTERVAL = 5000l;
	private static final ReentrantLock rpcLock = new ReentrantLock();
	protected Class<? extends ServerStopper> stopperCls;

	public BasedServerStopper(Class<? extends ServerStopper> stopperCls) {
		// TODO Auto-generated constructor stub
		this.stopperCls = stopperCls;
	}

	public abstract void cleanup() ;
	
	public void doStop(){
		try {
			CommonConfig config = new CommonConfig();
			System.out.println("[Command Server Stopper]:Try to stop command server!");
			
			ServerStopper stopper = RpcProxy.createProxy(stopperCls,
				config.getLocalhost(), config.getRpcPort());
			int countdown = Integer.parseInt("" + SYSTEM_EXIT_COUNTDOWN_INTERVAL / 1000l);
			while(countdown > 0){
				System.out.println("[Command Server Stopper]:Command server will be stop in  " + countdown + " seconds!");
				Thread.currentThread().sleep(1000);
				countdown --;
			}
			stopper.stopAllServer();
			Thread.currentThread().sleep(SYSTEM_EXIT_COUNTDOWN_INTERVAL);
			System.out.println("[Command Server Stopper]:Server has been stopped!");
		} catch (Exception e) {
			System.out.println("Stop fail!There are may be something wrong in server!MSG[" + e.getMessage() + "]!");
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void stopAllServer() {
		// TODO Auto-generated method stub
		try {
			rpcLock.lock();
			Thread stopThread = new Thread(new ServerStopThread());
			stopThread.start();
		} finally {
			rpcLock.unlock();
		}
		return;
	}
	
	public class ServerStopThread implements Runnable{

		@Override
		public void run() {
			try {
				rpcLock.lock();
				cleanup();
				LOG.info("Server has been stopped!");
				System.exit(0);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			} finally {
				rpcLock.unlock();
			}
		}
		
	}
	

}
