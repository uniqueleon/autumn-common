package org.aztec.autumn.common.math.modeling.packing;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

public class AlgorithmContextFactory {

	public static ThreadLocal<String> bizIds = new ThreadLocal<String>();
	public static Map<String,AlgorithmContext> contexts = Maps.newConcurrentMap();
	
	public static interface ContextKeys{
		public static final String ITEM_NUMS = "itemNums";
	}
	
	public static void initContext(){
		String key = UUID.randomUUID().toString();
		bizIds.set(key);
	}
	
	public static AlgorithmContext getContext(){
		if(bizIds.get() == null){
			return null;
		}
		AlgorithmContext context =  contexts.get(bizIds.get());
		if(context == null){
			context = new ThreadLocalContext();
			contexts.put(bizIds.get(), context);
		}
		return context;
	}
	
	public static void clearContext(){
		if(bizIds.get() != null){
			AlgorithmContext ac = contexts.get(bizIds.get());
			ac.clear();
			bizIds.set(null);
		}
	}
	
	private static class ThreadLocalContext implements AlgorithmContext{
		
		private Map<String,Object> dataMap = Maps.newHashMap();

		@Override
		public <T> T get(String key) {
			// TODO Auto-generated method stub
			return (T) dataMap.get(key);
		}

		@Override
		public void put(String key, Object object) {
			// TODO Auto-generated method stub
			dataMap.put(key, object);
		}

		@Override
		public void clear() {
			dataMap.clear();
		}
		
	}
}
