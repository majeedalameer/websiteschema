/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.http.ConnectionManager;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.junit.Test;
import org.w3c.dom.Document;
import websiteschema.fb.core.RuntimeContext;
import websiteschema.fb.core.app.Application;
import websiteschema.persistence.hbase.WebsiteschemaMapper;

/**
 *
 * @author mgd
 */
public class WebCrawlerTest {

    private static String URL_STR = "http://news.163.com/12/0117/11/7NVE580G00014JB5.html";

    @Test
    public void test() throws Exception {

        String siteId_str = "www_163_com_1";
        Application app = new Application();
        RuntimeContext context = app.getContext();
        context.loadConfigure("fb/crawler.app");
        WebsiteschemaMapper mapper = context.getSpringBeanFactory().getBean("websiteschemaMapper", WebsiteschemaMapper.class);
        FBWebCrawler fbc = new FBWebCrawler();
        fbc.url = URL_STR;
        fbc.schema = mapper.get(siteId_str);
        fbc.crawlerType = "websiteschema.crawler.SimpleHttpCrawler";
        fbc.fetch();
        Document doc = fbc.out;
        System.err.println(doc.getTextContent());
    }

    //@Test
    public void test_htmlparser() throws Exception {

        Parser parser = null;
        ConnectionManager cm = Parser.getConnectionManager();
        parser = new Parser(cm.openConnection(URL_STR));
        NodeFilter nodeFilter = new TagNameFilter("body");
        NodeList nodeList = null;
        try {
            nodeList = parser.parse(nodeFilter);
        } catch (ParserException ex) {
            System.out.println(ex);
        }
        String body_str = nodeList.elementAt(0).toHtml();
        System.err.println(body_str);
    }
}
