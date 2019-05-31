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
	
	public static final String ZOOKEEPER_PATH_SPLITOR = ".";

	// id | name | content | type | desc | c_gmt | u_gmt | 
	public static interface META_DATA_CENTER_INFO{
		public static final String BASE_TABLE_NAME = "base_meta_info";
		public static final int CONNECTION_CONFIG_TYPE = 1;
		public static final String ZK_CONFIG_NAME = "ZK_CONNECT_INFO";
		public static final String REDIS_CONFIG_NAME = "REDIS_CONNECT_INFO";
		public static final int ZOOKEEPER_CONNECT_INFO_TYPE = 1;
		public static final int REDIS_CONNECT_INFO_TYPE = 2;
		public static final String ZOOKEEPER_CONNECT_QUERY_SQL = "SELECT `content` from " + BASE_TABLE_NAME + " where type=" + CONNECTION_CONFIG_TYPE + " and name = " + ZK_CONFIG_NAME + " limit 1";
		public static final String REDIS_CONNECT_QUERY_SQL = "SELECT `content` from " + BASE_TABLE_NAME + " where type=" + CONNECTION_CONFIG_TYPE + " and name = " + REDIS_CONFIG_NAME + " limit 1";
		public static final String[] TABLE_COLUMNS = {"id","name","content","type","desc","c_gmt","u_gmt"}; 
		public static final int CONTENT_COLUMN_INDEX = 2;
		public static final int NAME_COLUMN_INDEX = 1;
		public static final int TYPE_COLUMN_INDEX = 3;
		public static final int DESC_COLUMN_INDEX = 4;
	}
}
