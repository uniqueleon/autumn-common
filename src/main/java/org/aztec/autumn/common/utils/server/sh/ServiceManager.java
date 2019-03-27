package org.aztec.autumn.common.utils.server.sh;

public class ServiceManager implements IServiceManager {

	public ServiceManager() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isServiceAvailable() {
		return true;
	}

	@Override
	public boolean touch() {
		return true;
	}

}
