/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import com.webrenderer.swing.dom.IDocument;
import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import org.w3c.dom.Node;
import websiteschema.element.Rectangle;
import websiteschema.element.factory.RectangleFactory;

/**
 *
 * @author ray
 */
public class ElementUtil {

    private final static ElementUtil instance = new ElementUtil();

    public static ElementUtil getInstance() {
        return instance;
    }

    public void drawRectangleInPage(IElement ele) {
        String lastStyle = ele.getAttribute("style", 0);
        String additionStyle = "border-style: solid; border-width: 3px;";
        if (null != lastStyle && !"".equals(lastStyle)) {
            ele.setAttribute("style", lastStyle + ";" + additionStyle, 0);
        } else {
            ele.setAttribute("style", additionStyle, 0);
        }
    }

    public double getPageSize(IDocument doc) {
        if (null != doc) {
            Rectangle rect = RectangleFactory.getInstance().create(doc.getBody());
            double pageSize = rect.getHeight() * rect.getWidth();
            return pageSize;
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    public String getText(IElement ele) {
        StringBuilder sb = new StringBuilder();

        IElementCollection children = ele.getChildElements();
        for (int i = 0; i < children.length(); i++) {
            IElement child = children.item(i);
            if (child.isTextNode()) {
                sb.append(child.getTextNodeText());
            } else {
                sb.append(getText(child));
            }
        }

        return sb.toString();
    }

    public void printNodeType(IElement ele) {
        Node node = ele.convertToW3CNode();
        int type = node.getNodeType();
        printNodeType(type);
    }

    public void printNodeType(int type) {
        switch (type) {
            case Node.ATTRIBUTE_NODE:
                System.out.println("ATTRIBUTE_NODE");
                break;
            case Node.CDATA_SECTION_NODE:
                System.out.println("CDATA_SECTION_NODE");
                break;
            case Node.COMMENT_NODE:
                System.out.println("COMMENT_NODE");
                break;
            case Node.DOCUMENT_FRAGMENT_NODE:
                System.out.println("DOCUMENT_FRAGMENT_NODE");
                break;
            case Node.DOCUMENT_NODE:
                System.out.println("DOCUMENT_NODE");
                break;
            case Node.DOCUMENT_TYPE_NODE:
                System.out.println("DOCUMENT_TYPE_NODE");
                break;
            case Node.ELEMENT_NODE:
                System.out.println("ELEMENT_NODE");
                break;
            case Node.ENTITY_NODE:
                System.out.println("ENTITY_NODE");
                break;
            case Node.ENTITY_REFERENCE_NODE:
                System.out.println("ENTITY_REFERENCE_NODE");
                break;
            case Node.NOTATION_NODE:
                System.out.println("NOTATION_NODE");
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                System.out.println("PROCESSING_INSTRUCTION_NODE");
                break;
            case Node.TEXT_NODE:
                System.out.println("TEXT_NODE");
                break;
        }
    }
}
