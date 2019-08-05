package org.aztec.autumn.common.utils.concurrent.impl;

import org.aztec.autumn.common.utils.CacheException;
import org.aztec.autumn.common.utils.CacheUtils;
import org.aztec.autumn.common.utils.concurrent.NoLockException;
import org.aztec.autumn.common.utils.concurrent.Synchronizable;
import org.aztec.autumn.common.utils.concurrent.VersionForest;
import org.aztec.autumn.common.utils.concurrent.VersionTree;
import org.aztec.autumn.common.utils.concurrent.NoLockException.ErrorCodes;

public class RedisForest implements VersionForest {
	
	private CacheUtils cacheUtil;
	private String key;
	private static final String LOCK_UUID = "ONLY_FOR_LOCK_NOT_USER";
	private static final long LOCK_TIME_OUT = 60000;
	
	// HSET UUID -> POINTER
	// POINTER -> HSET
	// HSET -> NODE

	public RedisForest(String redisKey,CacheUtils cacheUtil) {
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public <T> VersionTree findTree(String uuid) throws NoLockException {
		
		try {
			if(cacheUtil.hexists(key, uuid)) {
				
			}
			return null;
		} catch (CacheException e) {
			throw new NoLockException(ErrorCodes.UNKONW_ERROR,e);
		}
	}

	@Override
	public void removeTree(String uuid) throws NoLockException {
		try {
			cacheUtil.hdel(key, uuid);
		} catch (CacheException e) {
			throw new NoLockException(ErrorCodes.UNKONW_ERROR,e);
		}
	}

	@Override
	public <T> VersionTree buildIfNotExist(Synchronizable<T> root) throws NoLockException {
		try {
			if(!isTreeExists(root.getUUID())) {
				cacheUtil.lock(LOCK_UUID, LOCK_TIME_OUT);
				if(!isTreeExists(root.getUUID())) {
					
				}
			}
			return null;
		} catch (CacheException e) {
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
			return cacheUtil.hexists(key, uuid);
		} catch (CacheException e) {
			throw new NoLockException(ErrorCodes.UNKONW_ERROR,e);
		}
	}

}
