/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import websiteschema.element.factory.XPathAttrFactory;
import websiteschema.element.XPathAttributes;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import websiteschema.fb.annotation.DO;
import java.util.Set;
import java.util.Map;
import org.w3c.dom.Document;
import websiteschema.persistence.hbase.core.Function;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import static websiteschema.utils.PojoMapper.*;

/**
 *
 * @author ray
 */
@EO(name = {"EO"})
@EI(name = {"EXTRACT:EXT"})
public class FBDOMExtractor extends FunctionBlock {

    @DI(name = "DocIn")
    public Document in;
    @DI(name = "Prop")
    public Map<String, String> prop;
    @DI(name = "XPathAttr")
    public XPathAttributes xpathAttr;
    @DO(name = "DocOut", relativeEvents = {"EO"})
    public Document out;

    @Algorithm(name = "EXT")
    public void extract() {
        try {
            String vnode = prop.get("ValidNodes");
            Set<String> validNodes = null != vnode ? (Set<String>) fromJson(vnode, Set.class) : null;
            String ivnode = prop.get("InvalidNodes");
            Set<String> invalidNodes = null != vnode ? (Set<String>) fromJson(ivnode, Set.class) : null;

            out = extract(validNodes, invalidNodes);
            this.triggerEvent("EO");
        } catch (Exception ex) {
            ex.printStackTrace();
            l.error(ex);
        }
    }

    private boolean isTextNode(Node node) {
        return Node.TEXT_NODE == node.getNodeType();
    }

    private Document extract(final Set<String> validNodes, final Set<String> invalidNodes) {
        if (null != in) {
            final StringBuilder content = new StringBuilder();
            traversal(in.getDocumentElement(), new Function<Node>() {

                @Override
                public void invoke(Node node) {
//                    System.out.println("----" + XPathAttrFactory.getInstance().create(node, xpathAttr));
                    if (isTextNode(node)) {
                        Node father = node.getParentNode();
                        String xpath = XPathAttrFactory.getInstance().create(father, xpathAttr);
                        if (!invalidNodes.contains(xpath)) {
                            if (validNodes.contains(xpath)) {
                                String nodeName = father.getNodeName();
                                String text = node.getNodeValue();
                                content.append(node.getNodeValue());
                                if ("P".equalsIgnoreCase(nodeName)) {
                                    if (!text.endsWith("\n")) {
                                        content.append("\n");
                                    }
                                }
                            }
                        }
                    }
                }
            });

            try {
                DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = domFactory.newDocumentBuilder();
                Document doc = builder.newDocument();
                Element eleRoot = doc.createElement("DOCUMENT");
                Element eleContent = doc.createElement("DRECONTENT");
                eleContent.setTextContent(content.toString());
                eleRoot.appendChild(eleContent);
                doc.appendChild(eleRoot);
                return doc;
            } catch (Exception ex) {
                l.error("Can not create Document: ", ex);
            }
        }
        return null;
    }

    private void traversal(Node ele, Function<Node> func) {
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
}
