/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.vips.extraction;

import com.webrenderer.swing.dom.IElement;

/**
 *
 * @author ray
 */
public interface BlockExtractor {

    public final static int Dividable = 2;
    public final static int UnDividable = 1;
    public final static int Cut = 0;

    public int dividable(IElement ele, int level);

}
