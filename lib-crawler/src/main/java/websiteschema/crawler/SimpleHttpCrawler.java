/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler;

import org.w3c.dom.Document;
import websiteschema.crawler.htmlunit.HtmlUnitWebCrawler;
import websiteschema.model.domain.cralwer.CrawlerSettings;
import websiteschema.utils.JsoupUtil;
import org.apache.log4j.Logger;
import org.jsoup.Connection;

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
    int delay = 15 * sec;
    int httpStatus = 0;
    Logger l = Logger.getLogger(HtmlUnitWebCrawler.class);

    @Override
    public Document[] crawl(String url_str) {
        try {
            url = url_str;
            Connection conn = org.jsoup.Jsoup.connect(url_str);
            conn.followRedirects(true);
            conn.request().timeout(delay);

            org.jsoup.nodes.Document doc = conn.get();
            String title = doc.title();
            System.out.println(title);
            org.w3c.dom.Document document = JsoupUtil.getInstance().convert(doc);
            httpStatus = conn.response().statusCode();
            url = conn.response().url().toString();
            //System.err.println(html.getTextContent());
            return new Document[]{document};
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
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
