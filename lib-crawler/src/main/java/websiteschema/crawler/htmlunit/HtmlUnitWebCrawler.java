/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.htmlunit;

import com.gargoylesoftware.htmlunit.BinaryPage;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.JavaScriptPage;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.XHtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import websiteschema.crawler.Crawler;
import websiteschema.model.domain.cralwer.CrawlerSettings;

/**
 *
 * @author ray
 */
public class HtmlUnitWebCrawler implements Crawler {

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

    private WebClient getWebClient() {
        final WebClient webClient = new WebClient();
        webClient.setJavaScriptEnabled(false);
        webClient.setTimeout(delay);
        webClient.setPopupBlockerEnabled(allowPopupWindow);
        webClient.setRedirectEnabled(true);
        webClient.setThrowExceptionOnScriptError(false);
        webClient.setThrowExceptionOnFailingStatusCode(true);
        return webClient;
    }

    private Document[] getDocuments(Page page) throws ParserConfigurationException {
        if (page instanceof HtmlPage) {
            return new Document[]{(HtmlPage) page};
        } else if (page instanceof SgmlPage) {
            return new Document[]{(SgmlPage) page};
        } else if (page instanceof XmlPage) {
            return new Document[]{(XmlPage) page};
        } else if (page instanceof XHtmlPage) {
            return new Document[]{(XHtmlPage) page};
        } else if (page instanceof TextPage) {
            TextPage text = (TextPage) page;
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element html = doc.createElement("HTML");
            Element body = doc.createElement("BODY");
            body.setTextContent(text.getContent());
            html.appendChild(body);
            doc.appendChild(html);
            return new Document[]{doc};
        } else if (page instanceof JavaScriptPage) {
            return null;
        } else if (page instanceof UnexpectedPage) {
            return null;
        } else if (page instanceof BinaryPage) {
            return null;
        } else {
            return null;
        }
    }

    @Override
    public Document[] crawl(String url) {
        final WebClient webClient = getWebClient();
        try {
            URL dest = new URL(url);
            WebRequest req = new WebRequest(dest);
            WebResponse res = webClient.loadWebResponse(req);
            httpStatus = res.getStatusCode();
            
            WebWindow window = webClient.getCurrentWindow();
//            final HtmlPage page = new HtmlPage(dest, res, window);
            final Page page = webClient.loadWebResponseInto(res, window);

            this.url = page.getUrl().toString();
            return getDocuments(page);
        } catch (IOException ex) {
            l.error(ex);
        } catch (FailingHttpStatusCodeException ex) {
            l.error(ex);
            httpStatus = ex.getStatusCode();
        } catch (ParserConfigurationException ex) {
            l.error(ex);
        } finally {
            webClient.closeAllWindows();
        }
        return null;
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
