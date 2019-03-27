package org.aztec.autumn.common.utils.xml;

import java.io.File;
import java.io.InputStream;

public interface XMLReaderFactory {

	public XMLReader getReader(String configPath) throws Exception;
	public XMLReader getReader(File configFile) throws Exception;
	public XMLReader getReader(byte[] content) throws Exception;
	public XMLReader getReader(InputStream in) throws Exception;
	
}
