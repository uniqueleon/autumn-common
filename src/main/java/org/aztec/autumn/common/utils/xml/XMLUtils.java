package org.aztec.autumn.common.utils.xml;

import java.io.IOException;
import java.util.Map;

import org.xml.sax.SAXException;

public interface XMLUtils {

	public String toXML(Map xmlDatas) throws SAXException, IOException ;
	public <T> T xml2Object(String xmlStr, Class<T> c);
	public String object2Xml(Object object);
}
