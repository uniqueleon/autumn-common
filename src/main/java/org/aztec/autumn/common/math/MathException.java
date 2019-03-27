package org.aztec.autumn.common.math;

public class MathException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5725814542039580354L;
	private int errorCode;

	public MathException() {
		// TODO Auto-generated constructor stub
	}

	public MathException(int errCode,String arg0) {
		super(arg0);
		this.errorCode = errCode;
		// TODO Auto-generated constructor stub
	}

	public MathException(int errCode,Throwable arg0) {
		super(arg0);
		this.errorCode = errCode;
	}

	public MathException(int errCode,String arg0, Throwable arg1) {
		super(arg0, arg1);
		this.errorCode = errCode;
	}
	
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public static class DiophantineEquationErrorCode{
		public static final int offset = 1000;
		public static final int SOLUATION_PATH_HAS_BEEN_SEARCHED = offset + 1;
		public static final int SOLUTION_DOES_NOT_MATCH = offset + 2;
		public static final int SOLUTION_SERACH_TIME_EXCEED =  offset + 3;
	}
	
	public static class GeneralErrorCode{
		public static final int offset = 0;

		public static final int NO_MODULAR_INVERSOR = offset + 1;
	}
}
