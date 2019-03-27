package org.aztec.autumn.common.utils.net.rpc;

import org.aztec.autumn.common.utils.net.rpc.server.ServerException;
import org.aztec.autumn.common.utils.persistence.PersistenceException;

public interface ISystemManager {

	public void clearDBCache() throws PersistenceException;
	public void stop() throws ServerException;
	public void start() throws ServerException;
	public void restart() throws ServerException;
	public void test() throws ServerException;
}
