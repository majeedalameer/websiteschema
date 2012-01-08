/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer.fields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import websiteschema.element.DocumentUtil;

/**
 *
 * @author ray
 */
public class XPathExtractor extends AbstractFieldExtractor {

    private String xpath = null;
    public final static String xpathKey = "XPath";

    public Collection<String> extract(Document doc) {
        if (null != xpath && !"".equals(xpath)) {
            return extractByXPath(doc);
        }
        return null;
    }

    private Collection<String> extractByXPath(Document doc) {
        List<String> ret = new ArrayList<String>();
        List<Node> nodes = DocumentUtil.getByXPath(doc, xpath.trim());
        for (Node node : nodes) {
            String t = ExtractUtil.getInstance().getNodeText(node);
            if (null != t && !"".equals(t)) {
                t = t.trim();
                if (!ret.contains(t)) {
                    ret.add(t.trim());
                }
            }
        }
        return ret;
    }

    public void init(Map<String, String> params) {
        xpath = params.containsKey(xpathKey) ? params.get(xpathKey) : "";
    }
}
