package org.aztec.autumn.common.utils.compress.io.header.meta;

import org.aztec.autumn.common.utils.compress.CodingMetaData;
import org.aztec.autumn.common.utils.compress.CompressException;

public abstract class BaseMetaData implements CodingMetaData {

	protected Integer seqNo;
	protected byte[] content;

	public BaseMetaData() {
		super();
		this.seqNo = getSequenceNo();
	}

	@Override
	public byte[] getContent() {
		// TODO Auto-generated method stub
		return content;
	}

	@Override
	public void setContent(byte[] content) throws CompressException {
		this.content = content;
	}

	@Override
	public Integer getLength() {
		return content == null ? 0 : content.length;
	}



}
