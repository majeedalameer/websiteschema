/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.element.XPathAttributes;
import websiteschema.element.factory.XPathAttrFactory;
import websiteschema.model.domain.cluster.DocUnits;
import websiteschema.model.domain.cluster.Unit;

/**
 *
 * @author ray
 */
public class DocumentConvertor {

    XPathAttributes xpathAttr;

    public XPathAttributes getXpathAttr() {
        return xpathAttr;
    }

    public void setXpathAttr(XPathAttributes xpathAttr) {
        this.xpathAttr = xpathAttr;
    }

    public DocUnits convertDocument(Document document) {
        List<Unit> units = new ArrayList<Unit>();

        traversal(document.getDocumentElement(), units);

        DocUnits ret = new DocUnits();
        ret.setUnits(units.toArray(new Unit[0]));
        return ret;
    }

    private void traversal(Element ele, List<Unit> units) {
        StringBuilder text = new StringBuilder();
        String xpath = XPathAttrFactory.getInstance().create(ele, xpathAttr);
        NodeList children = ele.getChildNodes();
        if (null != children) {
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if(Node.TEXT_NODE == child.getNodeType()) {
                    text.append(child.getNodeValue());
                } else if(Node.ELEMENT_NODE == child.getNodeType()) {
                    traversal((Element) child,units);
                }
            }
        }
        Unit unit = new Unit(xpath, text.toString());
        units.add(unit);
    }
}
