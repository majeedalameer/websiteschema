/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.crawler.htmlunit.HtmlUnitWebCrawler;

/**
 *
 * @author ray
 */
public class HtmlUnitWebCrawlerTest {

    @Test
    public void test() throws InterruptedException {
        String url = "http://localhost:8080/";
        Crawler crawler = new HtmlUnitWebCrawler();
        Document[] docs = crawler.crawl(url);
        System.out.println("----" + System.currentTimeMillis());
        crawler = null;

        Document doc = docs[0];
        print(doc);
    }

    public static void print(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            System.out.println(node.getNodeValue());
        } else {
            if (node.hasChildNodes()) {
                NodeList children = node.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    Node child = children.item(i);
                    print(child);
                }
            }
        }
    }
}
