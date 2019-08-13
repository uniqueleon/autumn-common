package org.aztec.autumn.common.utils.cache;

import org.aztec.autumn.common.GlobalConst;
import org.aztec.autumn.common.utils.DatabasePropertiesConfig;
import org.aztec.autumn.common.utils.annotation.config.Property;

public class RedisConnectionConfig extends DatabasePropertiesConfig {

	@Property("hosts")
	private String hosts;
	@Property("ports")
	private String ports;
	@Property("password")
	private String password;

	public RedisConnectionConfig() throws Exception {
		super(GlobalConst.META_DATA_CENTER_INFO.REDIS_CONNECT_QUERY_SQL,
				GlobalConst.META_DATA_CENTER_INFO.TABLE_COLUMNS[GlobalConst.META_DATA_CENTER_INFO.CONTENT_COLUMN_INDEX]);
		loadInfoFromDB();
		init();
	}

	public String getHosts() {
		return hosts;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
	}

	public String getPorts() {
		return ports;
	}

	public void setPorts(String ports) {
		this.ports = ports;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
