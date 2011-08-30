/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips.extraction.rule;

import com.webrenderer.swing.dom.IElement;
import org.apache.log4j.Logger;
import websiteschema.vips.extraction.BlockExtractor;

/**
 * Rule8: If the node has at least one text node child or at least one virtual text node child,
 *        and the node's relative size is smaller than a threshold, the the node can not be divided.
 * @author ray
 */
public class Rule8 extends AbstractRule {

    Logger l = Logger.getRootLogger();
    double pageSize;
    double threshold;

    public Rule8() {
        this(0.0, 0.0);
    }

    public Rule8(double pageSize, double threshold) {
        this.pageSize = pageSize;
        this.threshold = threshold;
    }

    @Override
    public boolean match(IElement ele, int level) {
        double relativeSize = nodeFeature.getRelativeSize(ele, pageSize);
        if (relativeSize < threshold) {
            if (nodeFeature.hasTextOrVirtualTextNode(ele)) {
                l.trace("relativeSize: " + relativeSize + " PageSize: " + pageSize);
                return true;
            }
        }
        return false;
    }

    @Override
    public int dividable() {
        return BlockExtractor.UnDividable;
    }

    public double getPageSize() {
        return pageSize;
    }

    public void setPageSize(double pageSize) {
        this.pageSize = pageSize;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
