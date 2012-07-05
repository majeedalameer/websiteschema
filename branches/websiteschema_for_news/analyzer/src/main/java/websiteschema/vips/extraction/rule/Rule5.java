/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips.extraction.rule;

import com.webrenderer.swing.dom.IElement;

/**
 * Rule5: If one of the child nodes of the DOM node is line-break node, then divide this DOM node.
 * @author ray
 */
public class Rule5 extends AbstractRule {

    @Override
    public boolean match(IElement ele, int level) {
        if (nodeFeature.hasLineBreakChildNode(ele)) {
            //Rule5: If one of the child nodes of the DOM node is line-break node, then divide this DOM node.
            return true;
        } else {
            return false;
        }
    }
}
