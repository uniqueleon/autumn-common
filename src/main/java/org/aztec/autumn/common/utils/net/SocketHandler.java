package org.aztec.autumn.common.utils.net;

import java.nio.channels.Selector;

import org.aztec.autumn.common.utils.net.exception.HandleException;

public interface SocketHandler {

	public void handle(Selector selector) throws HandleException;
	public boolean isFinished() throws HandleException;
}
