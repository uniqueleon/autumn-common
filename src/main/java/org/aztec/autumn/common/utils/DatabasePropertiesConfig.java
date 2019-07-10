package org.aztec.autumn.common.utils;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.utils.jdbc.DBManager;
import org.aztec.autumn.common.utils.jdbc.QueryExecutor;

import com.mchange.util.AssertException;

public abstract class DatabasePropertiesConfig extends BasePropertiesConfig {
	
	private String prefix;
	private String querySql;
	private String colName;
	
	public static final String DEFAULT_PREFIX = "autumn.config.";

	public DatabasePropertiesConfig(String filePath,String prefix,
			String querySql,String colName) {
		super(filePath);
		this.querySql = querySql;
		this.colName = colName;
		this.prefix = prefix == null ? DEFAULT_PREFIX : prefix;
		// TODO Auto-generated constructor stub
	}
	
	public DatabasePropertiesConfig(
			String querySql,String colName) {
		super("system");
		this.querySql = querySql;
		this.colName = colName;
		this.prefix = DEFAULT_PREFIX ;
		// TODO Auto-generated constructor stub
	}

	
	protected void loadInfoFromDB() throws Exception {
		String dbUrl = properties.getProperty(prefix + "jdbc.url");
		String[] authUser = properties.getProperty(prefix + "auth").split(":");
		String dbType = properties.getProperty(prefix + "db.type");
		DBManager dbManager =  DBManager.getManager(dbUrl, authUser[0], authUser[1], dbType);
		if(dbManager != null) {
			QueryExecutor executor = dbManager.getQueryExecutor();
			/*List<Map<String,String>> retList = executor.getQueryResultAsMap(
					META_DATA_CENTER_INFO.ZOOKEEPER_CONNECT_QUERY_SQL);*/
			List<Map<String,String>> retList = executor.getQueryResultAsMap(querySql);
			if(retList == null || retList.size() == 0) {
				throw new AssertException("No zookeeper config in db!");
			}
			Map<String, String> targetRow = retList.get(0);
			String jsonContent = targetRow.get(colName);
			/*String jsonContent = targetRow.get(
					META_DATA_CENTER_INFO.TABLE_COLUMNS[META_DATA_CENTER_INFO.CONTENT_COLUMN_INDEX]);*/
			JsonUtils jsonUtils = UtilsFactory.getInstance().getJsonUtils();
			Map<String,Object> dataMap = jsonUtils.json2Object(jsonContent, Map.class);
			properties.clear();
			for(String key : dataMap.keySet()) {
				properties.put(key, dataMap.get(key));
			}
		}
		dbManager.disconnect();
		init();
	}

}
