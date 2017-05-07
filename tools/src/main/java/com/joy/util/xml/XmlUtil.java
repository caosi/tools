package com.joy.util.xml;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtil {

	private XmlUtil() {
	}
	/**
	 * 获取xml文档
	 * @param xmlFile
	 * @return
	 * @throws Exception
	 */
	public static Document load(String xmlFile) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		File file = new File(xmlFile);
		
		return db.parse(file);
	}
	/**
	 * 获取xml文档
	 * @param xmlFile
	 * @return
	 * @throws Exception
	 */
	public static Document load(InputStream is) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		return db.parse(is);
	}
	/**
	 * 获取某节点下所有子节点
	 * @param node
	 * @param tagName
	 * @return
	 */
	public static List<Element> childElements(Node node,String tagName){
		NodeList nodeList = node.getChildNodes();
		int len = nodeList.getLength();
		List<Element> children = new ArrayList<>(len);
		for(int i=0;i<len ;i++){
			Node child = nodeList.item(i);
			if(child.getNodeType()==Node.ELEMENT_NODE){
				children.add((Element)child);
			}
		}
		return children;
	}

}
