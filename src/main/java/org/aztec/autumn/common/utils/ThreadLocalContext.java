package org.aztec.autumn.common.utils;

public interface ThreadLocalContext {

	public <T> T get(String key);
	public void set(String key,Object value); 
	public String getID();
	public void destroy();
}
