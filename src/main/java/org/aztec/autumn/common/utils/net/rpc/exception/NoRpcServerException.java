package org.aztec.autumn.common.utils.net.rpc.exception;

public class NoRpcServerException extends Exception {

	public NoRpcServerException() {
	}

	public NoRpcServerException(String message) {
		super(message);
	}

	public NoRpcServerException(Throwable cause) {
		super(cause);
	}

	public NoRpcServerException(String message, Throwable cause) {
		super(message, cause);
	}

}
