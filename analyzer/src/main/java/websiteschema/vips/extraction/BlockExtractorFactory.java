/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips.extraction;

//import websiteschema.analyzer.context.BrowserContext;

import websiteschema.vips.VIPSContext;


/**
 *
 * @author ray
 */
public class BlockExtractorFactory {

    private final static BlockExtractorFactory instance = new BlockExtractorFactory();

    public static BlockExtractorFactory getInstance() {
        return instance;
    }

    public BlockExtractor create(VIPSContext context, String referrer, double pageSize, double threshold) {
        VipsBlockExtractor extractor = new VipsBlockExtractor();
        extractor.setPageSize(pageSize);
        extractor.setContext(context);
        extractor.setReferrer(referrer);
        extractor.setThreshold(threshold);
        extractor.setConfigure(VIPSContext.getConfigure());
        return extractor;
    }
}
