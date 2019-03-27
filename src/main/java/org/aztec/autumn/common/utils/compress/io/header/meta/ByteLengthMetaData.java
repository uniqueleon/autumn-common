package org.aztec.autumn.common.utils.compress.io.header.meta;

import org.aztec.autumn.common.utils.compress.CodingMetaData;

public class ByteLengthMetaData extends BaseMetaData implements CodingMetaData {


	@Override
	public Integer getLength() {
		return 1;
	}

	@Override
	public Integer getSequenceNo() {
		return 0;
	}

}
