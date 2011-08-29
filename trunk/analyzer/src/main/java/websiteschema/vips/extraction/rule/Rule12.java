/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.vips.extraction.rule;

import com.webrenderer.swing.dom.IElement;
import websiteschema.vips.extraction.BlockExtractor;

/**
 *
 * @author ray
 */
public class Rule12 extends AbstractRule {

    @Override
    public boolean match(IElement ele, int level) {
        return true;
    }

    @Override
    public int dividable() {
        return BlockExtractor.UnDividable;
    }
}
