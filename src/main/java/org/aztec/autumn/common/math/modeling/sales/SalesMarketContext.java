package org.aztec.autumn.common.math.modeling.sales;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SalesMarketContext {
	
	private static final Map<String,Object> context = new ConcurrentHashMap<>();
	
	public static interface ContextKey{
		public static final String INITIAL_CUSTOMER_NUM = "initCustomerNum";
		//public static final String CUSTOMER_
	}

	public SalesMarketContext() {
		// TODO Auto-generated constructor stub
	}

	public static void put(String key,Object value) {
		context.put(key, value);
	}
	
	public static <T> T get(String key) {
		return (T) context.get(key);
	}
}
