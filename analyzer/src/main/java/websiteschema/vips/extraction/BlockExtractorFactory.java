/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips.extraction;

import com.webrenderer.swing.dom.IDocument;
import websiteschema.context.BrowserContext;
import websiteschema.element.Rectangle;
import websiteschema.element.factory.RectangleFactory;
import websiteschema.utils.Configure;

/**
 *
 * @author ray
 */
public class BlockExtractorFactory {

    private final static BlockExtractorFactory instance = new BlockExtractorFactory();

    public static BlockExtractorFactory getInstance() {
        return instance;
    }

    public BlockExtractor create(BrowserContext context, String referrer, double pageSize, double threshold) {
        VipsBlockExtractor extractor = new VipsBlockExtractor();
        extractor.setPageSize(pageSize);
        extractor.setContext(context);
        extractor.setReferrer(referrer);
        extractor.setThreshold(threshold);
        extractor.setConfigure(Configure.getDefaultConfigure());
        return extractor;
    }
}
