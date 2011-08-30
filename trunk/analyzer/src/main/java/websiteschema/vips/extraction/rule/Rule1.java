/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips.extraction.rule;

import com.webrenderer.swing.dom.IElement;
import websiteschema.element.factory.XPathFactory;
import websiteschema.vips.extraction.BlockExtractor;

/**
 * Rule 1: if the DOM node is not a text node and it has no valid children,<br/>
 *         then this node cannot be divided and will be cut.
 * @author ray
 */
public class Rule1 extends AbstractRule {

    @Override
    public boolean match(IElement ele, int level) {
        if (!nodeFeature.isTextNode(ele) && !nodeFeature.hasValidChildren(ele)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int dividable() {
        return BlockExtractor.Cut;
    }
}
