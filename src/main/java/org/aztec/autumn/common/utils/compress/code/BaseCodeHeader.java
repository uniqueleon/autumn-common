package org.aztec.autumn.common.utils.compress.code;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.utils.compress.CodingMetaData;
import org.aztec.autumn.common.utils.compress.CompressException;
import org.aztec.autumn.common.utils.compress.CompressException.ErrorCode;
import org.aztec.autumn.common.utils.compress.FileHeader;

import com.google.common.collect.Maps;

public abstract class BaseCodeHeader implements FileHeader{
	
	protected byte[] contents;
	protected int length;
	protected int offset;
	protected String name;
	protected File outputFile;
	protected boolean hasWritten = false;

	protected void read(File file) throws IOException {
		contents = new byte[length];
		FileInputStream fis = new FileInputStream(file);
		fis.read(contents);
	}
	
	protected  Map<String,HeaderMetaData> getHeaderMetaData(byte[] contents) throws CompressException{
		return Maps.newHashMap();
	};
	
	public static class HeaderMetaData{
		private Integer length;
		private Integer offset;
		public Integer getLength() {
			return length;
		}
		public void setLength(Integer length) {
			this.length = length;
		}
		public Integer getOffset() {
			return offset;
		}
		public void setOffset(Integer offset) {
			this.offset = offset;
		}
		public HeaderMetaData(Integer length, Integer offset) {
			super();
			this.length = length;
			this.offset = offset;
		}
		
	}
	

	public byte[] getContents() {
		return contents;
	}
	public Integer getLength() {
		return length;
	}
	public void setContents(byte[] contents) {
		this.contents = contents;
	}
	public BaseCodeHeader(byte[] contents) {
		super();
		this.contents = contents;
	}

	public BaseCodeHeader() {
		super();
	}

	@Override
	public void parse(File file) throws CompressException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parse(byte[] bytes) throws CompressException {
		this.contents = bytes;
	}

	@Override
	public boolean hasWritten() {
		
		return hasWritten;
	}

	@Override
	public void write() throws CompressException {
		// TODO Auto-generated method stub
		try {
			if(outputFile != null && outputFile.exists()) {
				FileOutputStream fos = new FileOutputStream(outputFile);
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				List<CodingMetaData> metaDatas = getSectors();
				Collections.sort(metaDatas,new Comparator<CodingMetaData>() {

					@Override
					public int compare(CodingMetaData o1, CodingMetaData o2) {
						// TODO Auto-generated method stub
						return o1.getSequenceNo() - o2.getSequenceNo();
					}
				});
				for(int i = 0;i < metaDatas.size();i++) {
					buffer.put(metaDatas.get(i).getContent());
				}
				fos.write(buffer.array());
				fos.flush();
				fos.close();
				hasWritten = true;
			}
		} catch (Exception e) {
			throw new CompressException(ErrorCode.ENCODE_WRITE_HEADER_FAIL);
		}
	}
	
}
