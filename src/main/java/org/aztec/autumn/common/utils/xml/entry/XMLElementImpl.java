package org.aztec.autumn.common.utils.xml.entry;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.constant.CommonConst;
import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.ReflectionUtil;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.autumn.common.utils.xml.XMLParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLElementImpl implements XMLElement {

	private List<XMLElement> childs;
	private String innerText;
	private String nodeValue;
	private String tagName;
	private Map<String,String> attributes;

	
	public XMLElementImpl(Document document) {
		init(document.getDocumentElement());
	}
	
	public XMLElementImpl(Node ele) {
		init(ele);
	}
	
	private void init(Node node){
		this.tagName = node.getNodeName();
		innerText = node.getTextContent().trim();
		attributes = new HashMap<>();
		NamedNodeMap attrs = node.getAttributes();
		if(attrs != null){
			for(int i = 0;i < attrs.getLength();i++){
				Node attrNode = attrs.item(i);
				attributes.put(attrNode.getNodeName(), attrNode.getNodeValue());
			}
		}
		childs = new ArrayList<>();
		if(node.hasChildNodes()){
			NodeList childNodes = node.getChildNodes();
			for(int i = 0;i < childNodes.getLength();i++){
				if(!node.getNodeName().startsWith("#")){
					childs.add(new XMLElementImpl(childNodes.item(i)));
				}
			}
		}
	}

	@Override
	public String getInnerText() {
		return innerText;
	}

	@Override
	public String getNodeValue() {
		return nodeValue;
	}

	@Override
	public List<XMLElement> getChilds(String expr) {
		if(expr == null || expr.isEmpty())
			return new ArrayList<>();
		int index = expr.indexOf(CommonConst.XML_ELEMENT_SEPERATE_CHAR);
		String childName = expr;
		List<XMLElement> retElements = new ArrayList<>();
		if(index != -1)
			childName = expr.substring(0,expr.indexOf(CommonConst.XML_ELEMENT_SEPERATE_CHAR));
		for(XMLElement childElement : childs){
			if(childElement.getTagName().equals(childName)){
				if(index == -1)
					retElements.add(childElement);
				else
					retElements.addAll(childElement.getChilds(expr.substring(index + 1)));
			}
		}
		return retElements;
	}

	@Override
	public String getAttribute(String attrName) {
		return attributes.get(attrName);
	}

	@Override
	public String getTagName() {
		return tagName;
	}

	@Override
	public <T> T toBean(Class<T> beanClass) throws XMLParseException {
		try {
			ReflectionUtil reflectUtil = UtilsFactory.getInstance().getReflectUtil();
			T bean = reflectUtil.newInstanceUseMap(beanClass, reflectUtil.getPropertiesMap(beanClass, attributes), false);
			if(childs.size() > 0){
				Map<String,List> childGroups = new HashMap<>();
				for(XMLElement element : childs){
					//if(element.getAttribute(attrName))
				}
			}
			return bean;
		} catch (Exception e) {
			throw new XMLParseException(e.getMessage(), e);
		}
	}

}
