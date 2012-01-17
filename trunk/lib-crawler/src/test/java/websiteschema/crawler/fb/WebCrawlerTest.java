/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;


import org.junit.Test;
import org.w3c.dom.Document;
import websiteschema.fb.core.RuntimeContext;
import websiteschema.fb.core.app.Application;
import websiteschema.model.domain.Site;
import websiteschema.model.domain.Websiteschema;
import websiteschema.model.domain.factory.WebsiteschemaFactory;
import websiteschema.persistence.hbase.SampleMapper;
import websiteschema.persistence.hbase.WebsiteschemaMapper;
import websiteschema.persistence.rdbms.SiteMapper;


/**
 *
 * @author mgd
 */
public class WebCrawlerTest {

    @Test
    public void test() throws Exception {

        String url = "http://news.163.com/12/0117/11/7NVE580G00014JB5.html?from=index";
        String siteId = "www_163_com_1";
        Application app = new Application();
        RuntimeContext context = app.getContext();
        context.loadConfigure("fb/crawler.app");
        WebsiteschemaMapper mapper = context.getSpringBeanFactory().getBean("websiteschemaMapper", WebsiteschemaMapper.class);
        FBWebCrawler fbc = new FBWebCrawler();
        fbc.url = url;
        fbc.schema = mapper.get(siteId);
        fbc.fetch();
        Document doc  = fbc.out;
        System.out.println(doc.toString());
    }
}
