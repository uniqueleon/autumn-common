package org.aztec.autumn.common.utils.concurrent.impl;

import org.aztec.autumn.common.utils.concurrent.Synchronizable;
import org.aztec.autumn.common.utils.concurrent.VersionTree;

public class InMemoryForest extends AbstractVersionForest {

	public InMemoryForest() {
		super();
	}

	@Override
	public <T> VersionTree buildTree(Synchronizable<T> root) {
		return new InMemoryVersionTree();
	}

}
