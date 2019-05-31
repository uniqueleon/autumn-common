package org.aztec.autumn.common.utils.cache;

public interface CacheDataSubscriber {

	public void notify(String channel,String newMsg);
	public void unsubscribe();
	public boolean isUnsubscribed();
}
