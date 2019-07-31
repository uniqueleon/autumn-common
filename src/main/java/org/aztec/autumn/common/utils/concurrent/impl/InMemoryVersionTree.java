package org.aztec.autumn.common.utils.concurrent.impl;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.utils.concurrent.NoLockException;
import org.aztec.autumn.common.utils.concurrent.VersionTree;
import org.aztec.autumn.common.utils.concurrent.VersionedNode;
import org.aztec.autumn.common.utils.concurrent.NoLockException.ErrorCodes;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;

public class InMemoryVersionTree implements VersionTree{
	

	private final Object rootLock = new Object();
	private VersionedNode root;
	private final Map<String,VersionedNode> allNodes = Maps.newConcurrentMap();

	public InMemoryVersionTree() {
		// TODO Auto-generated constructor stub
	}

	public VersionedNode addNode(VersionedNode node) throws NoLockException {
		if(root == null) {
			synchronized (rootLock) {
				if(root == null) {
					root = node;
					root.setRoot(true);
					root.setLeaf(true);
					root.getData().setSynchronized(true);
					allNodes.put(root.getData().getVersion(), root);
					return root;
				}
			}
		}
		if (allNodes.containsKey(node.getData().getVersion())) {
			throw new NoLockException(ErrorCodes.DUPLICATED_VERSION);
		}
		if(node.getParent() == null) {
			throw new NoLockException(ErrorCodes.PARENT_NOT_EXISTS);
		}
		;
		node.setRoot(false);
		node.setLeaf(true);
		node.getParent().setLeaf(false);
		node.getData().setPreviousVersion(node.getParent().getData().getVersion());
		node.getData().setSynchronized(true);
		allNodes.put(node.getData().getVersion(), node);
		return node;
	}
	
	public VersionedNode findNodeByVersion(String version) throws NoLockException{
		return allNodes.get(version);
	}
	
	public VersionedNode getNewestNode() throws NoLockException {
		List<VersionedNode> newestNodes = Lists.newArrayList();
		int curDept = -1;
		for(VersionedNode node : allNodes.values()) {
			int dept = node.calcuteDept();
			if(curDept < dept ) {
				curDept = dept;
				newestNodes.clear();
				newestNodes.add(node);
			}
			else if(curDept == dept) {
				newestNodes.add(node);
			}
		}
		VersionedNode retNode = newestNodes.size() > 0 ? newestNodes.get(0) : null;
		/*
		 * if(retNode != null) { retNode.getData().setSynchronized(true); }
		 */
		return retNode;
	}
	
	public void merge() throws NoLockException {
		List<VersionedNode> leafs = getLeafs();
		VersionedNode newNode = getNewestNode();
		for(VersionedNode node : leafs) {
			if(newNode.getData().getVersion().equals(node.getData().getVersion())){
				continue;
			}
			if(newNode.getData().isMergable(node.getData())) {
				newNode = newNode.merge(node);
				newNode = addNode(newNode);
			}
		}
	}
	
	public List<VersionedNode> getLeafs() {
		List<VersionedNode> leafs = Lists.newArrayList();
		for(VersionedNode node : allNodes.values()) {
			if(node.isLeaf()) {
				leafs.add(node);
			}
		}
		return leafs;
	}

	public VersionedNode getRoot() {
		return root;
	}


	public void setRoot(InMemoryTreeNode root) {
		this.root = root;
	}


	public Map<String, VersionedNode> getAllNodes() {
		return allNodes;
	}


	
}
