package org.aztec.autumn.common.utils.compress.util;

import java.nio.LongBuffer;
import java.util.BitSet;

public class ByteUtils {

	private ByteUtils() {
		// TODO Auto-generated constructor stub
	}

	public static  byte[] longToByte(long longValue) {
		LongBuffer lb = LongBuffer.allocate(1);
		lb.put(longValue);
		
		BitSet bs = BitSet.valueOf(new long[] {longValue});
		byte[] retBytes = new byte[8];
		byte[] readByte = bs.toByteArray();
		for(int i = 0;i < readByte.length;i++) {
			retBytes[i] = readByte[i];
		}
		/*int beginCursor = retBytes.length - readByte.length;
		for(int i = beginCursor;i < retBytes.length; i ++) {
			retBytes[i] = readByte[i - beginCursor];
		}*/
		return retBytes;
	}
	
	public static long bytes2Long(byte[] bytes) {
		BitSet bs = BitSet.valueOf(bytes);
		return bs.toLongArray()[0];
	}
	
	public static void main(String[] args) {
		byte[] bytes = longToByte(1338793737417974l);
		System.out.println(bytes.length);
		System.out.println(bytes2Long(bytes));
		byte[] bytes2 = longToByte(1334l);
		System.out.println(bytes2.length);
	}
}
