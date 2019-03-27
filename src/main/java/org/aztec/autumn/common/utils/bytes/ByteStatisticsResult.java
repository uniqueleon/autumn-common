package org.aztec.autumn.common.utils.bytes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class ByteStatisticsResult {
	
	private Long specialByteNumber;
	private List<ByteCountInfo> countingInfo = Lists.newArrayList();
	private Map<Byte,Integer> indexes = new HashMap<Byte, Integer>();

	public ByteStatisticsResult() {
		// TODO Auto-generated constructor stub
	}

	public Long getSpecialByteNumber() {
		return specialByteNumber;
	}

	public void setSpecialByteNumber(Long specialByteNumber) {
		this.specialByteNumber = specialByteNumber;
	}

	public List<ByteCountInfo> getCountingInfo() {
		return countingInfo;
	}

	public void setCountingInfo(List<ByteCountInfo> countingInfo) {
		this.countingInfo = countingInfo;
	}

	public Map<Byte, Integer> getIndexes() {
		return indexes;
	}

	public void setIndexes(Map<Byte, Integer> indexes) {
		this.indexes = indexes;
	}
	
	public void append(ByteCountInfo countInfo) {
		countingInfo.add(countInfo);
		int indexSize = indexes.size();
		indexes.put(countInfo.getBite(), indexSize);
	}

	@Override
	public String toString() {
		return "ByteCountingResult [specialByteNumber=" + specialByteNumber + ", countingInfo=" + countingInfo
				+ ", indexes=" + indexes + "]";
	}
	

}
