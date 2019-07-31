package org.aztec.autumn.common.utils.concurrent.impl;

import org.aztec.autumn.common.utils.concurrent.Synchronizable;
import org.aztec.autumn.common.utils.concurrent.VersionedNode;

public class InMemoryTreeNode implements VersionedNode{
	
	private boolean isRoot;
	private boolean isLeaf;
	private int dept;
	private VersionedNode parent;
	private Synchronizable data;

	public InMemoryTreeNode(Synchronizable data) {
		this.data = data;
	}
	
	public int calcuteDept() {
		dept = 0;
		VersionedNode tempNode = parent;
		while(tempNode != null) {
			dept++;
			tempNode = tempNode.getParent();;
		}
		return dept;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public int getDept() {
		return dept;
	}

	public void setDept(int dept) {
		this.dept = dept;
	}

	
	
	public Synchronizable getData() {
		return data;
	}

	public void setData(Synchronizable data) {
		this.data = data;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public VersionedNode getParent() {
		return parent;
	}

	public void setParent(VersionedNode parent) {
		this.parent = parent;
	}

	public VersionedNode merge(VersionedNode otherNode) {
		InMemoryTreeNode newNode = new InMemoryTreeNode(data);
		newNode.data = data.cloneThis();
		newNode.data.merge(otherNode.getData());
		newNode.data.setPreviousVersion(data.getVersion());
		newNode.parent = this;
		return newNode;
	}
}
