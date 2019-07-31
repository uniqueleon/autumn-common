package org.aztec.autumn.common.utils.concurrent;

public interface Synchronizable<T> {

	public String getUUID();
	public String getVersion();
	public Synchronizable<T> cloneThis();
	public void setPreviousVersion(String previousVersion);
	public String getPerviousVersion();
	public int getDept();
	public T getOldData();
	public T getData();
	public void setData(T data);
	public void update(T data);
	public boolean isSynchronized();
	public void setSynchronized(boolean sync);
	public void merge(Synchronizable<T> otherNode);
	public boolean isMergable(Synchronizable<T> otherNode);
	public <E extends Synchronizable<T>> E cast();
}
