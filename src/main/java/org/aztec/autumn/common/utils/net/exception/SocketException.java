package org.aztec.autumn.common.utils.net.exception;

public class SocketException extends Exception {

	public SocketException() {
	}

	public SocketException(String message) {
		super(message);
	}

	public SocketException(Throwable cause) {
		super(cause);
	}

	public SocketException(String message, Throwable cause) {
		super(message, cause);
	}

}
