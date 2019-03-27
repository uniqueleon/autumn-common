package org.aztec.autumn.common.utils.compress.code;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;

import org.aztec.autumn.common.utils.compress.Code;

public class BaseCode implements Code{

	protected Long length;
	protected BitSet code;
	protected Long frequence;

	public static final long BIT_OF_BYTE = 8l;

	public static final long BIT_OF_WORD = 16l;
	public static final long BIT_OF_UTF_8_WORD = 64l;
	
	
	public String getAsBinaryString() {
		StringBuilder text = new StringBuilder();
		for(int i = 0;i < length;i++) {
			text.append(code.get((int) (length - i)) ? "1" : "0");
		}
		return text.toString();
	}
	
	public String getAsString() throws UnsupportedEncodingException {
		return new String(code.toByteArray(),"UTF-8");
	}
	
	public byte[] getAsByteArray() {
		byte[] retArray = code.toByteArray();
		return retArray.length > 0 ? retArray : new byte[] {0};
	}
	
	public BaseCode() {
		super();
	}

	public BaseCode(BitSet code,Long length,Long frequence) {
		super();
		this.length = length;
		this.code = code;
		this.frequence = frequence;
	}
	
	public BaseCode(String text,Long frequence) {
		byte[] byteArrs = text.getBytes();
		code = BitSet.valueOf(byteArrs);
		this.length = byteArrs.length * BIT_OF_BYTE;
		this.frequence = frequence;
	}
	
	public BaseCode(char rawChar,Long length,Long frequence) {
		code = BitSet.valueOf(new byte[] {(byte)(rawChar)});
		this.length = length;
		this.frequence = frequence;
	}
	
	public BaseCode(int rawCode, Long frequence) {
		code = BitSet.valueOf(new byte[] {(byte)rawCode});
		this.length = 8l;
		this.frequence = frequence;
	}
	
	public BaseCode(byte[] bytes,Long frequence) {
		code = BitSet.valueOf(bytes);
		this.length = bytes.length * BIT_OF_BYTE;
		this.frequence = frequence;
	}
	
	public BaseCode(byte bite,Long length) {
		code = BitSet.valueOf(new byte[] {bite});
		this.length = length;
		this.frequence = 0l;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public BitSet getCode() {
		return code;
	}

	public void setCode(BitSet code) {
		this.code = BitSet.valueOf(code.toByteArray());
	}

	public Long getFrequence() {
		return frequence;
	}

	public void setFrequence(Long frequence) {
		this.frequence = frequence;
	}
	
	public byte[] toByteArray() {
		
		return 
				code.toByteArray();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((length == null) ? 0 : length.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseCode other = (BaseCode) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (length == null) {
			if (other.length != null)
				return false;
		} else if (!length.equals(other.length))
			return false;
		return true;
	}
	
	
}
