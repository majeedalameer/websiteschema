/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips.extraction.rule;

import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import java.util.Set;
import websiteschema.element.factory.XPathAttrFactory;
import websiteschema.vips.extraction.BlockExtractor;

/**
 * If previous sibling node has not bean divided, do not divide this node.
 * @author ray
 */
public class Rule10 extends AbstractRule {

    Set<String> allDividableNode = null;

    @Override
    public boolean match(IElement ele, int level) {
        IElement parent = ele.getParentElement();
        if(null != parent) {
            IElementCollection children = parent.getChildElements();
            IElement silbling = children.item(0);
            String xpath = XPathAttrFactory.getInstance().create(silbling);
            return !allDividableNode.contains(xpath);
        }
        return false;
    }

    @Override
    public int dividable() {
        return BlockExtractor.UnDividable;
    }

    public Set<String> getAllDividableNode() {
        return allDividableNode;
    }

    public void setAllDividableNode(Set<String> allDividableNode) {
        this.allDividableNode = allDividableNode;
    }

}
