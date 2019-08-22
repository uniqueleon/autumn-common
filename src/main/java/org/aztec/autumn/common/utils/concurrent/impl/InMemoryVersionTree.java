package org.aztec.autumn.common.utils.concurrent.impl;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.utils.concurrent.NoLockException;
import org.aztec.autumn.common.utils.concurrent.VersionTree;
import org.aztec.autumn.common.utils.concurrent.VersionedNode;
import org.aztec.autumn.common.utils.concurrent.VersionedNodeFactory;
import org.aztec.autumn.common.utils.concurrent.NoLockException.ErrorCodes;
import org.aztec.autumn.common.utils.concurrent.Synchronizable;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;

public class InMemoryVersionTree implements VersionTree{
	

	protected final Object rootLock = new Object();
	protected VersionedNode root;
	protected final Map<String,VersionedNode> allNodes = Maps.newConcurrentMap();

	public InMemoryVersionTree() {
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
					try {
						root.persist();
					} catch (Exception e) {
						throw new NoLockException(ErrorCodes.UNKONW_ERROR,e);
					}
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
		try {
			node.persist();
		} catch (Exception e) {
			throw new NoLockException(ErrorCodes.UNKONW_ERROR,e);
		}
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
		//VersionedNode newNode = leafs.get(0);
		for(VersionedNode node : leafs) {
			if(newNode.getData().getVersion().equals(node.getData().getVersion())){
				continue;
			}
			if(newNode.getData().isMergable(node.getData())) {
				//VersionedNode conflictNode = findConflictNode(newNode, node);
//				if(conflictNode == null) {
//					continue;
//				}
				VersionedNode branchNode = findBranchNode(newNode, node);
				VersionedNode createNode = newNode.merge(branchNode,node,getNodeFactory());
				//conflictNode.setParent(newNode);
				newNode.setLeaf(false);
				try {
					newNode.persist();
					//conflictNode.persist();
				} catch (Exception e) {
					throw new NoLockException(ErrorCodes.UNKONW_ERROR,e);
				}
				createNode.setParent(newNode);
				newNode = addNode(createNode);
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

	@Override
	public String getUUID() {
		return root.getData().getUuid();
	}
	
	public VersionedNode findConflictNode(VersionedNode node1, VersionedNode node2){
		List<VersionedNode> acestors1 = node1.getAncestors();
		List<VersionedNode> acestors2 = node2.getAncestors();
		for(int i = 0;i < acestors1.size();i++) {
			VersionedNode acestor1 = acestors1.get(i);
			for(int j = 0;j < acestors2.size();j++) {
				VersionedNode acestor2 = acestors2.get(j);
				if(acestor1.getData().getVersion().equals(acestor2.getData().getVersion())) {
					return j > 0 ? acestors2.get(j - 1) : (!node2.getData().getVersion().equals(acestor1.getData().getVersion()) ? node2 : null);
				}
			}
		}
		return null;
	}
	
	public VersionedNode findBranchNode(VersionedNode node1, VersionedNode node2) throws NoLockException {
		
		List<VersionedNode> acestors1 = node1.getAncestors();
		List<VersionedNode> acestors2 = node2.getAncestors();
		for(VersionedNode acestor1 : acestors1) {
			for(VersionedNode acestor2 : acestors2) {
				if(acestor1.getData().getVersion().equals(acestor2.getData().getVersion())) {
					return acestor1;
				}
			}
		}
		return null;
	}

	@Override
	public VersionedNode findBranchNode(String version1, String version2) throws NoLockException {
		if(root.getData().getVersion().equals(version1)) {
			throw new NoLockException(ErrorCodes.UNSUPPORT_OPERATION);
		}
		if(!version1.equals(version2)) {
			VersionedNode node1 = findNodeByVersion(version1);
			VersionedNode node2 = findNodeByVersion(version2);
			return findBranchNode(node1, node1);
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		return allNodes.isEmpty();
	}

	@Override
	public Long getSize() {
		return new Long(allNodes.size());
	}

	@Override
	public <T> VersionedNode createNode(Synchronizable<T> data) throws NoLockException {
		return getNodeFactory().createNode(data);
	}

	@Override
	public VersionedNodeFactory getNodeFactory() {
		return new InMemoryNodeFactory();
	}

}
