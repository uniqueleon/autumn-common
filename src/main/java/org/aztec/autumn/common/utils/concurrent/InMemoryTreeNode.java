package org.aztec.autumn.common.utils.concurrent;

public class InMemoryTreeNode {
	
	private boolean isRoot;
	private boolean isLeaf;
	private int dept;
	private InMemoryTreeNode parent;
	private Synchronizable data;

	public InMemoryTreeNode(Synchronizable data) {
		this.data = data;
	}
	
	public int calcuteDept() {
		dept = 0;
		InMemoryTreeNode tempNode = parent;
		while(tempNode != null) {
			dept++;
			tempNode = tempNode.getParent();
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

	public InMemoryTreeNode getParent() {
		return parent;
	}

	public void setParent(InMemoryTreeNode parent) {
		this.parent = parent;
	}

	public InMemoryTreeNode merge(InMemoryTreeNode otherNode) {
		InMemoryTreeNode newNode = new InMemoryTreeNode(data);
		newNode.data = data.cloneThis();
		newNode.data.merge(otherNode.data);
		newNode.data.setPreviousVersion(data.getVersion());
		newNode.parent = this;
		return newNode;
	}
}
