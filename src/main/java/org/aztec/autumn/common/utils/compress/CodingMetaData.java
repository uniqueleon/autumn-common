package org.aztec.autumn.common.utils.compress;

public interface CodingMetaData {


	public Integer getSequenceNo();
	public byte[] getContent();
	public void setContent(byte[] content) throws CompressException;
	public Integer getLength();
}
