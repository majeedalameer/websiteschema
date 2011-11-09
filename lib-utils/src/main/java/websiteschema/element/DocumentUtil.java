/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.element;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import websiteschema.common.base.Function;

/**
 *
 * @author ray
 */
public class DocumentUtil {

    public static String getXMLString(Document doc) {
        return toString(doc);
    }

    static private String toString(Document document) {
        String result = null;
        if (document != null) {
            StringWriter strWtr = new StringWriter();
            StreamResult strResult = new StreamResult(strWtr);
            TransformerFactory tfac = TransformerFactory.newInstance();
            try {
                Transformer t = tfac.newTransformer();
                t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                t.setOutputProperty(OutputKeys.INDENT, "yes");
                t.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,// text
                t.setOutputProperty(
                        "{http://xml.apache.org/xslt}indent-amount", "4");
                t.transform(new DOMSource(document.getDocumentElement()),
                        strResult);
            } catch (Exception e) {
                System.err.println("DocumentUtil.toString(Document): " + e);
            }
            result = strResult.getWriter().toString();
        }
        return result;
    }

    public static Document getCopy(Document doc)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document ret = builder.parse(new ByteArrayInputStream(toString(doc).getBytes()));

        return ret;
    }

    /**
     * 复制一个仅包含标签、属性、文本的Document
     * @param xpath
     * @param pre
     * @return
     */
    public static Document clone(Document doc)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document ret = builder.newDocument();

        return ret;
    }

    public void traversal(Node ele, Function<Node> func) {
        if (null != ele) {
            func.invoke(ele);
            NodeList children = ele.getChildNodes();
            if (null != children) {
                for (int i = 0; i < children.getLength(); i++) {
                    Node child = children.item(i);
                    traversal(child, func);
                }
            }
        }
    }


    /**
     * 为XPATH增加pre namespace，实现简单，如果需要完全正确的结果，需要做XPATH的词法分析，这里还暂不支持//p[b='xy']这种格式。
     * @param xpath
     * @param pre
     * @return
     */
    public static String buildXPath(String xpath, String pre) {
        if (null == pre || "".equals(pre)) {
            return xpath;
        }
        StringBuilder sb = new StringBuilder();

        String[] array = xpath.split("(?<!/)/(?!/)");
        if (array.length > 1) {

            for (int i = 0; i < array.length; i++) {
                String str = array[i];
                if (str.trim().length() > 0) {
//                    System.out.println("-"+str);
                    if (!str.startsWith("//")) {
                        if (!str.startsWith("@") && !str.startsWith("*") && !str.endsWith(")")) {
                            sb.append(pre).append(":").append(str).append("/");
                        } else {
                            sb.append(str).append("/");
                        }
                    } else {
                        String content = str.substring(2);
                        if (!content.startsWith("@") && !content.startsWith("*") && !content.endsWith(")")) {
                            sb.append("//").append(pre).append(":").append(str.substring(2)).append("/");
                        } else {
                            sb.append(str).append("/");
                        }
                    }
                } else if (i == 0) {
                    sb.append("/");
                }
            }
        } else {
            String str = array[0];
            if (!str.startsWith("//")) {
                sb.append(pre).append(":").append(array[0]);
            } else {
                String content = str.substring(2);
                if (!content.startsWith("@") && !content.startsWith("*") && !content.endsWith(")")) {
                    sb.append("//").append(pre).append(":").append(content).append("/");
                } else {
                    sb.append(str);
                }
            }
        }
        String ret = sb.toString();
        return ret.endsWith("/") ? ret.substring(0, ret.length() - 1) : ret;
    }
}
