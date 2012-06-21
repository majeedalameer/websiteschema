/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips.extraction.rule;

import com.webrenderer.swing.dom.IElement;

/**
 * Rule6: If one of the child nodes of the DOM node has HTML tag <HR>, then divide this DOM node.
 * @author ray
 */
public class Rule6 extends AbstractRule {

    @Override
    public boolean match(IElement ele, int level) {
        if (nodeFeature.isChildNodeHRTag(ele)) {
            return true;
        } else {
            return false;
        }
    }
}
