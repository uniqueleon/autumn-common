package org.aztec.autumn.common.algorithm;

public interface AlgorithmContext {

	public <T> T get(String key);
	public void set(String key,Object value); 
	
}
