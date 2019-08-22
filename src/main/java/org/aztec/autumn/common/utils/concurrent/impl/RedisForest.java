package org.aztec.autumn.common.utils.concurrent.impl;

import org.aztec.autumn.common.utils.CacheException;
import org.aztec.autumn.common.utils.CacheUtils;
import org.aztec.autumn.common.utils.concurrent.NoLockException;
import org.aztec.autumn.common.utils.concurrent.Synchronizable;
import org.aztec.autumn.common.utils.concurrent.VersionForest;
import org.aztec.autumn.common.utils.concurrent.VersionTree;
import org.aztec.autumn.common.utils.concurrent.VersionedNode;
import org.aztec.autumn.common.utils.concurrent.VersionedNodeFactory;
import org.aztec.autumn.common.utils.concurrent.NoLockException.ErrorCodes;

public class RedisForest extends AbstractVersionForest implements VersionForest {
	
	private CacheUtils cacheUtil;
	private final static String DEFAULT_FOREST_KEY = "CONCURRENT_VERSION_FOREST";
	private static final String LOCK_UUID = "ONLY_FOR_LOCK_NOT_USER";
	private static final long LOCK_TIME_OUT = 60000;
	
	// HSET UUID -> POINTER
	// POINTER -> HSET
	// HSET -> NODE
	

	public RedisForest(CacheUtils cacheUtil) {
		super();
		this.cacheUtil = cacheUtil;
	}

	@Override
	public <T> VersionTree findTree(String uuid) throws NoLockException {
		try {
			if(cacheUtil.hexists(DEFAULT_FOREST_KEY, uuid) && !forest.containsKey(uuid)) {
				forest.put(uuid, buildTree(uuid));
			}
			RedisTree rTree = (RedisTree) forest.get(uuid);
			rTree.refreshTree();
			return super.findTree(uuid);
		} catch (Exception e) {
			throw new NoLockException(ErrorCodes.UNKONW_ERROR,e);
		}
		finally {
		}
	}

	@Override
	public void removeTree(String uuid) throws NoLockException {
		try {
			if(isTreeExists(uuid)) {
				cacheUtil.hdel(DEFAULT_FOREST_KEY, uuid);
				cacheUtil.hdelAll(RedisTree.getRedisKey(uuid));
				super.removeTree(uuid);
			}
		} catch (CacheException e) {
			throw new NoLockException(ErrorCodes.UNKONW_ERROR,e);
		}
	}

	@Override
	public <T> VersionTree buildIfNotExist(Synchronizable<T> root) throws NoLockException {
		try {
			cacheUtil.lock(LOCK_UUID, LOCK_TIME_OUT);
			if(isTreeExists(root.getUuid())) {
				buildTree(root.getUuid());
				return forest.get(root.getUuid());
			}
			cacheUtil.hset(DEFAULT_FOREST_KEY, root.getUuid(), root.getVersion());
			VersionTree vt = super.buildIfNotExist(root);
			return vt;
		} catch (Exception e) {
			e.printStackTrace();
			throw new NoLockException(ErrorCodes.UNKONW_ERROR,e);
		}
		finally {
			try {
				cacheUtil.unlock(LOCK_UUID);
			} catch (CacheException e) {
				throw new NoLockException(ErrorCodes.UNKONW_ERROR,e);
			}
		}
	}

	@Override
	public <T> boolean isTreeExists(String uuid) throws NoLockException {
		try {
			String rootVersion = cacheUtil.hget(DEFAULT_FOREST_KEY, uuid);
			if (rootVersion != null) {
				return cacheUtil.hexists(RedisTree.getRedisKey(uuid), rootVersion);
			}
			return false;
		} catch (CacheException e) {
			throw new NoLockException(ErrorCodes.UNKONW_ERROR,e);
		}
	}

	@Override
	public <T> VersionTree buildTree(Synchronizable<T> root) throws NoLockException {
		try {
			return new RedisTree(root.getUuid(), root.getVersion(), cacheUtil);
		} catch (Exception e) {
			throw new NoLockException(ErrorCodes.UNKONW_ERROR,e);
		}
	}

	public VersionTree buildTree(String uuid) throws Exception {
		
		if(!forest.containsKey(uuid)) {
			synchronized (forest) {
				if(!forest.containsKey(uuid)) {
					String rootVersion = cacheUtil.hget(DEFAULT_FOREST_KEY, uuid);
					RedisTree vt = new RedisTree(uuid, rootVersion, cacheUtil);
					vt.reloadTree();
					forest.put(uuid, vt);
				}
			}
		}
		return forest.get(uuid);
	}

}
