package org.aztec.autumn.common.utils.compress;

import java.io.File;

public interface CompressCoder {

	public File encode(CodingConfigure config)  throws CompressException;
	public File decode(CodingConfigure config)  throws CompressException;
	
}
