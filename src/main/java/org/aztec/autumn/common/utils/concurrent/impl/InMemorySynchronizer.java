package org.aztec.autumn.common.utils.concurrent.impl;

import java.util.Map;

import org.aztec.autumn.common.utils.concurrent.NoLockDataSynchronizer;
import org.aztec.autumn.common.utils.concurrent.NoLockException;
import org.aztec.autumn.common.utils.concurrent.Synchronizable;
import org.aztec.autumn.common.utils.concurrent.VersionTree;
import org.aztec.autumn.common.utils.concurrent.VersionedNode;
import org.aztec.autumn.common.utils.concurrent.NoLockException.ErrorCodes;

import com.google.common.collect.Maps;

public class InMemorySynchronizer implements NoLockDataSynchronizer{
	
	public static final Map<String,VersionTree> forest = Maps.newHashMap();

	public InMemorySynchronizer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Synchronizable synchronize(Synchronizable target) throws NoLockException {
		// TODO Auto-generated method stub
		if(!forest.containsKey(target.getUUID())) {
			synchronized(forest){
				if(!forest.containsKey(target.getUUID())) {
					VersionTree tree  = new InMemoryVersionTree();
					VersionedNode newNode = new InMemoryTreeNode(target);
					newNode = tree.addNode(newNode);
					forest.put(target.getUUID(), tree);
					return newNode.getData().cloneThis();
				}
			}
		}
		VersionTree tree = forest.get(target.getUUID());
		VersionedNode root = tree.getRoot();
		VersionedNode newNode = new InMemoryTreeNode(target);
		if(root == null) {
			try {
				newNode = tree.addNode(newNode);
				return newNode.getData().cloneThis();
			} catch (NoLockException e) {
				if(e.getErrorCode() == ErrorCodes.PARENT_NOT_EXISTS) {
					newNode = tree.getNewestNode();
					return newNode.getData().cloneThis();
				}
				else {
					throw e;
				}
			}
		}
		if(!target.isSynchronized()) {
			if(target.getPerviousVersion() == null) {
				return tree.getNewestNode().getData().cloneThis();
			}
			else {
				VersionedNode parent = tree.findNodeByVersion(newNode.getData().getPerviousVersion());
				
				newNode.setParent(parent);
				boolean outdate = true;
				while(outdate) {
					try {
						VersionedNode retNode = tree.addNode(newNode);
						outdate = false;
						return retNode.getData().cloneThis();
					} catch (NoLockException e) {
						if(e.getErrorCode() == NoLockException.ErrorCodes.DATA_EXPIRED) {
							outdate = true;
							newNode = tree.getNewestNode();
						}
						else {
							throw e;
						}
					}
				}
			}
		}
		return tree.getNewestNode().getData().cloneThis();
	}
	

	@Override
	public void release(Synchronizable target) {
		forest.remove(target.getUUID());
	}


	@Override
	public Synchronizable getNewestVersion(String uuid) throws NoLockException {
		if(forest.containsKey(uuid)){
			VersionTree tree = forest.get(uuid);
			return tree.getNewestNode().getData().cloneThis();
		}
		return null;
	}

	@Override
	public void merge(String uuid) throws NoLockException {
		if(forest.containsKey(uuid)){
			VersionTree tree = forest.get(uuid);
			tree.merge();
		}
	}

}
