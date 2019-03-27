package org.aztec.autumn.common.security;

import org.aztec.autumn.common.security.SecurityException;

public class ILLegalUserInputException extends SecurityException{

	public ILLegalUserInputException(String msg) {
		super(msg);
	}

	public ILLegalUserInputException(String msg, Throwable t) {
		super(msg, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

}
