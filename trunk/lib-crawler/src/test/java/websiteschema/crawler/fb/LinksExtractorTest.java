/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.io.ByteArrayInputStream;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.Test;
import org.w3c.dom.Document;
import websiteschema.utils.FileUtil;

/**
 *
 * @author ray
 */
public class LinksExtractorTest {

    @Test
    public void test() throws Exception {
        Document doc = create();
        String xpath = "//DIV[@class='main_content clearfix']";
        String url = "http://focus.news.163.com/";
        FBLinksExtractor extractor = new FBLinksExtractor();
        extractor.in = doc;
        extractor.xpath = xpath;
        extractor.url = url;
        extractor.extract();
        List<String> results = extractor.links;
        assert (results.size() == 26);
    }

    private Document create() throws Exception {
        String content = FileUtil.readResource("test.xml");
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(content.getBytes("UTF-8")));
        return doc;
    }
}
