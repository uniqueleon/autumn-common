package org.aztec.autumn.common.utils.compress;

import java.nio.ByteBuffer;

public interface CodeUnit {
	
	public byte[] toBytes();
	public CodeUnit read(ByteBuffer buffer);

}
