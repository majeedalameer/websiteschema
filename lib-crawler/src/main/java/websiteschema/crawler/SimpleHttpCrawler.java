/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler;

import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.http.ConnectionManager;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import websiteschema.crawler.htmlunit.HtmlUnitWebCrawler;
import websiteschema.model.domain.cralwer.CrawlerSettings;

/**
 *
 * @author ray
 */
public class SimpleHttpCrawler implements Crawler {

    boolean loadImage = false;
    boolean loadEmbeddedFrame = false;
    boolean allowPopupWindow = false;
    String url = null;
    String encoding = null;
    CrawlerSettings crawlerSettings;
    int sec = 1000;
    int delay = 30 * sec;
    int httpStatus = 0;
    Logger l = Logger.getLogger(HtmlUnitWebCrawler.class);

    private Parser getHtmlParser(String url_str, String encoding_str) {
        Parser parser = null;
        ConnectionManager cm = Parser.getConnectionManager();
        try {
            parser = new Parser(cm.openConnection(url_str));
            if (null != encoding_str && !"".equals(encoding_str)) {
                parser.setEncoding(encoding_str);
            }
        } catch (ParserException ex) {
            l.error(ex);
        }
        return parser;
    }

    @Override
    public Document[] crawl(String url_str) {
        Parser parser = getHtmlParser(url_str, null);// 默认编码
        NodeFilter nodeFilter = new TagNameFilter("body");
        NodeList nodeList = null;
        try {
            nodeList = parser.parse(nodeFilter);
        } catch (ParserException ex) {
            l.error(ex);
        }
        String body_str = nodeList.elementAt(0).toHtml();

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = null;
        try {
            builder = domFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            l.error(ex);
        }
        Document doc = builder.newDocument();
        Element html = doc.createElement("HTML");
        Element body = doc.createElement("BODY");
        body.setTextContent(body_str);
        html.appendChild(body);
        doc.appendChild(html);
        //System.err.println(html.getTextContent());
        return new Document[]{doc};
    }

    @Override
    public void stopLoad() {
        l.debug("getLinks: Not supported yet.");
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String[] getLinks() {
        l.debug("getLinks: Not supported yet.");
        return null;
    }

    @Override
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public void setProxy(String server, int port) {
        l.debug("setProxy: Not supported yet.");
    }

    @Override
    public void executeScript(String javascriptBody) {
        l.debug("executeScript: Not supported yet.");
    }

    @Override
    public void setLoadImage(boolean yes) {
        this.loadImage = yes;
    }

    @Override
    public void setLoadEmbeddedFrame(boolean yes) {
        loadEmbeddedFrame = yes;
    }

    @Override
    public void setAllowPopupWindow(boolean yes) {
        l.debug("setAllowPopupWindow: Not supported yet.");
    }

    @Override
    public void setCrawlerSettings(CrawlerSettings setting) {
        this.crawlerSettings = setting;
        if (null == encoding || "".equals(encoding)) {
            encoding = crawlerSettings.getEncoding();
        }
    }

    @Override
    public void setTimeout(int timeout) {
        this.delay = timeout;
    }
}
