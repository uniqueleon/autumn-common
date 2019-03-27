package org.aztec.autumn.common.utils.persistence.sync;

import javax.persistence.PersistenceException;

public interface PersistenceSynchronizer {

	public void synchronize() throws PersistenceException;
}
