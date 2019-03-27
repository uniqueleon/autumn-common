package org.aztec.autumn.common.utils.bytes;

import java.util.List;
import java.util.Map;

import com.beust.jcommander.internal.Maps;

public class ByteCountInfo{
	private byte bite;
	private boolean isSpecial;
	private long frequency;
	private Map<Integer,List<ByteNeighborInfo>> l_neighborhoods = Maps.newHashMap();
	private Map<Integer,List<ByteNeighborInfo>> r_neighborhoods = Maps.newHashMap();
	public byte getBite() {
		return bite;
	}
	public void setBite(byte bite) {
		this.bite = bite;
	}
	public boolean isSpecial() {
		return isSpecial;
	}
	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}
	public long getFrequency() {
		return frequency;
	}
	public void setFrequency(long frequency) {
		this.frequency = frequency;
	}
	
	public Map<Integer, List<ByteNeighborInfo>> getL_neighborhoods() {
		return l_neighborhoods;
	}
	public void setL_neighborhoods(Map<Integer, List<ByteNeighborInfo>> l_neighborhoods) {
		this.l_neighborhoods = l_neighborhoods;
	}
	public Map<Integer, List<ByteNeighborInfo>> getR_neighborhoods() {
		return r_neighborhoods;
	}
	public void setR_neighborhoods(Map<Integer, List<ByteNeighborInfo>> r_neighborhoods) {
		this.r_neighborhoods = r_neighborhoods;
	}
	public ByteCountInfo(byte bite, boolean isSpecial, long frequency) {
		super();
		this.bite = bite;
		this.isSpecial = isSpecial;
		this.frequency = frequency;
	}
	
	public void update(ByteCountInfo updateData) {
		this.frequency = this.frequency + updateData.frequency;
		
	}
	@Override
	public String toString() {
		return "ByteCountInfo [bite=" + bite + ", isSpecial=" + isSpecial + ", frequency=" + frequency
				+ ", l_neighborhoods=" + l_neighborhoods + ", r_neighborhoods=" + r_neighborhoods + "]";
	}
	
	
}
