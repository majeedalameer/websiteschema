/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.crawler;

import com.webrenderer.swing.BrowserFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import org.w3c.dom.Document;
import websiteschema.cralwer.Crawler;
import websiteschema.cralwer.browser.BrowserWebCrawler;

/**
 *
 * @author ray
 */
public class BrowserWebCrawlerTest {

    @Test
    public void test() {
        String url = "http://localhost:8080/";
        Crawler crawler = new BrowserWebCrawler();
        Document[] docs = crawler.crawl(url);

        while(!BrowserFactory.shutdownMozilla()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
