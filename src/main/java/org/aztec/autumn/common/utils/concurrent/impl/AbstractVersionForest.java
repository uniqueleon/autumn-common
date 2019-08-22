package org.aztec.autumn.common.utils.concurrent.impl;

import java.util.Map;

import org.aztec.autumn.common.utils.concurrent.NoLockException;
import org.aztec.autumn.common.utils.concurrent.Synchronizable;
import org.aztec.autumn.common.utils.concurrent.VersionForest;
import org.aztec.autumn.common.utils.concurrent.VersionTree;
import org.aztec.autumn.common.utils.concurrent.VersionedNode;

import com.google.common.collect.Maps;

public abstract class AbstractVersionForest implements VersionForest {

	protected static final Map<String,VersionTree> forest = Maps.newHashMap();

	public AbstractVersionForest() {
	}
	
	public abstract <T> VersionTree buildTree(Synchronizable<T> root) throws NoLockException;

	@Override
	public <T> VersionTree findTree(String uuid)  throws NoLockException {
		return forest.get(uuid);
	}

	@Override
	public <T> VersionTree buildIfNotExist(Synchronizable<T> root) throws NoLockException {
		if(!forest.containsKey(root.getUuid())) {
			synchronized(forest){
				if(!forest.containsKey(root.getUuid())) {
					VersionTree tree  = buildTree(root);
					VersionedNode newNode = tree.createNode(root);
					newNode = tree.addNode(newNode);
					forest.put(root.getUuid(), tree);
					return tree;
				}
			}
		}
		return forest.get(root.getUuid());
	}

	@Override
	public <T> boolean isTreeExists(String uuid) throws NoLockException {
		return forest.containsKey(uuid);
	}

	@Override
	public void removeTree(String uuid) throws NoLockException {
		forest.remove(uuid);
	}

	
	

}
