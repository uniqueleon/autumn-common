package org.aztec.autumn.common.utils.concurrent.impl;

import java.util.Map;

import org.aztec.autumn.common.utils.concurrent.NoLockException;
import org.aztec.autumn.common.utils.concurrent.Synchronizable;
import org.aztec.autumn.common.utils.concurrent.VersionForest;
import org.aztec.autumn.common.utils.concurrent.VersionTree;
import org.aztec.autumn.common.utils.concurrent.VersionedNode;

import com.google.common.collect.Maps;

public class InMemoryForest implements VersionForest {

	private static final Map<String,VersionTree> forest = Maps.newHashMap();

	public InMemoryForest() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public <T> VersionTree findTree(String uuid)  throws NoLockException {
		return forest.get(uuid);
	}

	@Override
	public <T> VersionTree buildIfNotExist(Synchronizable<T> root) throws NoLockException {
		if(!forest.containsKey(root.getUUID())) {
			synchronized(forest){
				if(!forest.containsKey(root.getUUID())) {
					VersionTree tree  = new InMemoryVersionTree();
					VersionedNode newNode = new InMemoryTreeNode(root);
					newNode = tree.addNode(newNode);
					forest.put(root.getUUID(), tree);
					return tree;
				}
			}
		}
		return forest.get(root.getUUID());
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
