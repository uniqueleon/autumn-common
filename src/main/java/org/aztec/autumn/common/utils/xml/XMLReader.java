package org.aztec.autumn.common.utils.xml;

import java.util.List;

import org.aztec.autumn.common.utils.xml.entry.XMLElement;

public interface XMLReader {

	public void setConfigPath(String path) throws XMLException;

	public void loadData() throws XMLException;

	public void reload() throws XMLException;

	public <T> T get(String fullPath, Integer index, Class<T> retType)
			throws XMLException;

	public <T> T getAttr(String fullPath, String attrName, Integer index, Class<T> retType)
			throws XMLException;
	
	public <T> T getAsBean(String fullPath, Integer index, Class<T> beanType)
			throws XMLException;

	public List<XMLElement> getElements(String fullPath) throws XMLException;;
}
