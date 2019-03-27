package org.aztec.autumn.common;

import java.text.SimpleDateFormat;

public interface GlobalConst {

	public static final int DEFAULT_BUFFER_SIZE = 10  * 1024;
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	public static final int DEFAULT_PAGE_SIZE = 10;

	public static final int MAX_ACCEPTABLE_PAGE_SIZE = 2 * 1024 * 1024;
	
	public static final String DEFAULT_SHOW_TIME = "00:00:00";
	public static final int MAX_BUFFER_SIZE = 1 * 1024 * 1024;
	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	
}
