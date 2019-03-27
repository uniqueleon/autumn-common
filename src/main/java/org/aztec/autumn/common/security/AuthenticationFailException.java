package org.aztec.autumn.common.security;

import org.aztec.autumn.common.security.SecurityException;

public class AuthenticationFailException extends SecurityException {

	public AuthenticationFailException() {
	}

	public AuthenticationFailException(String message) {
		super(message);
	}

	public AuthenticationFailException(Throwable cause) {
		super(cause);
	}

	public AuthenticationFailException(String message, Throwable cause) {
		super(message, cause);
	}

}
