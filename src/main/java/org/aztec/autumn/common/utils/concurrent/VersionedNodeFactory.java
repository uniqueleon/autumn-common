package org.aztec.autumn.common.utils.concurrent;

public interface VersionedNodeFactory {

	public <T> VersionedNode createNode(Synchronizable<T> data) throws NoLockException ;
}
