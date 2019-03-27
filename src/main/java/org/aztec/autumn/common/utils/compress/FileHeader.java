package org.aztec.autumn.common.utils.compress;

import java.io.File;
import java.util.List;

public interface FileHeader {

	public String getName();
	public List<CodingMetaData> getSectors();
	public Integer getLength();
	public void parse(File file) throws CompressException;
	public void parse(byte[] bytes) throws CompressException;
	public boolean hasWritten() ;
	public void write() throws CompressException;
}
