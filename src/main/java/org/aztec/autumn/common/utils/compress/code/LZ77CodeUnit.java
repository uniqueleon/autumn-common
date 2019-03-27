package org.aztec.autumn.common.utils.compress.code;

import java.util.BitSet;


public class LZ77CodeUnit extends BaseCode{
	
	private Integer offset;
	private Integer matchLength;

	/*public LZ77Code(char rawChar, Long length,Long frequence) {
		super(rawChar, length,frequence);
		positions = Lists.newArrayList();
	}

	public LZ77Code(Long length, BitSet code,Long frequence) {
		super( code,length,frequence);
		positions = Lists.newArrayList();
	}

	public LZ77Code(String text,Long frequence) {
		super(text,frequence);
		positions = Lists.newArrayList();
	}*/

	public LZ77CodeUnit(byte[] bytes,Integer offset,Integer length) {
		super(bytes, 1l);
		this.offset = offset;
		this.matchLength = length;
	}
	
	public LZ77CodeUnit(Integer offset,Integer length) {
		this.offset = offset;
		this.matchLength = length;
	}

	public Boolean isContains(LZ77CodeUnit otherCode) {
		BitSet testBitSet = BitSet.valueOf(this.code.toByteArray());
		testBitSet.and(otherCode.getCode());
		return testBitSet.equals(otherCode.getCode());
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getMatchLength() {
		return matchLength;
	}

	public void setMatchLength(Integer matchLength) {
		this.matchLength = matchLength;
	}


	
}