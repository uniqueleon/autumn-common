package org.aztec.autumn.common.utils.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class XMLReaderFactoryImpl implements XMLReaderFactory {

	@Override
	public XMLReader getReader(String configPath) throws ParserConfigurationException, SAXException, IOException {
		return new BaseXMLReader(configPath);
	}

	@Override
	public XMLReader getReader(File configFile) throws Exception {
		return new BaseXMLReader(configFile);
	}

	@Override
	public XMLReader getReader(byte[] content) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(content);
		return new BaseXMLReader(bais);
	}

	@Override
	public XMLReader getReader(InputStream in) throws Exception {
		// TODO Auto-generated method stub
		return new BaseXMLReader(in);
	}

}
