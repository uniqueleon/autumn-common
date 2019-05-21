package org.aztec.autumn.common.math.modeling.packing;

import java.util.List;

import com.google.common.collect.Lists;

public class Edge {
	private Long length;
	private int index;
	public Long getLength() {
		return length;
	}
	public void setLength(Long length) {
		this.length = length;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public Edge(Long length, int index) {
		super();
		this.length = length;
		this.index = index;
	}
	
	public static List<Long> toLongArray(List<List<Edge>> edges){
		
		List<Long> lengths = Lists.newArrayList();
		for(List<Edge> itemEdge : edges){
			for(int i = 0;i < itemEdge.size();i++){
				lengths.add(itemEdge.get(i).getLength());
			}
		}
		return lengths;
	}

}
