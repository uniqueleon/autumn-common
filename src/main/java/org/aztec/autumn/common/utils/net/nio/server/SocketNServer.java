package org.aztec.autumn.common.utils.net.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import org.aztec.autumn.common.GlobalConfig;
import org.aztec.autumn.common.utils.ClassScanUtils;
import org.aztec.autumn.common.utils.ClassScanUtils.ClassSelectStrategy;
import org.aztec.autumn.common.utils.net.SocketHandler;
import org.aztec.autumn.common.utils.net.SocketHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketNServer extends Thread {

	private ServerSocketChannel channel;
	private Selector selector;
	private List<SocketHandler> handlers = new ArrayList<SocketHandler>();
	private static final Logger LOG = LoggerFactory.getLogger(SocketNServer.class);

	public SocketNServer(int port) throws IOException {
		// TODO Auto-generated constructor stub
		this.setName("SocketNServer-Thread");
		channel = ServerSocketChannel.open();
		//channel.configureBlocking(true);
		channel.socket().bind(new InetSocketAddress(port));
		channel.configureBlocking(false);

		selector = Selector.open();
		channel.register(selector, SelectionKey.OP_ACCEPT);
		
	}
	
	public void setHandlers(String[] handlePacks) throws ClassNotFoundException, InstantiationException, IllegalAccessException, URISyntaxException, IOException{
		if(handlePacks == null || handlePacks.length == 0)
			return ;
		for(String handlePack : handlePacks){
			handlers.addAll(ClassScanUtils.newInstancesAsList(handlePacks, SocketHandler.class, false,ClassSelectStrategy.INTERFACE));
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				try {
					acceptNewClient();
					for (SocketHandler handler : handlers) {
						handler.handle(selector);
					}
					Thread.currentThread().sleep(GlobalConfig.getInstance().getSocketServerSleepInterval());
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error(e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void acceptNewClient(){
		try {
			SocketChannel clientChannel = channel.accept();
			if (clientChannel != null) {
				LOG.info("New client accepted!");
				clientChannel.configureBlocking(false);
				//clientChannel.configureBlocking(true);
				SelectionKey key = clientChannel.register(selector,
						SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			}
		} catch (ClosedChannelException cce) {
			//LOG.warn(cce.getMessage());
		} catch (Exception e){
			LOG.warn(e.getMessage());
		}
	}

}
