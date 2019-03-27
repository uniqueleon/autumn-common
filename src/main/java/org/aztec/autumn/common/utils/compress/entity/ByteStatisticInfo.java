package org.aztec.autumn.common.utils.compress.entity;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.List;

public class ByteStatisticInfo {
	private byte byteData;
	private Long count;

	public byte getByteData() {
		return byteData;
	}

	public void setByteData(byte byteData) {
		this.byteData = byteData;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public byte[] toBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.put(byteData);
		BitSet bs = BitSet.valueOf(new long[] { count });
		buffer.put(bs.toByteArray());
		return buffer.array();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + byteData;
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
		ByteStatisticInfo other = (ByteStatisticInfo) obj;
		if (byteData != other.byteData)
			return false;
		return true;
	}

	public ByteStatisticInfo(byte byteData, Long count) {
		super();
		this.byteData = byteData;
		this.count = count;
	}

	@Override
	public String toString() {
		return "" + byteData + "," + count + "###";
	}
	

	public static void main(String[] args) {
		
	}
}
