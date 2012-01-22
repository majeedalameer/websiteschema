/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.xerces.util.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.element.DocumentUtil;
import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.utils.UrlLinkUtil;

/**
 *
 * @author ray
 */
@EI(name = {"EI:EXT"})
@EO(name = {"EO", "FATAL", "EMPTY"})
@Description(desc = "抽取DOM树中指定位置内的链接。")
public class FBLinksExtractor extends FunctionBlock {

    @DI(name = "IN")
    public Document in;
    @DI(name = "XPATH")
    public String xpath = null;
    @DI(name = "URL")
    public String url;
    @DO(name = "OUT", relativeEvents = {"EO"})
    public List<String> links = null;

    @Algorithm(name = "EXT")
    public void extract() {
        try {
            extractLinks();
            if (null != links && !links.isEmpty()) {
                this.triggerEvent("EO");
            } else {
                this.triggerEvent("EMPTY");
            }
        } catch (Exception ex) {
            l.error(this.getName() + " error when extract links: " + ex.getMessage(), ex);
            this.triggerEvent("FATAL");
        }
    }

    private void extractLinks() {
        if (null != in && null != xpath && null != url) {
            List<Node> nodes = DocumentUtil.getByXPath(in, xpath.trim());
            links = new ArrayList<String>();
            for (int i = 0; i < nodes.size(); i++) {
                Node node = nodes.get(i);
                List<String> urls = getLinks(node);
                if (null != urls) {
                    links.addAll(urls);
                }
            }
        }
    }

    private List<String> getLinks(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            String nodeName = node.getNodeName();
            if (nodeName.equalsIgnoreCase("A")) {
                String href = DOMUtil.getAttrValue((Element) node, "href");
                if (null != href) {
                    URL link = UrlLinkUtil.getInstance().getURL(url, href);
                    if (null != link) {
                        List<String> ret = new ArrayList<String>();
                        ret.add(link.toString());
                        return ret;
                    }
                }
            } else {
                List<String> ret = new ArrayList<String>();
                NodeList children = node.getChildNodes();
                if (null != children) {
                    for (int i = 0; i < children.getLength(); i++) {
                        Node child = children.item(i);
                        List<String> urls = getLinks(child);
                        if (null != urls) {
                            ret.addAll(urls);
                        }
                    }
                }
                if (!ret.isEmpty()) {
                    return ret;
                }
            }
        }
        return null;
    }
}
