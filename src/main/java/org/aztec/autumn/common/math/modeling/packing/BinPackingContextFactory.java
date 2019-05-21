package org.aztec.autumn.common.math.modeling.packing;

import java.util.HashMap;
import java.util.Map;

public class BinPackingContextFactory {
	
	public static ThreadLocal<BinPackingContext> localContext = new ThreadLocal<>();

	public BinPackingContextFactory() {
		// TODO Auto-generated constructor stub
	}

	public static BinPackingContext build(BinPackingConfig config) {
		if(localContext.get() != null) {
			return localContext.get();
		}
		BinPackingContext context = new ThreadLocalContext();
		localContext.set(context);
		return localContext.get();
	}
	
	public static BinPackingContext getContext() {
		return localContext.get();
	}
	
	public static void destroy() {
		localContext.set(null);
	}
	
	private static class ThreadLocalContext implements BinPackingContext{

		public  Map<String,Object> contextMap = new HashMap<>();
		@Override
		public <T> T get(String key) {
			return (T) contextMap.get(key);
		}

		@Override
		public void put(String key, Object value) {
			contextMap.put(key, value);
		}
		
	}
	
}
