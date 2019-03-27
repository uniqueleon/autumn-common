package org.aztec.autumn.common.utils.persistence.impl;

import org.aztec.autumn.common.utils.persistence.EventSource;

public class HostNameSource implements EventSource {

	private String hostName;
	
	public HostNameSource(String hostName) {
		this.hostName = hostName;
	}

	@Override
	public String getSourceID() {
		// TODO Auto-generated method stub
		return hostName;
	}

}
