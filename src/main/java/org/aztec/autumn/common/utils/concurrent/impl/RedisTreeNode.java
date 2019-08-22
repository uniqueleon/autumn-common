package org.aztec.autumn.common.utils.concurrent.impl;

import org.aztec.autumn.common.utils.CacheUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.autumn.common.utils.concurrent.Synchronizable;
import org.aztec.autumn.common.utils.concurrent.VersionedNode;

public class RedisTreeNode extends InMemoryTreeNode implements VersionedNode {
	
	private String uuid;
	private String version;
	private String previousVersion;
	private String syncClassName;
	private Object dataContent;
	
	public RedisTreeNode() {
		super();
	}

	public RedisTreeNode(Synchronizable data) {
		// TODO Auto-generated constructor stub
		super(data);
		this.uuid = data.getUuid();
		this.dataContent = data.getData();
		this.setData(data);
		this.syncClassName = data.getClass().getName();
		this.version = data.getVersion();
		this.previousVersion = data.getPerviousVersion();
		this.dept = data.getDept();
	}
	
	public void loadData() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class<? extends Synchronizable> dataCls = (Class<? extends Synchronizable>) Class.forName(syncClassName);
		data = dataCls.newInstance();
		data.setData(dataContent);
		data.setPreviousVersion(previousVersion);
		data.setVersion(version);
		data.setSynchronized(true);
		data.setUuid(uuid);
	}

	public String getUUID() {
		return uuid;
	}

	public void setUUID(String uUID) {
		uuid = uUID;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPreviousVersion() {
		return previousVersion;
	}

	public void setPreviousVersion(String previousVersion) {
		this.previousVersion = previousVersion;
	}

	public String getSyncClassName() {
		return syncClassName;
	}

	public void setSyncClassName(String syncClassName) {
		this.syncClassName = syncClassName;
	}
	

	public void setData(Object data) {
		this.dataContent = data;
		setData(data);
	}


	public Object getDataContent() {
		return dataContent;
	}

	public void setDataContent(Object dataContent) {
		this.dataContent = dataContent;
	}

	@Override
	public void persist() throws Exception {
		CacheUtils cacheUtil = UtilsFactory.getInstance().getDefaultCacheUtils();
		String jsonContent = UtilsFactory.getInstance().getJsonUtils().object2Json(this);
		cacheUtil.hset(RedisTree.getRedisKey(data.getUuid()), data.getVersion(),jsonContent);
	}

	
	
}
