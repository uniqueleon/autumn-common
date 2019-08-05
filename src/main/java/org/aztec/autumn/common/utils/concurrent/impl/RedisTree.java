package org.aztec.autumn.common.utils.concurrent.impl;

import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.utils.CacheUtils;
import org.aztec.autumn.common.utils.concurrent.NoLockException;
import org.aztec.autumn.common.utils.concurrent.VersionTree;
import org.aztec.autumn.common.utils.concurrent.VersionedNode;

import com.google.common.collect.Maps;

public class RedisTree implements VersionTree {
	
	private CacheUtils cacheUtil;
	private String key;
	private final Map<String,VersionedNode> allNodes = Maps.newConcurrentMap();

	public RedisTree(String treeKey, CacheUtils cacheUtil) {
		// TODO Auto-generated constructor stub
	}
	
	public void reloadTree() {
		
	}

	@Override
	public VersionedNode addNode(VersionedNode node) throws NoLockException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VersionedNode findNodeByVersion(String version) throws NoLockException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VersionedNode getNewestNode() throws NoLockException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void merge() throws NoLockException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<VersionedNode> getLeafs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VersionedNode getRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUUID() {
		// TODO Auto-generated method stub
		return null;
	}

}
