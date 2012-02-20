/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import org.w3c.dom.Document;
import websiteschema.crawler.Crawler;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.model.domain.Websiteschema;

/**
 *
 * @author ray
 */
@EO(name = {"SUC", "FAL"})
@EI(name = {"FETCH:FETCH"})
public class FBWebCrawler extends FunctionBlock {

    Crawler crawler;
    @DI(name = "URL")
    public String url;
    @DI(name = "CRAWLER")
    public String crawlerType;
    @DI(name = "SCHEMA")
    public Websiteschema schema;
    @DO(name = "DOC", relativeEvents = {"SUC"})
    public Document out;
    @DO(name = "DOCS", relativeEvents = {"SUC"})
    public Document[] docAndFrames;
    @DO(name = "URL", relativeEvents = {"SUC"})
    public String do_url;

    @Algorithm(name = "FETCH")
    public void fetch() {
        try {
            docAndFrames = null;
            Crawler c = createCrawler();
            docAndFrames = c.crawl(url);
            do_url = c.getUrl();
            if (null != docAndFrames && docAndFrames.length > 0) {
                out = docAndFrames[0];
                this.triggerEvent("SUC");
            } else {
                this.triggerEvent("FAL");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            l.error(ex);
            this.triggerEvent("FAL");
        }
    }

    private Crawler createCrawler() {
        if (null == crawler) {
            if (null == crawlerType) {
                crawlerType = "websiteschema.crawler.htmlunit.HtmlUnitWebCrawler";
            }
            try {
                Class clazz = Class.forName(crawlerType);
                crawler = (Crawler) clazz.newInstance();
                crawler.setCrawlerSettings(schema.getCrawlerSettings());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return crawler;
    }
}
