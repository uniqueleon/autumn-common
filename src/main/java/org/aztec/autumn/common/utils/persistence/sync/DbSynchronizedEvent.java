package org.aztec.autumn.common.utils.persistence.sync;

import org.aztec.autumn.common.utils.persistence.EventSource;
import org.aztec.autumn.common.utils.persistence.PersistenceEvent;

public class DbSynchronizedEvent implements PersistenceEvent {

	private static final String type = "";
	private EventSource source;
	
	public DbSynchronizedEvent(EventSource source) {
		this.source = source;
	}

	@Override
	public EventType getEventType() {
		// TODO Auto-generated method stub
		return EventType.DB_CACHE_SYNCHORIZED;
	}

	@Override
	public EventSource getSource() {
		return source;
	}

}
