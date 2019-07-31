package org.aztec.autumn.common.zk;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Lists;

public abstract class AbstractSubNodeReloader implements TimeLimitedCallable{
	
	private ZkConfig parent;
	private List<ZkConfig> childrens;

	public AbstractSubNodeReloader(ZkConfig parent) {
		this.parent = parent;
	}
	
	public Object call() throws Exception {
		if(!CollectionUtils.isEmpty(childrens)) {
			for(ZkConfig child : childrens) {
				child.destroy();
			}
			childrens.clear();
		}
		load();
		return null;
	}
	
	public void load() throws Exception {
		childrens = Lists.newArrayList();
		int loadSize = parent.getSubNodes().size();
		for(int i = 0;i < loadSize ;i++) {
			childrens.add(loadChild(i));
		}
		setChildrens(childrens);
	}
	
	protected abstract void setChildrens(List children) throws Exception;
	protected abstract ZkConfig loadChild(int index) throws Exception;


	public TimeUnit getUnit() {
		return TimeUnit.MILLISECONDS;
	}

	public void interupt() {
		// TODO Auto-generated method stub
		
	}

	

}
