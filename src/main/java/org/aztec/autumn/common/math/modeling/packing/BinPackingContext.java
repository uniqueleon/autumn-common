package org.aztec.autumn.common.math.modeling.packing;

public interface BinPackingContext {

	public <T> T get(String key);
	public void put(String key,Object value);
	
	public static class ContextKeys{
		public static final String SORT_EDGES = "sortedEdges";
	}
	
	
}
