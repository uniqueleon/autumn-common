package org.aztec.autumn.common.utils.concurrent.impl;

import org.aztec.autumn.common.utils.concurrent.NoLockException;
import org.aztec.autumn.common.utils.concurrent.Synchronizable;
import org.aztec.autumn.common.utils.concurrent.VersionedNode;
import org.aztec.autumn.common.utils.concurrent.VersionedNodeFactory;

public class InMemoryNodeFactory implements VersionedNodeFactory {

	public InMemoryNodeFactory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public <T> VersionedNode createNode(Synchronizable<T> data) throws NoLockException {
		return new InMemoryTreeNode(data);
	}

}
