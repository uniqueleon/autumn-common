package org.aztec.autumn.common.utils.compress.io.header.meta;

import java.util.Map;

import org.aztec.autumn.common.utils.compress.CodingMetaData;

import com.google.common.collect.Maps;

public class ByteDictionary extends BaseMetaData implements CodingMetaData {
	
	private Map<Integer,Integer> dictMap = Maps.newHashMap();

	public ByteDictionary(byte[] dictionary) {
		this.content = dictionary;
		for(int i = 0;i < dictionary.length;i++) {
			dictMap.put((int) dictionary[i],i);
		}
	}
	
	public byte transfer(byte rawByte) {
		return dictMap.get((int) rawByte).byteValue();
	}

	@Override
	public Integer getSequenceNo() {
		return 0;
	}

	@Override
	public Integer getLength() {
		// 256 * 2 = 512
		return dictMap.size() * 2;
	}
	
	public static void main(String[] args) {
		ByteDictionary bd = new ByteDictionary(new byte[] {(byte) 1, (byte) 7, (byte) 9, (byte) 8, (byte) 4, (byte) 3,
				(byte) - 127,(byte) 128,(byte) 0,(byte) -1,(byte) 129});
		System.out.println(bd.transfer((byte)-127));
		System.out.println(bd.transfer((byte)-1));
		System.out.println(((byte) 130));
	}

	
	
}
