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
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
public class StandardExtractor extends AbstractFieldExtractor {

    private String xpath = null;
    private String prefix = null;
    private String suffix = null;
    private String regex = null;
    public final static String xpathKey = "XPath";
    public final static String prefixKey = "PrefixString";
    public final static String suffixKey = "SuffixString";
    public final static String regexKey = "Regex";

    public Collection<String> extract(Document doc) {
        if (null != xpath && !"".equals(xpath)) {
            return extractByXPath(doc);
        } else if (!"".equals(prefix) && !"".equals(suffix)) {
            return extractByPattern(prefix, suffix, doc);
        }
        return null;
    }

    private Collection<String> extractByXPath(Document doc) {
        List<String> ret = new ArrayList<String>();
        if (null != doc) {
            List<Node> nodes = DocumentUtil.getByXPath(doc, xpath.trim());
            for (Node node : nodes) {
                String res = ExtractUtil.getInstance().getNodeText(node);
                if (null != res && !"".equals(res)) {
                    res = StringUtil.trim(res);
                    if (null != regex && !"".equals(regex)) {
                        if (res.matches(regex) && !ret.contains(res)) {
                            ret.add(res);
                        }
                    } else if (!ret.contains(res)) {
                        ret.add(res);
                    }
                }
            }
        }
        return ret;
    }

    private List<String> extractByPattern(String prefix, String suffix, Document doc) {
        List<String> ret = new ArrayList<String>();

        String text = DocumentUtil.getXMLString(doc);
        int start = text.indexOf(prefix);
        while (start >= 0) {
            int end = text.indexOf(suffix, start + prefix.length());
            if (end > 0) {
                String res = text.substring(start + prefix.length(), end);
                if (null != res && !"".equals(res)) {
                    res = StringUtil.trim(res);
                    if (null != regex && !"".equals(regex)) {
                        if (res.matches(regex) && !ret.contains(res)) {
                            ret.add(res);
                        }
                    } else if (!ret.contains(res)) {
                        ret.add(res);
                    }
                }
                start = text.indexOf(prefix, end + suffix.length());
            } else {
                break;
            }
        }
        return ret;
    }

    public void init(Map<String, String> params) {
        xpath = params.containsKey(xpathKey) ? params.get(xpathKey) : "";
        prefix = params.containsKey(prefixKey) ? params.get(prefixKey) : "";
        suffix = params.containsKey(suffixKey) ? params.get(suffixKey) : "";
        regex = params.containsKey(regexKey) ? params.get(regexKey) : "";
    }
}
