/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.browser.listener;

import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.event.MouseEvent;
import com.webrenderer.swing.event.MouseListener;
import org.apache.log4j.Logger;
import websiteschema.context.BrowserContext;
import websiteschema.element.Rectangle;
import websiteschema.element.RectangleFactory;
import websiteschema.element.XPathAttributes;
import websiteschema.element.XPathFactory;

/**
 *
 * @author ray
 */
public class SimpleMouseListener implements MouseListener {

    XPathAttributes attr = new XPathAttributes();
    Logger l = Logger.getRootLogger();
    BrowserContext context;

    public SimpleMouseListener(BrowserContext context) {
        attr.setUsingClass(true);
        attr.setUsingId(true);
        attr.setSpecifyAttr("name");
        this.context = context;
    }

    @Override
    public void onClick(MouseEvent me) {
        l.debug("mouse onclick");
        IElement ele = me.getTargetElement();
        Rectangle rect = new RectangleFactory().create(ele);
        String xpath = new XPathFactory().create(ele, attr);
        l.debug("xpath: " + xpath);
        System.out.println("xpath: " + xpath);
        l.debug(rect);
    }

    @Override
    public void onDoubleClick(MouseEvent me) {
//        l.debug("mouse ondoubleclick");
    }

    @Override
    public void onMouseDown(MouseEvent me) {
//        l.debug("mouse onMouseDown");
    }

    @Override
    public void onMouseUp(MouseEvent me) {
//        l.debug("mouse onMouseUp");
    }
}
