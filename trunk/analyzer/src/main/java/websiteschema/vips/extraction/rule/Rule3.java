/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips.extraction.rule;

import com.webrenderer.swing.dom.IElement;

/**
 * Rule3: If the DOM node is the root node of the sub-DOM tree (corresponding to the block),
 *        and there is only one sub DOM tree corresponding to this block, divide this node.
 * @author ray
 */
public class Rule3 extends AbstractRule {

    @Override
    public boolean match(IElement ele, int level) {
        if (0 == level) {
            return true;
        } else {
            return false;
        }
    }
}
