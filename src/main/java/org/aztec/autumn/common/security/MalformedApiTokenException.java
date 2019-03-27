package org.aztec.autumn.common.security;

import org.aztec.autumn.common.security.SecurityException;

public class MalformedApiTokenException extends SecurityException {

	public MalformedApiTokenException() {
		// TODO Auto-generated constructor stub
	}

	public MalformedApiTokenException(String message) {
		super(message);
	}

	public MalformedApiTokenException(Throwable cause) {
		super(cause);
	}

	public MalformedApiTokenException(String message, Throwable cause) {
		super(message, cause);
	}

}
