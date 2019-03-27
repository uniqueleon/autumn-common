package org.aztec.autumn.common.utils.compress;

public interface CompressConstant {

	public static final String TEMP_FILE_PREFIX = "CC_{label}_";
	
	public static int DEFAULT_HEADER_LENGTH = 25;
	
	public static class CompressTypes{
		public static String PURE_CONGURENCE = "pure"; // pure
		public static String COMPLEX_CONGURENCE = "complex"; // congruence + zip
		
	}
	
	public static final byte DEFUALT_COMPRESS_ALGORITHM_INDEX = 0x01;
	// 01(main) 000(sub) 000(low) 
	public static final byte DEFAULT_COMPRESS_ALGORITHM_VERSION = 0x40 ;
	
}
