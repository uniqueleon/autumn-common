package org.aztec.autumn.common.utils.persistence;

import java.util.List;

public interface PersistenceEventListener {

	public List<PersistenceEvent> listen(boolean async)  throws Exception ;
	public void stopListener();
}
