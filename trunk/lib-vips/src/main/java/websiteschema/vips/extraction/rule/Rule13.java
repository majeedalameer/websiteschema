/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips.extraction.rule;

import com.webrenderer.swing.dom.IElement;
import websiteschema.vips.extraction.BlockExtractor;

/**
 * If this DOM node's all valid children are small node, do not divide this node.
 * @author ray
 */
public class Rule13 extends AbstractRule {

    @Override
    public boolean match(IElement ele, int level) {
        if (nodeFeature.areChildrenSmallNode(ele)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int dividable() {
        return BlockExtractor.UnDividable;
    }

    @Override
    public int getDoC(IElement ele, int level) {
        int DoC = super.getDoC(ele, level);

        DoC += 3;

        if (DoC > 10) {
            DoC = 10;
        }

        return DoC;
    }
}
