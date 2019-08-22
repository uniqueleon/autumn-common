package org.aztec.autumn.common.utils.concurrent;

public interface Synchronizable<T> {

	public String getUuid();
	public void setUuid(String uuid);
	public String getVersion();
	public String generateVersion();
	public Synchronizable<T> cloneThis();
	public void setPreviousVersion(String previousVersion);
	public String getPerviousVersion();
	public void setVersion(String version);
	public int getDept();
	public T getOldData();
	public T getData();
	public void setData(T data);
	public void update(T data);
	public boolean isSynchronized();
	public boolean isUpdated();
	public void setSynchronized(boolean sync);
	public void merge(Synchronizable<T> otherVer,Synchronizable<T> conflictVer);
	public boolean isMergable(Synchronizable<T> otherNode);
	public <E extends Synchronizable<T>> E cast();
}
