package org.aztec.autumn.common.utils.xml.entry;

import java.util.List;

import org.aztec.autumn.common.utils.xml.XMLParseException;


public interface XMLElement {
	
	public String getInnerText() ;

	public String getNodeValue() ;
	
	public String getTagName();
	
	public <T> T toBean(Class<T> beanClass) throws XMLParseException;

	public List<XMLElement> getChilds(String expr);
	
	public String getAttribute(String attrName);
	
	
}
