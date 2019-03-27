package org.aztec.autumn.common.utils.compress.io.header.meta;

import org.aztec.autumn.common.utils.compress.CodingConfigure;
import org.aztec.autumn.common.utils.compress.CodingMetaData;
import org.aztec.autumn.common.utils.compress.CompressConstant;

public class AlgorithmInfo extends BaseMetaData implements CodingMetaData {

	public AlgorithmInfo(byte[] contents) {
		this.content = contents;
	}

	@Override
	public Integer getSequenceNo() {
		// TODO Auto-generated method stub
		return 0;
	}

}
