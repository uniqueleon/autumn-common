package org.aztec.autumn.common.utils.bytes;

import java.io.File;

public class ByteAnalyzeConfig {
	
	private int threadNum;
	private File dataFile;
	private byte[] data;
	private int bufferSize = 1024 * 1024;
	private int maxBufferSize = 1024 * 1024 * 1024;
	private int backward = 0;
	private int forward = 1;

	public ByteAnalyzeConfig() {
		// TODO Auto-generated constructor stub
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getMaxBufferSize() {
		return maxBufferSize;
	}

	public void setMaxBufferSize(int maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
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
	
	

}
