package org.aztec.autumn.common.utils.concurrent;

public interface VersionedNode {

	public int calcuteDept();
	public boolean isRoot();
	public Synchronizable getData();
	public boolean isLeaf();
	public void setRoot(boolean isRoot) ;
	public void setLeaf(boolean isLeaf) ;
	public void setParent(VersionedNode parent);
	public VersionedNode getParent();
	public VersionedNode merge(VersionedNode otherNode);
}
