package org.aztec.autumn.common.utils.compress;

import java.io.File;

import org.apache.commons.lang.StringUtils;

public class CodingConfigure {

	private int[] modulars;
	private Integer bufferSize;
	private Integer statusRange;
	private Integer dictionarySize;
	private Integer lookaheadBufferSize;
	private Integer leastBufferSize = 3;
	private Integer product;
	private byte[] algorithmInfos = 
			new byte[] {CompressConstant.DEFUALT_COMPRESS_ALGORITHM_INDEX,
					CompressConstant.DEFAULT_COMPRESS_ALGORITHM_VERSION,
					1};
	private String charset;
	private File targetFile;
	private File sourceFile;
	private String sessionId;
	private Integer workThreadNum;
	private CodingProgress progress;
	private String compressType;
	private Integer bitCount;
	private String congruenceSequence = "";
	
	public String getDefaultCongruenceSequence() {
		if(StringUtils.isNotBlank(congruenceSequence)) {
			return congruenceSequence;
		}
		StringBuilder builder = new StringBuilder();
		int roundBase = statusRange / (product + 1);
		int roundCursor = 0;
		for(int i = 0;i < statusRange;) {
			if(i < product) {
				if(builder.length() != 0)
					builder.append(",");
				builder.append("" + i);
				i++;
				continue;
			}
			else if(i == product){
				builder.append("," + (statusRange - 1));
				i++;
				continue;
			}
			else {
				for(int j = roundBase;j > 0;j--) {
					builder.append("," + (j * product + roundCursor));
					i++;
				}
				roundCursor ++;
				continue;
			}
		}
		
		return builder.toString();
	}
	
	public int[] getModulars() {
		return modulars;
	}
	public void setModulars(int[] modulars) {
		this.modulars = modulars;
	}
	public Integer getStatusRange() {
		return statusRange;
	}
	public void setStatusRange(Integer statusRange) {
		this.statusRange = statusRange;
	}
	public Integer getDictionarySize() {
		return dictionarySize;
	}
	public void setDictionarySize(Integer dictionarySize) {
		this.dictionarySize = dictionarySize;
	}
	public Integer getLookaheadBufferSize() {
		return lookaheadBufferSize;
	}
	public void setLookaheadBufferSize(Integer lookaheadBufferSize) {
		this.lookaheadBufferSize = lookaheadBufferSize;
	}
	
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public File getTargetFile() {
		return targetFile;
	}
	public void setTargetFile(File targetFile) {
		this.targetFile = targetFile;
	}
	
	public Integer getLeastBufferSize() {
		return leastBufferSize;
	}
	public void setLeastBufferSize(Integer leastBufferSize) {
		this.leastBufferSize = leastBufferSize;
	}
	
	
	public Integer getWorkThreadNum() {
		return workThreadNum;
	}
	public void setWorkThreadNum(Integer workThreadNum) {
		this.workThreadNum = workThreadNum;
	}
	
	public Integer getBufferSize() {
		return bufferSize;
	}
	public void setBufferSize(Integer bufferSize) {
		this.bufferSize = bufferSize;
	}
	
	public File getSourceFile() {
		return sourceFile;
	}
	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}
	
	public CodingProgress getProgress() {
		return progress;
	}
	public void setProgress(CodingProgress progress) {
		this.progress = progress;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getCompressType() {
		return compressType;
	}
	public void setCompressType(String compressType) {
		this.compressType = compressType;
	}
	public int getProduct() {
		
		return product;
	}
	
	public byte[] getAlgorithmInfos() {
		return algorithmInfos;
	}

	public void setAlgorithmInfos(byte[] algorithmInfos) {
		this.algorithmInfos = algorithmInfos;
	}

	public CodingConfigure(String sessionId,int[] modulars, Integer statusRange, 
			Integer workingBufferSize,Integer workingThread,String charset,String sourceFile,String targetFile) {
		super();
		this.sessionId = sessionId;
		this.modulars = modulars;
		this.bufferSize = workingBufferSize;
		this.statusRange = statusRange;
		this.charset = charset;
		this.sourceFile = new File(sourceFile);
		this.targetFile = new File(targetFile);
		this.workThreadNum = workingThread;
		this.bitCount = 0;
		this.product = 1;
		if(modulars != null) {
			for(int modular : modulars) {
				product *= modular;
			}
		}
		while(Math.pow(2, bitCount) < product) {
			bitCount ++;
		}
		congruenceSequence = getDefaultCongruenceSequence();
	}
	
	public CodingConfigure(Integer dictionarySize, Integer lookaheadBufferSize,
			String charset,String targetFile) {
		super();
		this.modulars = modulars;
		this.statusRange = statusRange;
		this.dictionarySize = dictionarySize;
		this.lookaheadBufferSize = lookaheadBufferSize;
		this.charset = charset;
		this.targetFile = new File(targetFile);
	}
	public String getCongruenceSequence() {
		return congruenceSequence;
	}
	public void setCongruenceSequence(String congruenceSequence) {
		this.congruenceSequence = congruenceSequence;
	}
	public Integer getBitCount() {
		return bitCount;
	}
	public void setBitCount(Integer bitCount) {
		this.bitCount = bitCount;
	}
	
	
}
