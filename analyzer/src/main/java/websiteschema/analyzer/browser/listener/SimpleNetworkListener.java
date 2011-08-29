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

    public SimpleNetworkListener(BrowserContext context) {
        this.context = context;
        this.browser = context.getBrowser();
    }

    @Override
    public void onProgressChange(NetworkEvent ne) {
        l.debug("onProgressChange" + ((float) ne.getCurrentProgress() / (float) ne.getMaximumProgress()));
    }

    @Override
    public void onDocumentLoad(NetworkEvent ne) {
        l.debug("onDocumentLoad ");
    }

    @Override
    public void onDocumentComplete(NetworkEvent ne) {
        l.debug("onDocumentComplete " + ne.getURL());

        IDocument doc = context.getBrowser().getDocument();
        StyleSheet styleSheet = new StyleSheetFactory().createStyleSheet(doc);
        String referrer = doc.getReferrer();
        context.setStyleSheet(referrer, styleSheet);

        if (context.isUseVIPS()) {
            context.getVipsCanvas().setDocument(browser.getDocument());
        }
        // Creator VIPS Segmenter
        VisualPageSegmenter segmenter = new VisualPageSegmenter();
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
