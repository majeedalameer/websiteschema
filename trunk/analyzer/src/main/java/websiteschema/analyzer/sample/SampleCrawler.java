/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.sample;

import java.util.Date;
import org.w3c.dom.Document;
import websiteschema.cluster.DocumentConvertor;
import websiteschema.context.BrowserContext;
import websiteschema.cralwer.Crawler;
import websiteschema.cralwer.browser.BrowserWebCrawler;
import websiteschema.element.XPathAttributes;
import websiteschema.model.domain.cluster.DocUnits;
import websiteschema.model.domain.cluster.Sample;
import websiteschema.persistence.hbase.SampleMapper;

/**
 *
 * @author ray
 */
public class SampleCrawler {

    final DocumentConvertor docConvertor = new DocumentConvertor();
    final SampleMapper mapper = BrowserContext.getSpringContext().getBean("sampleMapper", SampleMapper.class);

    public void setXPathAttributes(XPathAttributes attr) {
        this.docConvertor.setXpathAttr(attr);
    }

    public void fetch(Sample sample) {
        String url = sample.getUrl();
        System.out.println("fetch: " + url);

        if (shouldCrawl(sample)) {
            final Crawler crawler = new BrowserWebCrawler();
            crawler.setLoadImage(false);
            Document[] docs = crawler.crawl(url);
            Document doc = docs[0];
            System.out.println("start convert");
            DocUnits units = docConvertor.convertDocument(doc);
            System.out.println("end convert");
            sample.setContent(units);
            sample.setHttpStatus(crawler.getHttpStatus());
            sample.setLastUpdateTime(new Date());
            mapper.put(sample);
        }
    }

    private boolean shouldCrawl(Sample sample) {
        if (null != sample) {
            DocUnits content = sample.getContent();
            if (null != content) {
                Date last = sample.getLastUpdateTime();
                if (System.currentTimeMillis() - last.getTime() > 86400000
                        || 0 == sample.getHttpStatus()) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }
}
