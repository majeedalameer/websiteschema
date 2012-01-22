/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jsoup.nodes.*;

/**
 *
 * @author ray
 */
public class JsoupUtil {

    private static JsoupUtil ins = new JsoupUtil();

    public static JsoupUtil getInstance() {
        return ins;
    }

    public org.w3c.dom.Document convert(Document doc) {
        W3CDocumentConvertor convertor = new W3CDocumentConvertor();
        convertor.convert(doc);
        return convertor.doc;
    }

    private class W3CDocumentConvertor {

        String doctype = "html";
        org.w3c.dom.Document doc = null;

        void convert(Document document) {
            if (null != document) {
                doc = createDocument();
                convert(document, doc);
            }
        }

        void convert(Node node, org.w3c.dom.Node n) {
            List<Node> children = node.childNodes();
            if (null != children) {
                for (int i = 0; i < children.size(); i++) {
                    Node child = children.get(i);
                    if (child instanceof DocumentType) {
                        this.doctype = ((DocumentType) child).toString();
//                        doc.getDoctype().setNodeValue(doctype);
                    } else if (child instanceof Element) {
                        org.w3c.dom.Element ele = doc.createElement(child.nodeName());
                        //添加属性
                        Attributes attrs = child.attributes();
                        if (null != attrs) {
                            List<Attribute> list = attrs.asList();
                            for (Attribute attr : list) {
                                String key = attr.getKey();
                                String value = attr.getValue();
                                org.w3c.dom.Attr a = doc.createAttribute(key);
                                a.setValue(value);
                                ele.setAttributeNode(a);
                            }
                        }
                        n.appendChild(ele);
                        convert(child, ele);
                    } else if (child instanceof TextNode) {
                        TextNode t = (TextNode) child;
                        org.w3c.dom.Text text = doc.createTextNode(t.text());
                        n.appendChild(text);
                    }
                }
            }
        }

        org.w3c.dom.Document createDocument() {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);
            DocumentBuilder builder = null;
            try {
                builder = domFactory.newDocumentBuilder();
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace();
            }
            org.w3c.dom.Document doc = builder.newDocument();
            return doc;
        }
    }
}
