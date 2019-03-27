package org.aztec.autumn.common.utils.compress.code;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;

public class HuffmanCode extends BaseCode{
	private BaseCode oldCode;
	private BaseCode newCode;
			
	public BaseCode getNewCode() {
		return newCode;
	}
	
	public BaseCode getOldCode() {
		return oldCode;
	}

	public void setOldCode(BaseCode oldCode) {
		this.oldCode = oldCode;
	}

	public void setNewCode(BaseCode newCode) {
		this.newCode = newCode;
	}

	public String getNewCodeAsString() throws UnsupportedEncodingException {
		
		return newCode.getAsString();
	}
	public String getOldCodeAsString() throws UnsupportedEncodingException {
		return oldCode.getAsString();
	}
	public int getOldCodeAsInt() throws UnsupportedEncodingException {
		return oldCode.getAsByteArray().length == 0 ? 0 : oldCode.getAsByteArray()[0];
	}
	public HuffmanCode clone() {
		//HuffmanCode newCode = new HuffmanCode(length.intValue(),bitCode,rawCode,frequence.longValue());
		//return newCode;
		return null;
	}
	public HuffmanCode(Long lenght, BitSet code, BitSet rawCode, Long frequence) {
		oldCode = new BaseCode(rawCode,lenght ,frequence);
		this.newCode = new BaseCode(code,0l ,0l);
	}

	public HuffmanCode(BitSet rawCode, Long frequence) {
		oldCode = new BaseCode(rawCode,0l ,frequence);
		this.newCode = new BaseCode();
	}
	
	public HuffmanCode(String text,Long frequence) {
		this.oldCode = new BaseCode(text, frequence);
		this.newCode = new BaseCode();
	}
	
	public HuffmanCode(char rawChar,Long frequence) {
		oldCode = new BaseCode(rawChar, 8l, frequence);
		this.newCode = new BaseCode();
	}
	
	public HuffmanCode(int code,Long frequence) {

		oldCode = new BaseCode(code, frequence);
		this.newCode = new BaseCode();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((oldCode == null) ? 0 : oldCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		HuffmanCode other = (HuffmanCode) obj;
		if (oldCode == null) {
			if (other.oldCode != null)
				return false;
		} else if (!oldCode.equals(other.oldCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HuffmanCode [oldCode=" + oldCode.getAsBinaryString() + ", newCode=" + newCode.getAsBinaryString() + "]";
	}

	@Override
	public BitSet getCode() {
		BitSet bs = super.getCode();
		BitSet retBs = BitSet.valueOf(new byte[] {0});
		int nextSetIndex = bs.nextSetBit(0);
		while (nextSetIndex != -1) {
			retBs.set((int) (length - nextSetIndex));
			nextSetIndex = bs.nextSetBit(nextSetIndex + 1); 
		}
		return retBs;
	}
	
}