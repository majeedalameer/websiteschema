/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips;

import com.webrenderer.swing.dom.IDocument;
import com.webrenderer.swing.dom.IElement;
import org.apache.log4j.Logger;
import websiteschema.context.BrowserContext;
import websiteschema.element.Rectangle;
import websiteschema.element.StyleSheet;
import websiteschema.element.factory.RectangleFactory;
import websiteschema.element.factory.StyleSheetFactory;
import websiteschema.vips.extraction.BlockExtractor;
import websiteschema.vips.extraction.BlockExtractorFactory;

/**
 *
 * @author ray
 */
public class VIPSImpl {

    BrowserContext context = null;
    VisionBasedPageSegmenter segmenter = null;
    Logger l = Logger.getRootLogger();

    public VIPSImpl(BrowserContext context) {
        this.context = context;
        init();
    }

    public VisionBlock segment(IDocument doc) {
        return processVIPS(doc);
    }

    /**
     * Creator VIPS Segmenter
     */
    private void init() {
        segmenter = new VisionBasedPageSegmenter();
        segmenter.setPDoC(context.getConfigure().getIntProperty("VIPS", "PDoC"));
    }

    private VisionBlock processVIPS(IDocument doc) {
        StyleSheet styleSheet = new StyleSheetFactory().createStyleSheet(doc);
        String referrer = doc.getReferrer();
        context.setStyleSheet(referrer, styleSheet);

        // Create extractor
        IElement body = doc.getBody();
        if (null != body) {
            Rectangle rect = RectangleFactory.getInstance().create(body);
            double pageSize = rect.getHeight() * rect.getWidth();
            context.getConsole().log("Page: " + rect + " Size: " + pageSize);
            double threshold = context.getConfigure().getDoubleProperty("VIPS", "RelativeSizeThreshold", 0.1);
            BlockExtractor extractor =
                    BlockExtractorFactory.getInstance().create(context, referrer, pageSize, threshold);

            // Set extractor
            segmenter.setExtractor(extractor);
            segmenter.pageSegment(doc);
        } else {
            l.info("This is not a HTML page, ignore..." + doc.getReferrer());
        }
    }

    public BrowserContext getContext() {
        return context;
    }

    public void setContext(BrowserContext context) {
        this.context = context;
    }

    public VisionBasedPageSegmenter getSegmenter() {
        return segmenter;
    }

    public void setSegmenter(VisionBasedPageSegmenter segmenter) {
        this.segmenter = segmenter;
    }
}
