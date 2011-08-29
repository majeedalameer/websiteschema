/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.vips.extraction.rule;

import com.webrenderer.swing.dom.IElement;
import websiteschema.vips.extraction.BlockExtractor;
import websiteschema.vips.NodeFeature;

/**
 *
 * @author ray
 */
public abstract class AbstractRule implements DivideRule{

    NodeFeature nodeFeature = NodeFeature.getInstance();

    @Override
    public int dividable() {
        return BlockExtractor.Dividable;
    }

    @Override
    public int getDoC(IElement ele, int level) {
        return 0;
    }

}
