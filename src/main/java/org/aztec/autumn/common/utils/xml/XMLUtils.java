package org.aztec.autumn.common.utils.xml;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.xml.sax.SAXException;

public interface XMLUtils {

	public String toXML(Map xmlDatas) throws SAXException, IOException ;
}
