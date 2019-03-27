package org.aztec.autumn.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadLocalContextBuilder {
	
	private static final Map<String,DefaultContext> contexts = new ConcurrentHashMap<String, ThreadLocalContextBuilder.DefaultContext>();

	public static ThreadLocalContext build(String id) {
		return new DefaultContext(id);
	}
	
	public static ThreadLocalContext get(String id) {
		if(contexts.get(id) != null) {
			return contexts.get(id);
		}
		return null;
	}

	private static class DefaultContext implements ThreadLocalContext {
		
		private String id;
		private static final ThreadLocal<Map<String,Object>> contextMap = new ThreadLocal<>();
		
		public DefaultContext(String id) {
			super();
			this.id = id;
		}

		@Override
		public <T> T get(String key) {
			// TODO Auto-generated method stub
			return (T) contextMap.get().get(getCompleteKey(key));
		}

		@Override
		public void set(String key, Object value) {
			contextMap.get().put(getCompleteKey(key), value);
		}
		
		private String getCompleteKey(String key) {
			return getPrefix() + key;
		}

		@Override
		public String getID() {
			// TODO Auto-generated method stub
			return id;
		}
		
		private String getPrefix() {
			return id + "_";
		}

		@Override
		public void destroy() {
			Map<String,Object> dataMap = contextMap.get();
			for(String key : dataMap.keySet()) {
				if(key.startsWith(getPrefix())) {
					dataMap.remove(key);
				}
			}
			
		}
		
	}
}
