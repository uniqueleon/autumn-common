package org.aztec.autumn.common.utils.bytes;

import java.util.List;

public interface ByteCounter {

	public boolean isSpecial(byte bite);
	public ByteCountInfo count(AnalyzerConfig config);
	public byte map(byte bite,int order);
	public byte[] map(byte[] bytes,List<ByteStatisticsResult> result);
}
