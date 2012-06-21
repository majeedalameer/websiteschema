/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips.extraction;

import websiteschema.vips.extraction.rule.DivideRule;
import com.webrenderer.swing.dom.IElement;

/**
 *
 * @author ray
 */
public interface BlockExtractor {

    public final static int Dividable = -1;
    public final static int UnDividable = -2;
    public final static int Cut = -3;

    public DivideRule dividable(IElement ele, int level);
}
