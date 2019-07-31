package org.aztec.autumn.common.utils.concurrent;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.utils.concurrent.NoLockException.ErrorCodes;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;

public class InMemoryVersionTree {
	

	private final Object rootLock = new Object();
	private InMemoryTreeNode root;
	private final Map<String,InMemoryTreeNode> allNodes = Maps.newConcurrentMap();

	public InMemoryVersionTree() {
		// TODO Auto-generated constructor stub
	}

	public InMemoryTreeNode addNode(InMemoryTreeNode node) throws NoLockException {
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
	
	public InMemoryTreeNode findNodeByVersion(String version) throws NoLockException{
		return allNodes.get(version);
	}
	
	public InMemoryTreeNode getNewestNode() throws NoLockException {
		List<InMemoryTreeNode> newestNodes = Lists.newArrayList();
		int curDept = -1;
		for(InMemoryTreeNode node : allNodes.values()) {
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
		InMemoryTreeNode retNode = newestNodes.size() > 0 ? newestNodes.get(0) : null;
		/*
		 * if(retNode != null) { retNode.getData().setSynchronized(true); }
		 */
		return retNode;
	}
	
	public void merge() throws NoLockException {
		List<InMemoryTreeNode> leafs = getLeafs();
		InMemoryTreeNode newNode = getNewestNode();
		for(InMemoryTreeNode node : leafs) {
			if(newNode.getData().getVersion().equals(node.getData().getVersion())){
				continue;
			}
			if(newNode.getData().isMergable(node.getData())) {
				newNode = newNode.merge(node);
				newNode = addNode(newNode);
			}
		}
	}
	
	public List<InMemoryTreeNode> getLeafs() {
		List<InMemoryTreeNode> leafs = Lists.newArrayList();
		for(InMemoryTreeNode node : allNodes.values()) {
			if(node.isLeaf()) {
				leafs.add(node);
			}
		}
		return leafs;
	}

	public InMemoryTreeNode getRoot() {
		return root;
	}


	public void setRoot(InMemoryTreeNode root) {
		this.root = root;
	}


	public Map<String, InMemoryTreeNode> getAllNodes() {
		return allNodes;
	}


	
}
