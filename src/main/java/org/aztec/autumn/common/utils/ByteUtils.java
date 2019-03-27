package org.aztec.autumn.common.utils;

import java.util.BitSet;
import java.util.List;

import com.beust.jcommander.internal.Lists;

public class ByteUtils {

	public static byte mix(byte lowByte,byte highbyte) {
		Integer byteValue = lowByte & 15;
		byteValue += (highbyte << 4);
		return byteValue.byteValue();
	}
	
	public static byte[] split(byte rawByte) {
		Integer lowByte = rawByte & 15;
		Integer highByte = (rawByte & 240) >> 4;
		return new byte[] {lowByte.byteValue(),highByte.byteValue()};
	}
	
	public static byte booleansToByte(List<Boolean> flags) {
		byte retByte = 0;
		for(int i = 0 ;i < 8;i++) {
			retByte += (flags.get(i) ? 1 : 0) << i;
		}
		return retByte;
	}
	
	public static byte[] integersToByte(List<Integer> intDatas,int minLength) {
		BitSet bs = new BitSet();
		for(Integer intData : intDatas) {
			bs.set(intData);
		}
		byte[] retByte = new byte[minLength];
		byte[] bsBytes = bs.toByteArray();
		for(int i = 0;i < bsBytes.length;i++) {
			retByte[i] = bsBytes[i];
		}
		return retByte;
	}
	
	
	public static void main(String[] args) {

		/*byte mixByte = mix((byte)4,(byte)6);
		System.out.println(mixByte);
		byte[] splitByte = split(mixByte);
		System.out.println(splitByte[0] + "-" + splitByte[1]);
		*/
		List<Integer> testData = Lists.newArrayList();

		testData.add(0);
		testData.add(4);
		testData.add(7);
		testData.add(15);
		byte[] retBytes = integersToByte(testData, 2);
		for(int i = 0;i < retBytes.length;i++) {
			System.out.println(retBytes[i]);
		}
	}
}
