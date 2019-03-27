package org.aztec.autumn.common.utils.bytes;

public class AnalyzerConfig {

	public byte target;
	public byte[] l_Neighborhood;
	public byte[] r_Neighborhood;
	private ByteStatisticsResult workingData;
	private int backward;
	private int forward;
	
	public byte getTarget() {
		return target;
	}
	
	public byte[] getL_Neighborhood() {
		return l_Neighborhood;
	}

	public void setL_Neighborhood(byte[] l_Neighborhood) {
		this.l_Neighborhood = l_Neighborhood;
	}

	public byte[] getR_Neighborhood() {
		return r_Neighborhood;
	}

	public void setR_Neighborhood(byte[] r_Neighborhood) {
		this.r_Neighborhood = r_Neighborhood;
	}

	public void setTarget(byte target) {
		this.target = target;
	}
	public ByteStatisticsResult getWorkingData() {
		return workingData;
	}
	public void setWorkingData(ByteStatisticsResult workingData) {
		this.workingData = workingData;
	}

	public int getBackward() {
		return backward;
	}
	public void setBackward(int backward) {
		this.backward = backward;
	}
	public int getForward() {
		return forward;
	}
	public void setForward(int forward) {
		this.forward = forward;
	}
	
	public AnalyzerConfig(ByteStatisticsResult result,int cursor,byte[] readData,int backward,int forward) {
		this.workingData = result;
		target = readData[cursor];
		int r_size = readData.length - cursor > forward ? forward : readData.length - cursor;
		int l_size = cursor - 0 > backward ? backward : cursor - 0;
		l_Neighborhood = new byte[l_size];
		r_Neighborhood = new byte[r_size];
		for(int i = 0;i < r_size;i++) {
			r_Neighborhood[i] = readData[i + cursor + 1];
		}
		for(int i = 0;i < l_size;i++) {
			l_Neighborhood[i] = readData[cursor - i - 1];
		}
		this.backward = backward;
		this.forward = forward;
	}
}
