/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.crawler;

import org.w3c.dom.Document;
import websiteschema.model.domain.cralwer.CrawlerSettings;

/**
 *
 * @author ray
 */
public class SimpleHttpCrawler implements Crawler {

    @Override
    public Document[] crawl(String url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void stopLoad() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getUrl() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getHttpStatus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] getLinks() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setEncoding(String encoding) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setProxy(String server, int port) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void executeScript(String javascriptBody) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLoadImage(boolean yes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLoadEmbeddedFrame(boolean yes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAllowPopupWindow(boolean yes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCrawlerSettings(CrawlerSettings setting) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTimeout(int timeout) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
