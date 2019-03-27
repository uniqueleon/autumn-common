package org.aztec.autumn.common.utils.compress.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.aztec.autumn.common.utils.compress.CodingMetaData;
import org.aztec.autumn.common.utils.compress.CompressException;
import org.aztec.autumn.common.utils.compress.FileHeader;
import org.aztec.autumn.common.utils.compress.code.BaseCodeHeader;
import org.aztec.autumn.common.utils.compress.io.header.meta.ByteCountLength;
import org.aztec.autumn.common.utils.compress.io.header.meta.ByteLengthMetaData;

import com.google.common.collect.Lists;

public class ByteStatisticFile extends BaseCodeFile {

	public ByteStatisticFile(byte[] codes) throws IOException, CompressException {
		super(new ByteStatisticHeader(), codes);
		// TODO Auto-generated constructor stub
		this.virtual = true;
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

	@Override
	public boolean isTemp() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static class ByteStatisticHeader implements FileHeader{
		
		private ByteLengthMetaData lengthMetaData = new ByteLengthMetaData();
		private ByteCountLength countMetaData = new ByteCountLength();

		public static interface HEADER_INDEX {
			public static final int BYTE_LENGTH = 1;
			public static final int BYTE_COUNT_LENGTH = 1;
		}
		
		public ByteStatisticHeader() throws CompressException {
			super();
			lengthMetaData.setContent(new byte[] {(byte)1});
			countMetaData.setContent(new byte[] {(byte)63});
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<CodingMetaData> getSectors() {
			List<CodingMetaData> metaDatas = Lists.newArrayList();
			metaDatas.add(lengthMetaData);
			metaDatas.add(countMetaData);
			return metaDatas;
		}

		@Override
		public Integer getLength() {
			// TODO Auto-generated method stub
			return lengthMetaData.getLength() + countMetaData.getLength();
		}

		@Override
		public void parse(File file) throws CompressException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void parse(byte[] bytes) throws CompressException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean hasWritten() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void write() {
			// TODO Auto-generated method stub
			
		}
		
	}

}
