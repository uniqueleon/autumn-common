package org.aztec.autumn.common.utils.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.aztec.autumn.common.GlobalConst;
import org.dom4j.dom.DOMElement;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class XMLUtilsImpl implements XMLUtils {

	public XMLUtilsImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toXML(Map xmlDatas) throws SAXException, IOException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream(GlobalConst.MAX_BUFFER_SIZE);
		XMLWriter writer = new XMLWriter(baos);
		writer.startDocument();
		writeXML(xmlDatas, writer,null);
		writer.endDocument();
		baos.flush();
		baos.close();
		return baos.toString("UTF-8");
	}
	
	private void writeXML(Map<String,Object> xmlDatas,XMLWriter writer,DOMElement rootElement) throws IOException, SAXException{
		if(xmlDatas != null && xmlDatas.size() > 0){
			for(String key : xmlDatas.keySet()){

				Object xmlData = xmlDatas.get(key);
				if(xmlData instanceof Map){
					//writer.startEntity(key);
					//Node node = new DOm

					DOMElement element = new DOMElement(key);
					//writer.write(element);
					writeXML((Map)xmlData, writer,element);
				}
				else{
					
					DOMElement element = new DOMElement(key);
					if(xmlData instanceof String){
						element.setText((String)xmlDatas.get(key));
						//element.setText("\\<!CDATA[" + xmlDatas.get(key) + "]\\>");
						//element.setTextContent("<!CDATA[" + xmlDatas.get(key) + "]>");
					}
					else{
						element.setText("" + xmlDatas.get(key));
					}
					if(rootElement != null){
						rootElement.add(element);
					}
				}
			}

			if(rootElement != null)writer.write(rootElement);
		}
	}
	

	//@Override
	public Map<String, Object> toMap(String xmlData) throws SAXException, IOException, ParserConfigurationException {
		// TODO Auto-generated method stub
		ByteArrayInputStream inStream = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(inStream);
		Element rootElement = document.getDocumentElement();
		
		return null;
	}
	

	public <T> T xml2Object(String xmlStr, Class<T> c) {
		try {
			JAXBContext context = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			T t = (T) unmarshaller.unmarshal(new StringReader(xmlStr));

			return t;

		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * @param object
	 *            对象
	 * @return 返回xmlStr
	 */
	public String object2Xml(Object object) {
		try {
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			Marshaller marshal = context.createMarshaller();

			marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // 格式化输出
			marshal.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式,默认为utf-8
			marshal.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xml头信息
			marshal.setProperty("jaxb.encoding", "utf-8");
			marshal.marshal(object, writer);

			return new String(writer.getBuffer());

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
