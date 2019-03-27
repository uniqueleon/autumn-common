package org.aztec.autumn.common.utils.compress;

public interface CodeMapper {

	public void init(Object[] params);
	public byte[] encode(byte[] bits);
	public byte[] mappingTable();
	public byte[] decode(byte[] table,byte[] datas);
}
