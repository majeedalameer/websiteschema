/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer.fields;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import websiteschema.cluster.analyzer.IFieldExtractor;
import websiteschema.element.DocumentUtil;

/**
 *
 * @author ray
 */
public class XPathExtractor implements IFieldExtractor {

    private String xpath = null;
    private final static String xpathKey = "XPath";

    public Set<String> extract(Document doc) {
        Set<String> ret = new HashSet<String>();
        List<Node> nodes = DocumentUtil.getByXPath(doc, xpath);
        for (Node node : nodes) {
            String t = ExtractUtil.getInstance().getNodeText(node);
            if (null != t && !"".equals(t)) {
                ret.add(t.trim());
            }
        }
        return ret;
    }

    public void init(Map<String, String> params) {
        xpath = params.containsKey(xpathKey) ? params.get(xpathKey) : "";
    }
}
