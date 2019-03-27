package org.aztec.autumn.common.utils;

import java.util.BitSet;

public class NumberUtils {

	public static byte[] longToByteArray(Long longValue) {
		
		byte[] longBytes = new byte[64];
		BitSet bs = BitSet.valueOf(new long[] {longValue});
		String flagStr = bs.toString().replace("{", "").replace("}", "").trim();
		if(flagStr.isEmpty()) {
			return longBytes;
		}
		else {
			String[] flagsStr = flagStr.split(",");
			for(String sFlag : flagsStr) {
				int flag = Integer.parseInt(sFlag);
				int index = 64;
				while(flag >= 8) {
					flag = flag / 8;
					index --;
				}
				Byte targetByte = longBytes[index];
				// 00000000
				targetByte = (byte) (targetByte | (1 >> flag));
			}
			return longBytes;
		}
	}
	
	public static void main(String[] args) {
		System.out.println();
	}
}
