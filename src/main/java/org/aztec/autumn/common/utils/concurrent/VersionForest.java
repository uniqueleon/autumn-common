package org.aztec.autumn.common.utils.concurrent;

public interface VersionForest {

	public <T> VersionTree findTree(String uuid) throws NoLockException ;
	public void removeTree(String uuid) throws NoLockException ;
	public <T> VersionTree buildIfNotExist(Synchronizable<T> root) throws NoLockException ;
	public <T> boolean isTreeExists(String uuid) throws NoLockException ;
}
