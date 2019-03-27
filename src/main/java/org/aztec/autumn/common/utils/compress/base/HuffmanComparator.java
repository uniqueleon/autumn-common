package org.aztec.autumn.common.utils.compress.base;

import java.util.Comparator;

import org.aztec.autumn.common.utils.compress.base.HuffmanCoder.HuffmanTreeNode;

public class HuffmanComparator implements Comparator<HuffmanTreeNode>{

	@Override
	public int compare(HuffmanTreeNode o1, HuffmanTreeNode o2) {
		return o1.getFrequence() - o2.getFrequence() > 0 ? 1 : -1;
	}
	
}
