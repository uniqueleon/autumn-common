package org.aztec.autumn.common.utils;

import java.util.BitSet;
import java.util.List;

import org.junit.Test;

import com.beust.jcommander.internal.Lists;

public class StringUtilsTest {

	@Test
	public void test() {
		
		List<Integer> hashCodes = Lists.newArrayList();
		int strLength = 100;
		int testSize = 10;
		for(int i = 0;i < testSize ;i ++) {
			String randStr = StringUtils.getRamdonNumberString(strLength);
			hashCodes.add(randStr.hashCode());
		}
		BitSet bs = new BitSet();
		for(Integer bitIndex : hashCodes) {
			bs.set(Integer.parseInt(bitIndex.toUnsignedString(bitIndex)));;
		}
		String bsStr = BitSetUtil.toString(bs, 1);
		System.out.println(bsStr.length());
	}
	
	

}
