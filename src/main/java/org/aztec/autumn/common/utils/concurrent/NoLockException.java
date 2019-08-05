package org.aztec.autumn.common.utils.concurrent;

public class NoLockException extends Exception {
	
	
	private int errorCode;
	
	
	public static interface ErrorCodes {
		public static final int DUPLICATED_VERSION = 1;
		public static final int ROOT_HAS_BEEN_SET = 2;
		public static final int DATA_EXPIRED = 3;
		public static final int PARENT_NOT_EXISTS = 4;
		public static final int DATA_HAS_NOT_SYNCHRONIZED = 5;
		public static final int UNKONW_ERROR = -1;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public NoLockException(int errorCode) {
		super("ERROR:" + errorCode);
		this.errorCode = errorCode;
	}
	
	public NoLockException(int errorCode,Throwable t) {
		super("ERROR:" + errorCode,t);
		this.errorCode = errorCode;
	}

	public NoLockException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NoLockException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public NoLockException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NoLockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
