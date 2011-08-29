/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.vips.extraction.rule;

import com.webrenderer.swing.dom.IElement;
import websiteschema.vips.extraction.BlockExtractor;

/**
 * Rule4: If all of the child nodes of the DOM node are text nodes or virtual text nodes, do not divide the node.
 * @author ray
 */
public class Rule4 extends AbstractRule {

    @Override
    public boolean match(IElement ele, int level) {
        if (nodeFeature.areChildrenVirtualTextNode(ele)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int dividable() {
        return BlockExtractor.UnDividable;
    }
}
