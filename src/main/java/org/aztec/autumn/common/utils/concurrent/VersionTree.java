package org.aztec.autumn.common.utils.concurrent;

import java.util.List;

public interface VersionTree {

	public VersionedNode addNode(VersionedNode node) throws NoLockException;
	public VersionedNode findNodeByVersion(String version) throws NoLockException;
	public VersionedNode getNewestNode() throws NoLockException;
	public void merge() throws NoLockException;
	public List<VersionedNode> getLeafs();
	public VersionedNode getRoot();
}
