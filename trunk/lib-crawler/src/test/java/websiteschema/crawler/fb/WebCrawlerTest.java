/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import org.junit.Test;
import websiteschema.fb.core.app.Application;
import websiteschema.fb.core.RuntimeContext;

/**
 *
 * @author ray
 */
public class WebCrawlerTest {

    @Test
    public void test() throws InterruptedException {
        Application app = new Application();
        RuntimeContext context = app.getContext();
        context.loadConfigure("fb/links.app");

        Thread t = new Thread(app);
        t.start();
        t.join();
    }

//    @Test
    public void testCrawler() throws InterruptedException {
        Application app = new Application();
        RuntimeContext context = app.getContext();
        context.loadConfigure("fb/crawler.app");

        Thread t = new Thread(app);
        t.start();
        t.join();
    }
}
