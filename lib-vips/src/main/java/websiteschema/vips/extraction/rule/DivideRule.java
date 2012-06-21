/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.vips.extraction.rule;

import com.webrenderer.swing.dom.IElement;

/**
 *
 * @author ray
 */
public interface DivideRule {

    /**
     * If DOM node match divide rule, the return true.<br/>
     * @param ele
     * @param level
     * @return
     */
    public boolean match(IElement ele, int level);

    /**
     * Determine whether divide the node if matched this rule.
     * @return
     */
    public int dividable();

    /**
     * Compute the Degree of Coherence of this node.
     * @param ele
     * @param level
     * @return
     */
    public int getDoC(IElement ele, int level);

}
