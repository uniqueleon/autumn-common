package org.aztec.autumn.common.utils.concurrent.impl;

import org.aztec.autumn.common.utils.concurrent.NoLockDataSynchronizer;
import org.aztec.autumn.common.utils.concurrent.NoLockException;
import org.aztec.autumn.common.utils.concurrent.Synchronizable;
import org.aztec.autumn.common.utils.concurrent.VersionForest;
import org.aztec.autumn.common.utils.concurrent.VersionTree;
import org.aztec.autumn.common.utils.concurrent.VersionedNode;
import org.aztec.autumn.common.utils.concurrent.VersionedNodeFactory;

public class InMemorySynchronizer implements NoLockDataSynchronizer{
	
	//private static final Map<String,VersionTree> forest = Maps.newHashMap();
	

	private VersionForest forest;
	private VersionedNodeFactory nodeFactory;

	public InMemorySynchronizer(VersionForest forest,VersionedNodeFactory nodeFactory) {
		// TODO Auto-generated constructor stub
		this.forest = forest;
		this.nodeFactory = nodeFactory;
	}

	@Override
	public Synchronizable synchronize(Synchronizable target) throws NoLockException {
		// TODO Auto-generated method stub
		if(!forest.isTreeExists(target.getUUID())) {
			synchronized(forest){
				if(!forest.isTreeExists(target.getUUID())) {
					VersionTree tree = forest.buildIfNotExist(target);
					return tree.getNewestNode().getData();
				}
			}
		}
		VersionTree tree = forest.findTree(target.getUUID());
		VersionedNode newNode = nodeFactory.createNode(target);
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
	public void release(Synchronizable target) throws NoLockException {
		forest.removeTree(target.getUUID());
	}


	@Override
	public Synchronizable getNewestVersion(String uuid) throws NoLockException {
		if(forest.isTreeExists(uuid)){
			VersionTree tree = forest.findTree(uuid);
			return tree.getNewestNode().getData().cloneThis();
		}
		return null;
	}

	@Override
	public void merge(String uuid) throws NoLockException {
		if(forest.isTreeExists(uuid)){
			VersionTree tree = forest.findTree(uuid);
			tree.merge();
		}
	}

}
