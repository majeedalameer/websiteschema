/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.browser.listener;

import com.webrenderer.swing.IBrowserCanvas;
import com.webrenderer.swing.dom.IDocument;
import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IStyleSheet;
import com.webrenderer.swing.event.NetworkEvent;
import com.webrenderer.swing.event.NetworkListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import websiteschema.context.BrowserContext;
import websiteschema.element.Rectangle;
import websiteschema.element.StyleSheet;
import websiteschema.element.factory.RectangleFactory;
import websiteschema.element.factory.StyleSheetFactory;
import websiteschema.utils.Configure;
import websiteschema.vips.VisualPageSegmenter;
import websiteschema.vips.extraction.BlockExtractor;
import websiteschema.vips.extraction.BlockExtractorFactory;
import websiteschema.vips.extraction.VipsBlockExtractor;

/**
 *
 * @author ray
 */
public class SimpleNetworkListener implements NetworkListener {

    Logger l = Logger.getRootLogger();
    BrowserContext context;
    IBrowserCanvas browser;
    javax.swing.JTextField addressTextField;
    int sec = 1000;
    long delay = 30 * sec;
    boolean started = false;
    Timer timer = null;

    public SimpleNetworkListener(BrowserContext context) {
        this.context = context;
        this.browser = context.getBrowser();
    }

    public JTextField getAddressTextField() {
        return addressTextField;
    }

    public void setAddressTextField(JTextField addressTextField) {
        this.addressTextField = addressTextField;
    }

    @Override
    public void onProgressChange(NetworkEvent ne) {
        l.debug("onProgressChange" + ((float) ne.getCurrentProgress() / (float) ne.getMaximumProgress()));
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

    private void processVIPS() {
        IDocument doc = context.getBrowser().getDocument();
        StyleSheet styleSheet = new StyleSheetFactory().createStyleSheet(doc);
        String referrer = doc.getReferrer();
        context.setStyleSheet(referrer, styleSheet);

        if (context.isUseVIPS()) {
            context.getVipsCanvas().setDocument(browser.getDocument());
        }
        // Creator VIPS Segmenter
        VisualPageSegmenter segmenter = new VisualPageSegmenter();
        segmenter.setPDoC(context.getConfigure().getIntProperty("VIPS", "PDoC"));
        // Create extractor
        Rectangle rect = RectangleFactory.getInstance().create(doc.getBody());
        double pageSize = rect.getHeight() * rect.getWidth();
        context.getConsole().log("Page: " + rect + " Size: " + pageSize);
        double threshold = context.getConfigure().getDoubleProperty("VIPS", "RelativeSizeThreshold", 0.1);
        BlockExtractor extractor =
                BlockExtractorFactory.getInstance().create(context, referrer, pageSize, threshold);

        // Set extractor
        segmenter.setExtractor(extractor);
        segmenter.pageSegment(doc);
    }

    private void process() {
//        processVIPS();
    }

    @Override
    public void onDocumentComplete(NetworkEvent ne) {
        l.debug("onDocumentComplete " + ne.getURL());
        addressTextField.setText(ne.getURL());

        timer.cancel();
        timer = null;
        if (couldProcess()) {
            process();
        }
    }

    @Override
    public void onNetworkStatus(NetworkEvent ne) {
        l.debug("onNetworkStatus " + ne.getStatus());
    }

    @Override
    public void onNetworkError(NetworkEvent ne) {
        l.debug("onNetworkError " + ne.getFailure());
    }

    @Override
    public void onHTTPResponse(NetworkEvent ne) {
//        l.debug("onHTTPResponse\n" + ne.getResponseHeaders());
    }

    @Override
    public void onHTTPInterceptHeaders(NetworkEvent ne) {
        l.debug("onHTTPInterceptHeaders " + ne.getURL());
        l.trace("Send Request Header:\n" + ne.getMutableRequestHeaders());
    }
}
