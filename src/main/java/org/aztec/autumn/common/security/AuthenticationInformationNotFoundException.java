package org.aztec.autumn.common.security;

import org.aztec.autumn.common.security.SecurityException;

public class AuthenticationInformationNotFoundException extends
		SecurityException {

	public AuthenticationInformationNotFoundException() {
	}

	public AuthenticationInformationNotFoundException(String message) {
		super(message);
	}

	public AuthenticationInformationNotFoundException(Throwable cause) {
		super(cause);
	}

	public AuthenticationInformationNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
