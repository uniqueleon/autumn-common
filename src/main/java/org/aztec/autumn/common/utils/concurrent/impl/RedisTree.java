package org.aztec.autumn.common.utils.concurrent.impl;

import java.util.List;

import org.aztec.autumn.common.utils.CacheException;
import org.aztec.autumn.common.utils.CacheUtils;
import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.autumn.common.utils.concurrent.NoLockException;
import org.aztec.autumn.common.utils.concurrent.Synchronizable;
import org.aztec.autumn.common.utils.concurrent.NoLockException.ErrorCodes;
import org.aztec.autumn.common.utils.concurrent.VersionTree;
import org.aztec.autumn.common.utils.concurrent.VersionedNode;
import org.aztec.autumn.common.utils.concurrent.VersionedNodeFactory;

public class RedisTree extends InMemoryVersionTree implements VersionTree {
	
	private CacheUtils cacheUtil;
	private static final String REDIS_NO_LOCK_TREE_PREFIX = "CONCURRENT_VERSION_TREE_";
	private static final String NEWEST_VERSION_KEY = "NEWEST_VERSION_OF_TREE";
	private String key;
	private String rootVersion;
	private JsonUtils jsonUtil = UtilsFactory.getInstance().getJsonUtils();
	
	public static String getRedisKey(String uuid) {
		return REDIS_NO_LOCK_TREE_PREFIX + uuid;
	}
	
	public String getNewestVersion() throws CacheException {
		return cacheUtil.hget(key, NEWEST_VERSION_KEY);
	}

	public RedisTree(String treeKey,String rootVersion, CacheUtils cacheUtil) {
		super();
		this.key = getRedisKey(treeKey);
		this.cacheUtil = cacheUtil;
		this.rootVersion = rootVersion;
	}
	
	public RedisTreeNode loadNode(String key,String version) throws Exception{
		String rootData = cacheUtil.hget(key, version);
		
		RedisTreeNode node = jsonUtil.json2Object(rootData, RedisTreeNode.class);
		node.loadData();
		return node;
	}
	
	public void reloadTree() throws Exception {
		root = loadNode(key,rootVersion);
		List<String> nodeDatas = cacheUtil.hkeys(key);
		for(String version : nodeDatas) {
			if(version.equals(NEWEST_VERSION_KEY)
					|| allNodes.containsKey(version)) {
				continue;
			}
			RedisTreeNode treeNode = loadNode(key, version);
			treeNode.getData().setSynchronized(true);
			allNodes.put(version, treeNode);
		}
	}

	@Override
	public VersionedNode addNode(VersionedNode node) throws NoLockException {
		try {
			node = super.addNode(node);
			if(node instanceof RedisTreeNode) {
				RedisTreeNode redisNode = (RedisTreeNode) node;
				cacheUtil.hset(key ,NEWEST_VERSION_KEY, redisNode.getVersion());
			}
			return node;
		} catch (Exception e) {
			throw new NoLockException(ErrorCodes.UNKONW_ERROR,e);
		}
		
	}
	
	public boolean refreshTree() throws NoLockException {
		try {
			synchronized (allNodes) {
				long thisSize = super.getSize();
				long treeLength = cacheUtil.hlen(key);
				if(thisSize != treeLength) {
					reloadTree();
				}
			}
			return true;
		} catch (Exception e) {
			throw new NoLockException(ErrorCodes.UNKONW_ERROR,e);
		}
	}

	@Override
	public VersionedNode getNewestNode() throws NoLockException {
		refreshTree();
		VersionedNode vNode = super.getNewestNode();
		return vNode;
	}

	@Override
	public VersionedNodeFactory getNodeFactory() {
		return new RedisNodeFactory();
	}

	public static class RedisNodeFactory implements VersionedNodeFactory{
		@Override
		public <T> VersionedNode createNode(Synchronizable<T> data) throws NoLockException {

			return new RedisTreeNode(data);
		}
		
	}

}
