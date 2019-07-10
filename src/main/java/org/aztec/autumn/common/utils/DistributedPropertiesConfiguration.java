package org.aztec.autumn.common.utils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.utils.annotation.config.Property;
import org.aztec.autumn.common.utils.jdbc.DBManager;
import org.aztec.autumn.common.utils.jdbc.QueryExecutor;

import com.google.common.collect.Maps;

public abstract class DistributedPropertiesConfiguration extends BasePropertiesConfig {
	

	public static final String DEFAULT_CONFIG_SERVER_NAME = "disconf.aztec.org";
	public static final Integer CONFIG_SERVER_PORT = 3306;
	public static final String DEFAULT_QUERY_SQL = "SELECT content FROM `base_meta_info` where key=?1 and type=2";
	@Property("aztec.disconf.username")
	private String username;
	@Property("aztec.disconf.username")
	private String password;
	@Property("aztec.disconf.server.name")
	private String server;
	@Property("aztec.disconf.server.port")
	private Integer port;
	@Property("aztec.disconf.server.type")
	private String type;
	@Property("aztec.disconf.conf.key")
	private String key;
	
	protected Map<String,Object> propertiesMap = Maps.newHashMap(); 

	public DistributedPropertiesConfiguration() {
		super("system");
	}

	private void initData() throws ClassNotFoundException, SQLException {
		switch(type) {
		case "DB":
			if(server == null || server.isEmpty()) {
				server = DEFAULT_CONFIG_SERVER_NAME;
				port = CONFIG_SERVER_PORT;
			}
			DBManager manager = DBManager.getManager("jdbc.mysql://" + server + ":" + port, username, password, "mysql");
			QueryExecutor executor = manager.getQueryExecutor();
			List<Map<String,String>> dbMapList = executor.getQueryResultAsMap(DEFAULT_QUERY_SQL,new Object[] {"USER." + username + "." + key});
			Map<String,String> dbMap = dbMapList.get(0);
			parse(dbMap.get("content"));
			manager.disconnect();
			break;
		}
	}
	
	protected abstract void parse(String content);
}
