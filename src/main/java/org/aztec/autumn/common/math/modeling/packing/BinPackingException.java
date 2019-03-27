package org.aztec.autumn.common.math.modeling.packing;

public class BinPackingException extends Exception {
	
	private int errorCode;
	
	public static interface ErrorCodes{
		public static final int FILL_OBJECT_TO_HIGH = 0;
		public static final int PATH_HAS_BEEN_SEARCH_BEFORE = 1;
	}

	public BinPackingException() {
		// TODO Auto-generated constructor stub
	}

	public BinPackingException(String message,int errorCode) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BinPackingException(Throwable cause,int errorCode) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public BinPackingException(String message, Throwable cause,int errorCode) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	

}
