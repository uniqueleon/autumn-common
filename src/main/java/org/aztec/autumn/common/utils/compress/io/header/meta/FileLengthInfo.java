package org.aztec.autumn.common.utils.compress.io.header.meta;

import org.aztec.autumn.common.utils.compress.CodingMetaData;
import org.aztec.autumn.common.utils.compress.CompressException;
import org.aztec.autumn.common.utils.compress.util.ByteUtils;


public class FileLengthInfo extends BaseMetaData implements CodingMetaData {

	public FileLengthInfo(Long length) throws CompressException {
		this.setContent(ByteUtils.longToByte(length));
	}

	@Override
	public Integer getSequenceNo() {
		// TODO Auto-generated method stub
		return 1;
	}


}
