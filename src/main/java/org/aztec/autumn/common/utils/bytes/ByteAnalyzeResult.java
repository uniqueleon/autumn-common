package org.aztec.autumn.common.utils.bytes;

import java.util.List;

public class ByteAnalyzeResult {
	
	public ByteStatisticsResult countResult;
	
	private List<ByteCountInfo> frequenceList;

	public List<ByteCountInfo> getFrequenceList() {
		return frequenceList;
	}

	public void setFrequenceList(List<ByteCountInfo> frequenceList) {
		this.frequenceList = frequenceList;
	}

	public ByteAnalyzeResult() {
		// TODO Auto-generated constructor stub
	}

}
