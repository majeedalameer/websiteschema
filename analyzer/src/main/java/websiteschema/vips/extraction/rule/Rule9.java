/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.vips.extraction.rule;

import com.webrenderer.swing.dom.IElement;
import websiteschema.vips.extraction.BlockExtractor;

/**
 * Rule9: If the child of the node with maximum size are smaller than a threshold(relative size), do not divide this node.
 * @author ray
 */
public class Rule9 extends AbstractRule {

    double pageSize;
    double threshold;

    public Rule9() {
        this(0.0, 0.0);
    }

    public Rule9(double pageSize, double threshold) {
        this.pageSize = pageSize;
        this.threshold = threshold;
    }

    @Override
    public boolean match(IElement ele, int level) {
        double relativeSize = nodeFeature.getRelativeSize(ele, pageSize);
        System.out.println("relativeSize: " + relativeSize + " PageSize: " + pageSize);
        if (relativeSize < threshold) {
            if (nodeFeature.hasTextOrVirtualTextNode(ele)) {
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