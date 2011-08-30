/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import com.webrenderer.swing.dom.IDocument;
import com.webrenderer.swing.dom.IElement;
import websiteschema.element.Rectangle;
import websiteschema.element.factory.RectangleFactory;

/**
 *
 * @author ray
 */
public class ElementUtil {

    private final static ElementUtil instance = new ElementUtil();

    public static ElementUtil getInstance() {
        return instance;
    }

    public void drawRectangleInPage(IElement ele) {
        String lastStyle = ele.getAttribute("style", 0);
        String additionStyle = "border-style: solid; border-width: 3px;";
        if (null != lastStyle && !"".equals(lastStyle)) {
            ele.setAttribute("style", lastStyle + ";" + additionStyle, 0);
        } else {
            ele.setAttribute("style", additionStyle, 0);
        }
    }

    public double getPageSize(IDocument doc) {
        if (null != doc) {
            Rectangle rect = RectangleFactory.getInstance().create(doc.getBody());
            double pageSize = rect.getHeight() * rect.getWidth();
            return pageSize;
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }
}
