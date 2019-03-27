package org.aztec.autumn.common.utils.cache;

import java.io.File;

public class LocalCacheConfig {

	private File configFile;
	private String cacheType;
	public File getConfigFile() {
		return configFile;
	}
	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}
	public String getCacheType() {
		return cacheType;
	}
	public void setCacheType(String cacheType) {
		this.cacheType = cacheType;
	}
	

}
