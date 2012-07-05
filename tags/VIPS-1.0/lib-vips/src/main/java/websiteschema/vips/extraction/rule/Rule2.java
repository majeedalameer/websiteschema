/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips.extraction.rule;

import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;

/**
 * Rule2: If the DOM node has only child and the child is not a text node, then divide the node.
 * @author ray
 */
public class Rule2 extends AbstractRule {

    @Override
    public boolean match(IElement ele, int level) {
        if (1 == nodeFeature.howManyChildren(ele)) {
            IElementCollection children = ele.getChildElements();
            IElement onlyChild = children.item(0);
            if (!nodeFeature.isVirtualTextNode(onlyChild)) {
                return true;
            }
        }
        return false;
    }

}
