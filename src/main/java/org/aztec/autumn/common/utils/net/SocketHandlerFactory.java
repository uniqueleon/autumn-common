package org.aztec.autumn.common.utils.net;

import java.util.List;

public abstract class SocketHandlerFactory {
	
	public static SocketHandlerFactory singleton;

	protected SocketHandlerFactory() {
	}

	public abstract List<SocketHandler> getNHandlers();
	
	
	public static SocketHandlerFactory getInstance(){
		return singleton;
	}
	
	
}
