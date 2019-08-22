package org.aztec.autumn.common.utils.concurrent.impl;

import java.util.List;

import org.aztec.autumn.common.utils.concurrent.NoLockException;
import org.aztec.autumn.common.utils.concurrent.Synchronizable;
import org.aztec.autumn.common.utils.concurrent.VersionedNode;
import org.aztec.autumn.common.utils.concurrent.VersionedNodeFactory;

import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class InMemoryTreeNode implements VersionedNode{
	
	protected boolean isRoot;
	protected boolean isLeaf;
	protected int dept;
	@JsonIgnore
	protected VersionedNode parent;
	@JsonIgnore
	protected Synchronizable data;

	public InMemoryTreeNode() {
		super();
	}

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
		this.data.setPreviousVersion(parent.getData().getVersion());
	}

	public VersionedNode merge(VersionedNode branchNode,VersionedNode otherNode,VersionedNodeFactory nodeFactory) throws NoLockException {
		VersionedNode newNode = nodeFactory.createNode(data);
		newNode.setData( data.cloneThis());
		newNode.getData().merge(otherNode.getData(),branchNode.getData());
		newNode.getData().setVersion(data.generateVersion());
		return newNode;
	}

	@Override
	@JsonIgnore
	public List<VersionedNode> getAncestors() {
		List<VersionedNode> ancestors = Lists.newArrayList();
		VersionedNode acestor = parent;
		while(acestor != null) {
			if(ancestors.contains(acestor)) {
				throw new IllegalStateException("There may be a loop in tree!");
			}
			ancestors.add(acestor);
			acestor = acestor.getParent();
		}
		return ancestors;
	}

	@Override
	public void persist()  throws Exception{
	}
}
