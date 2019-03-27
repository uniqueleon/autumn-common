package org.aztec.autumn.common.utils.compress.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.aztec.autumn.common.utils.compress.CodingConfigure;
import org.aztec.autumn.common.utils.compress.CodingMetaData;
import org.aztec.autumn.common.utils.compress.CompressException;
import org.aztec.autumn.common.utils.compress.code.BaseCodeHeader;
import org.aztec.autumn.common.utils.compress.io.header.meta.ByteDictionary;

import com.beust.jcommander.internal.Lists;

/**
 * 同余排序表文件.
 * 将排名前十五的分到 0~15 
 * 16,17,18,19,20,21
 * 
 * 
 * @author 10064513
 *
 */
public class CongruenceSortTableFile extends BaseCodeFile{
	
	private long[] counts;
	private List<Long> frequences;
	private long length;
	private final static int baseMod = 16;
	
	public static void main(String[] args) {
		
	}

	public CongruenceSortTableFile(CodingConfigure config,byte[] codes,long[] counts) throws IOException, CompressException {
		super(new SortTableHeader(codes),counts.length);
		this.counts = counts;
		init();
	}
	
	public List<Long> getFrequences() {
		return frequences;
	}

	public void setFrequences(List<Long> frequeces) {
		this.frequences = frequeces;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	protected void init() {
		frequences = Lists.newArrayList();
		length = 0l;
		for(int i = 0;i < counts.length;i++) {
			if(i < baseMod 
					//|| i == 255
					) {
				frequences.add(counts[i]);
			}
			else if(i < 256){
				int index = (i / baseMod) - 1;
				//if(frequec)
				System.out.println(i + ">>" + index);
				frequences.set(index, frequences.get(index) + counts[i]);
			}
			length += counts[i];
		}
		Long totalFrequence = 0l;
		for(Long frequence : frequences) {
			totalFrequence += frequence;
		}
	}
	
	public byte[] splitBytes(byte rawByte) {
		SortTableHeader sHeader = (SortTableHeader) header;
		ByteDictionary bd = (ByteDictionary) header.getSectors().get(0);
		byte newByte = bd.transfer(rawByte);
		if(newByte < 0) {
			newByte += 128;
		}
		int byteValue = (int ) newByte;
		byte[] retBytes = new byte[2];
		retBytes[1] = (byte)(byteValue / baseMod);
		retBytes[0] = (byte)(byteValue % baseMod);
		return retBytes;
	}

	@Override
	public boolean isTemp() {
		return true;
	}
	
	public boolean isVirtual() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return "SortTable";
	}
	
	public static class SortTableHeader extends BaseCodeHeader{
		
		List<CodingMetaData> metaDatas = Lists.newArrayList();

		public SortTableHeader(byte[] contents) {
			super(contents);
			metaDatas.add(new ByteDictionary(contents));
		}

		@Override
		public List<CodingMetaData> getSectors() {
			return metaDatas;
		}

		@Override
		public String getName() {
			return "SortTable";
		}
		
		

	}

	@Override
	protected File getTargetFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getSessionID() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
