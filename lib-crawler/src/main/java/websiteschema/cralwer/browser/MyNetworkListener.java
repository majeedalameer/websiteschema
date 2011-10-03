/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cralwer.browser;

import com.webrenderer.swing.IBrowserCanvas;
import com.webrenderer.swing.event.NetworkEvent;
import com.webrenderer.swing.event.NetworkListener;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;

/**
 *
 * @author ray
 */
public class MyNetworkListener implements NetworkListener {

    Logger l = Logger.getLogger(MyNetworkListener.class);
    IBrowserCanvas browser;
    int sec = 1000;
    long delay = 30 * sec;
    boolean started = false;
    Timer timer = null;
    boolean loadImage = false;
    boolean loadEmbeddedFrame = false;
    BrowserWebCrawler crawler;

    public MyNetworkListener(BrowserWebCrawler crawler) {
        this.crawler = crawler;
    }

    @Override
    public void onProgressChange(NetworkEvent ne) {
        l.debug("onProgressChange" + ((float) ne.getCurrentProgress() / (float) ne.getMaximumProgress()));
        float f = ((float) ne.getCurrentProgress() / (float) ne.getMaximumProgress());
    }

    @Override
    public void onDocumentLoad(NetworkEvent ne) {
        l.debug("onDocumentLoad ");
        started = false;
        timer = new Timer();
        timer.schedule(new MyTimerTask(), delay);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            if (couldProcess()) {
                process();
            }
        }
    }

    public synchronized boolean couldProcess() {
        if (!started) {
            started = true;
            return started;
        } else {
            return false;
        }
    }

    private void process() {
        synchronized (crawler.lock) {
            System.out.println("notify");
            crawler.lock.notify();
        }
    }

    @Override
    public void onDocumentComplete(NetworkEvent ne) {
        l.debug("onDocumentComplete " + ne.getURL());

        crawler.setUrl(ne.getURL());

        timer.cancel();
        timer = null;
        if (couldProcess()) {
            process();
        }
    }

    @Override
    public void onNetworkStatus(NetworkEvent ne) {
//        l.debug("onNetworkStatus " + ne.getStatus());
    }

    @Override
    public void onNetworkError(NetworkEvent ne) {
//        l.debug("onNetworkError " + ne.getFailure());
    }

    @Override
    public void onHTTPResponse(NetworkEvent ne) {
//        l.debug("onHTTPResponse\n" + ne.getResponseHeaders());
    }

    @Override
    public void onHTTPInterceptHeaders(NetworkEvent ne) {
//        l.debug("onHTTPInterceptHeaders " + ne.getURL());
//        l.trace("Send Request Header:\n" + ne.getMutableRequestHeaders());
    }

    public void setLoadImage(boolean yes) {
        this.loadImage = yes;
    }

    public boolean isLoadImage() {
        return loadImage;
    }

    public void setLoadEmbeddedFrame(boolean yes) {
        loadEmbeddedFrame = yes;
    }

    public boolean isLoadEmbeddedFrame() {
        return loadEmbeddedFrame;
    }
}
