package org.aztec.autumn.common.math.modeling.packing;

public interface AlgorithmContext {

	public <T> T get(String key);
	public void put(String key,Object object);
	public void clear();
}
