package org.aztec.autumn.common.utils.compress;

public class CompressException extends Exception {
	
	private int errorCode;

	public CompressException(int errCode) {
		super();
		this.errorCode = errCode;
		// TODO Auto-generated constructor stub
	}

	public CompressException(int errCode,String arg0, Throwable arg1) {
		super(arg0, arg1);
		this.errorCode = errCode;
	}

	public CompressException(int errCode,String arg0) {
		super(arg0);
		this.errorCode = errCode;
	}

	public CompressException(int errCode,Throwable arg0) {
		super(arg0);
		this.errorCode = errCode;
	}

	public static class ErrorCode{
		public static int CODE_LIMIT_EXCEED = 1;
		public static int MAIN_FILE_NOT_FOUND = 2;
		public static int WORK_FILE_NOT_FOUND = 3;
		public static int COUNTING_FAIL = 4;
		public static int RESULT_NOT_FOUND = 5;
		public static int CODE_FILE_OPERATE_ERROR = 6;
		public static int HEADER_VALIDATE_FAIL = 7;
		public static int HEADER_FILE_NOT_FOUND = 8;
		public static int CONGRUENCE_ENCODING_FAIL = 9;
		public static int UNSUPPORTED_OPERATION = 10;
		public static int ENCODE_WRITE_HEADER_FAIL = 11;
		public static int ENCODE_WRITE_CONTENT_FAIL = 12;
		
	}
	
}
