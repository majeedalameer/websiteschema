/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

//import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.apache.xerces.util.DOMUtil;
import org.jaxen.JaxenException;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.cluster.analyzer.Link;
import websiteschema.element.DocumentUtil;
import websiteschema.element.W3CDOMUtil;
import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.utils.StringUtil;
import websiteschema.utils.UrlLinkUtil;

import org.jaxen.XPathSyntaxException;
import websiteschema.crawler.Crawler;
import websiteschema.crawler.WebPage;

/**
 *
 * @author ray
 */
@EI(name = {"EI:EXT"})
@EO(name = {"EO", "EMPTY"})
@Description(desc = "抽取DOM树中指定位置内的链接。")
public class FBSearchLinksExtractor extends FunctionBlock {

    @DI(name = "IN")
    public Document in;
    @DI(name = "DOCS")
    public Document[] docs;
    @DI(name = "SELECTITEMXPATH")
    public String selectItemXpath = null;
    @DI(name = "BUTTONXPATH")
    public String buttonXpath = null;
    @DI(name = "URL")
    public String url;
    @DI(name = "PAGE")
    public WebPage webPage;
    @DO(name = "OUT", relativeEvents = {"EO"})
    public List<Link> links = null;

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
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void extractLinks() {
        try {
            if (null != docs && null != selectItemXpath && null != buttonXpath && null != url) {
                links = new ArrayList<Link>();
                for (Document doc : docs) {
                    List<Link> urls = extractLinks(doc);
                    if (null != urls) {
                        links.addAll(urls);
                    }
                }
            } else if (null != in && null != selectItemXpath && null != buttonXpath && null != url) {
                links = new ArrayList<Link>();
                List<Link> urls = extractLinks(in);
                if (null != urls) {
                    links.addAll(urls);
                }
            }
        } catch (XPathSyntaxException e) {
            l.debug(e.getMessage());
            l.debug("[exception] " + url + " seletItemXpath:" + selectItemXpath + " buttonXpath:" + buttonXpath);
        } catch (Exception e) {
            l.debug("[exception]", e);
        }
    }

    private List<Link> extractLinks(Document doc) throws Exception {
        List<Link> ret = new ArrayList<Link>();
        if (null != doc && null != selectItemXpath && null != buttonXpath && null != url) {
            List<Node> seletetItemNodes = DocumentUtil.getByXPath(doc, selectItemXpath.trim());
            List<Node> buttonNodes = DocumentUtil.getByXPath(doc, buttonXpath.trim());

            HtmlSelect select = (HtmlSelect) seletetItemNodes.get(0);
            HtmlImageInput button = (HtmlImageInput) buttonNodes.get(0);
            List<HtmlOption> listOption = select.getOptions();

            for (HtmlOption o : listOption) {
                select.setSelectedAttribute(o, true);
                final Page page = button.click();
                Link lnk = new Link();
                lnk.setHref(webPage.getCrawler().getUrl());
                lnk.setText(o.getFirstChild().getNodeValue());
                ret.add(lnk);
                page.cleanUp();
            }

        }
        return ret;
    }   
}
