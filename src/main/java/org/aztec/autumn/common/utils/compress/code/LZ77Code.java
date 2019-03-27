package org.aztec.autumn.common.utils.compress.code;

import java.util.List;

import org.aztec.autumn.common.utils.compress.CompressException;

import com.beust.jcommander.internal.Lists;

public class LZ77Code {
	
	private Header header;
	private List<LZ77CodeUnit> codes;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public List<LZ77CodeUnit> getCodes() {
		return codes;
	}

	public void setCodes(List<LZ77CodeUnit> codes) {
		this.codes = codes;
	}

	public LZ77Code(Header header) {
		super();
		this.header = header;
	}

	public static class Header{
		private Integer totalLength;

		public Integer getTotalLength() {
			return totalLength;
		}

		public void setTotalLength(Integer totalLength) {
			this.totalLength = totalLength;
		}

		public Header(Integer totalLength) {
			super();
			this.totalLength = totalLength;
		}
		
	}
	
	public List<HuffmanCode> toHuffmanCodes(int codeLimit) throws CompressException {
		List<HuffmanCode> huffmanCodes = Lists.newArrayList();
		for(LZ77CodeUnit code : codes) {
			HuffmanCode huffmanCode = new HuffmanCode(code.getCode(), 1l);
			int searchIndex = huffmanCodes.indexOf(huffmanCode);
			if(searchIndex != -1) {
				BaseCode oldCode = huffmanCodes.get(searchIndex).getOldCode();
				oldCode.setFrequence(oldCode.getFrequence() + 1);
			}
			else {
				huffmanCodes.add(huffmanCode);
				if(huffmanCodes.size() > codeLimit) {
					throw new CompressException(CompressException.ErrorCode.CODE_LIMIT_EXCEED);
				}
			}
		}
		return huffmanCodes;
	}
}
