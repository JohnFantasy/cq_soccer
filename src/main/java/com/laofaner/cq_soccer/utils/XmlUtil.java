package com.laofaner.cq_soccer.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by
 */
public class XmlUtil {

    private String _xml;

    public XmlUtil(String xml)
    {
        _xml = xml;
    }

    public static Map xmlToMap(String strXML) throws Exception {
        HashMap data = new HashMap();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        ByteArrayInputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
        Document doc = documentBuilder.parse(stream);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getDocumentElement().getChildNodes();

        for(int idx = 0; idx < nodeList.getLength(); ++idx) {
            Node node = nodeList.item(idx);
            if(node.getNodeType() == 1) {
                Element element = (Element)node;
                data.put(element.getNodeName(), element.getTextContent());
            }
        }

        try {
            stream.close();
        } catch (Exception e) {
            ;
        }

        return data;
    }

    public static String mapToXml(Map data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("xml");
        document.appendChild(root);
        Iterator tf = data.keySet().iterator();

        while(tf.hasNext()) {
            String transformer = (String)tf.next();
            String source = (String)data.get(transformer);
            if(source == null) {
                source = "";
            }

            source = source.trim();
            Element writer = document.createElement(transformer);
            writer.appendChild(document.createTextNode(source));
            root.appendChild(writer);
        }

        TransformerFactory tf1 = TransformerFactory.newInstance();
        Transformer transformer1 = tf1.newTransformer();
        DOMSource source1 = new DOMSource(document);
        transformer1.setOutputProperty("encoding", "UTF-8");
        transformer1.setOutputProperty("indent", "yes");
        StringWriter writer1 = new StringWriter();
        StreamResult result = new StreamResult(writer1);
        transformer1.transform(source1, result);
        String output = writer1.getBuffer().toString();

        try {
            writer1.close();
        } catch (Exception e) {
            ;
        }

        return output;
    }

    /**
     * 单层XML根据节点名称获取值
     * @param field
     * @return
     */
    public String simpleXMLGetValue(String field)
    {
        String start = "<" + field + ">";
        String end = "</" + field + ">";
        String result = _xml.substring(_xml.indexOf(start), _xml.indexOf(end));
        return result.replaceAll(start, "");
    }
}
