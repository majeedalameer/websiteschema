/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips;

import com.webrenderer.swing.dom.IDocument;
import com.webrenderer.swing.dom.IElement;
import org.apache.log4j.Logger;
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

    private VIPSContext context = null;
    private VisionBasedPageSegmenter segmenter = null;
    private Logger l = Logger.getRootLogger();
    private final static double standardPageSize = 1024 * 768;

    public VIPSImpl(VIPSContext context) {
        this.context = context;
        init();
    }

    public VisionBlock segment(IDocument doc, String url) {
        return processVIPS(doc, url);
    }

    /**
     * Creator VIPS Segmenter
     */
    private void init() {
        segmenter = new VisionBasedPageSegmenter(VIPSContext.getConfigure());
        segmenter.setPDoC(VIPSContext.getConfigure().getIntProperty("VIPS", "PDoC"));
    }

    private VisionBlock processVIPS(IDocument doc, String url) {
        StyleSheet styleSheet = new StyleSheetFactory().createStyleSheet(doc);
        context.setStyleSheet(url, styleSheet);

        // Create extractor
        IElement body = doc.getBody();
        if (null != body) {
            Rectangle rect = RectangleFactory.getInstance().create(body);
            double pageSize = rect.getHeight() * rect.getWidth();
            context.getConsole().log("Page: " + rect + " Size: " + pageSize);
            double threshold = VIPSContext.getConfigure().getDoubleProperty("VIPS", "RelativeSizeThreshold", 0.1);
            BlockExtractor extractor =
                    BlockExtractorFactory.getInstance().create(context, url, standardPageSize, threshold);

            // Set extractor
            segmenter.setExtractor(extractor);
            return segmenter.pageSegment(doc);
        } else {
            l.info("This is not a HTML page, ignore... " + url);
            return null;
        }
    
    }

    public VisionBasedPageSegmenter getSegmenter() {
        return segmenter;
    }

    public void setSegmenter(VisionBasedPageSegmenter segmenter) {
        this.segmenter = segmenter;
    }
}
