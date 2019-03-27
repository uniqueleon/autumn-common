package org.aztec.autumn.common.utils.xml;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.aztec.autumn.common.constant.CommonConst;
import org.aztec.autumn.common.utils.ReflectionUtil;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.autumn.common.utils.xml.entry.XMLElement;
import org.aztec.autumn.common.utils.xml.entry.XMLElementImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.aztec.autumn.common.utils.ClassUtils;

public class BaseXMLReader implements XMLReader {

	private String configPath;
	private File configFile;
	private InputStream inStream;
	private List<XMLElement> rootElements;

	public BaseXMLReader(String configPath) {

		this.configPath = configPath;
	}

	public BaseXMLReader(File configFile) {
		this.configFile = configFile;
	}

	public BaseXMLReader(InputStream is) {
		this.inStream = is;
	}

	@Override
	public void setConfigPath(String path) throws XMLException {
		// TODO Auto-generated method stub
		this.configPath = path;
	}

	@Override
	public void loadData() throws XMLException {
		// TODO Auto-generated method stub
		try {
			rootElements = new ArrayList<>();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = null;
			if (configPath != null)
				doc = builder.parse(new File(configPath));
			else if (configFile != null)
				doc = builder.parse(configFile);
			else if (inStream != null)
				doc = builder.parse(inStream);
			Node rootNode = doc.getDocumentElement();
			rootElements.add(new XMLElementImpl(doc));
			while (rootNode.getNextSibling() != null) {
				System.out.println(rootNode.getNodeName());
			}
		} catch (Exception e) {
			throw new XMLException(e.getMessage(), e);
		}
		// rootElement = new XMLElementImpl(doc);
	}

	@Override
	public void reload() throws XMLException {
		// TODO Auto-generated method stub
		loadData();
	}

	@Override
	public List<XMLElement> getElements(String fullPath) {
		// TODO Auto-generated method stub
		// List<>
		// return rootElement.getChilds(fullPath);
		List<XMLElement> retElements = new ArrayList<>();
		int rootIndex = fullPath.indexOf(CommonConst.XML_ELEMENT_SEPERATE_CHAR);
		if (rootIndex == -1) {
			for (XMLElement rootElement : rootElements) {
				if (rootElement.getTagName().equals(fullPath))
					retElements.add(rootElement);
			}
		} else {
			String rootName = fullPath.substring(0, rootIndex);
			for (XMLElement rootElement : rootElements) {
				if (rootElement.getTagName().equals(rootName)) {
					retElements.addAll(rootElement.getChilds(fullPath.substring(rootIndex + 1)));
				}
			}
		}
		return retElements;
	}

	@Override
	public <T> T get(String fullPath, Integer index, Class<T> retType) throws XMLException {
		try {
			List<XMLElement> foundElements = getElements(fullPath);
			return getValue(retType, foundElements.get(index).getInnerText());
		} catch (Exception e) {
			throw new XMLException(e.getMessage(), e);
		}
	}

	@Override
	public <T> T getAttr(String fullPath, String attrName, Integer index, Class<T> retType) throws XMLException {
		try {
			List<XMLElement> foundElements = getElements(fullPath);
			return getValue(retType, foundElements.get(index).getAttribute(attrName));
		} catch (Exception e) {
			throw new XMLException(e.getMessage(), e);
		}
	}

	private <T> T getValue(Class<T> retType, String value) throws NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (ClassUtils.isPrimitiveWrapper(retType) || retType.equals(String.class)) {
			ReflectionUtil reflectUtil = UtilsFactory.getInstance().getReflectUtil();
			return reflectUtil.newPrimitiveInstance(retType, value);
		} else {
			throw new IllegalArgumentException("Only support wrapper type");
		}
	}

	@Override
	public <T> T getAsBean(String fullPath, Integer index, Class<T> beanType) throws XMLParseException {
		List<XMLElement> foundElements = getElements(fullPath);

		return foundElements.get(index).toBean(beanType);
	}

}
