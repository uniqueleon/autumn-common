package org.aztec.autumn.common.utils.concurrent;

import java.util.List;

public interface VersionedNode {

	public int calcuteDept();
	public boolean isRoot();
	public <T> Synchronizable<T> getData();
	public <T> void setData(Synchronizable<T> data);
	public boolean isLeaf();
	public void setRoot(boolean isRoot) ;
	public void setLeaf(boolean isLeaf) ;
	public void setParent(VersionedNode parent);
	public VersionedNode getParent();
	public List<VersionedNode> getAncestors();
	public VersionedNode merge(VersionedNode branchNode,VersionedNode otherNode,VersionedNodeFactory nodeFactory) throws NoLockException;
	public void persist() throws Exception;
}
