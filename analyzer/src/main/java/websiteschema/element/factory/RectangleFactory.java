/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.element.factory;

import com.webrenderer.swing.dom.IElement;
import websiteschema.element.Rectangle;

/**
 *
 * @author ray
 */
public class RectangleFactory {

    public Rectangle create(IElement ele) {
        int h = (int) ele.getOffsetHeight();
        int w = (int) ele.getOffsetWidth();
        int l = getAbsoluteOffsetLeft(ele);
        int t = getAbsoluteOffsetTop(ele);
        return new Rectangle(h,w,l,t);
    }

    private int getAbsoluteOffsetLeft(IElement ele) {
        IElement pEle = ele.getOffsetParent();
        int offsetLeft = (int) ele.getOffsetLeft();
        if (null != pEle) {
            offsetLeft += getAbsoluteOffsetLeft(pEle);
        }
        return offsetLeft;
    }

    private int getAbsoluteOffsetTop(IElement ele) {
        IElement pEle = ele.getOffsetParent();
        int offsetTop = (int) ele.getOffsetTop();
        if (null != pEle) {
            offsetTop += getAbsoluteOffsetTop(pEle);
        }
        return offsetTop;
    }

}
