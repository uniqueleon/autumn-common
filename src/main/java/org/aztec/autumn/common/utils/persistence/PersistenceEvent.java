package org.aztec.autumn.common.utils.persistence;

public interface PersistenceEvent {

	public static enum EventType{
		DB_CACHE_SYNCHORIZED;
	}
	
	public EventType getEventType();
	public EventSource getSource();
}
