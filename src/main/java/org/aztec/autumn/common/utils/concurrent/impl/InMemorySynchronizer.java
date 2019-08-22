package org.aztec.autumn.common.utils.concurrent.impl;

import org.aztec.autumn.common.utils.concurrent.NoLockDataSynchronizer;
import org.aztec.autumn.common.utils.concurrent.NoLockException;
import org.aztec.autumn.common.utils.concurrent.NoLockException.ErrorCodes;
import org.aztec.autumn.common.utils.concurrent.Synchronizable;
import org.aztec.autumn.common.utils.concurrent.VersionForest;
import org.aztec.autumn.common.utils.concurrent.VersionTree;
import org.aztec.autumn.common.utils.concurrent.VersionedNode;

public class InMemorySynchronizer implements NoLockDataSynchronizer{
	
	//private static final Map<String,VersionTree> forest = Maps.newHashMap();
	

	private VersionForest forest;

	public InMemorySynchronizer(VersionForest forest) {
		// TODO Auto-generated constructor stub
		this.forest = forest;
	}

	@Override
	public Synchronizable synchronize(Synchronizable target) throws NoLockException {
		// TODO Auto-generated method stub
		if(!forest.isTreeExists(target.getUuid())) {
			synchronized(forest){
				if(!forest.isTreeExists(target.getUuid())) {
					VersionTree tree = forest.buildIfNotExist(target);
					return tree.getNewestNode().getData();
				}
			}
		}
		VersionTree tree = forest.findTree(target.getUuid());
		VersionedNode newNode = tree.createNode(target);
		if(!target.isSynchronized()) {
			VersionedNode newestNode = tree.getNewestNode();
			return newestNode.getData().cloneThis();
		}else if(target.isUpdated()){
			VersionedNode parent = tree.findNodeByVersion(newNode.getData().getPerviousVersion());
			if(parent == null) {
				return tree.getNewestNode().getData();
			}
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
		return tree.getNewestNode().getData().cloneThis();
	}
	

	@Override
	public void release(Synchronizable target) throws NoLockException {
		forest.removeTree(target.getUuid());
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

	@Override
	public Synchronizable getConflictVersion(Synchronizable s1, Synchronizable s2) throws NoLockException {
		// TODO Auto-generated method stub
		if(!s1.getUuid().equals(s2.getUuid())) {
			throw new NoLockException(ErrorCodes.DIFFERENCE_TREE);
		}
		VersionTree vt = forest.findTree(s1.getUuid());
		VersionedNode branchNode = vt.findBranchNode(s1.getVersion(), s2.getVersion());
		VersionedNode root = vt.getRoot();
		return branchNode != null && branchNode != root ? branchNode.getData() : null;
	}

	@Override
	public Long getVersionedDataSize(String uuid) throws NoLockException {
		VersionTree vt = forest.findTree(uuid);
		return vt != null ? vt.getSize() : 0l;
	}

}
