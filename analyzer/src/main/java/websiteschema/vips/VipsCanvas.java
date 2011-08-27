/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips;

import com.webrenderer.swing.dom.IDocument;
import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import websiteschema.element.Rectangle;
import websiteschema.element.factory.RectangleFactory;

/**
 *
 * @author ray
 */
public class VipsCanvas extends Canvas {

    IDocument document = null;
    Rectangle size = null;

    public VipsCanvas() {
        this.setBackground(Color.white);
    }

    @Override
    public void paint(Graphics g) {
        System.out.println("paint VipsCanvas.");
        this.setBackground(Color.white);
        if (null != document) {
            IElement body = document.getBody();
            paintElement(g, body);
        }
    }

    private void paintElement(Graphics g, IElement ele) {
        Rectangle rect = new RectangleFactory().create(ele);
        g.drawRect(rect.getLeft(), rect.getTop(), rect.getWidth(), rect.getHeight());
        IElementCollection children = ele.getChildElements();
        if (null != children) {
            for (int i = 0; i < children.length(); i++) {
                IElement child = children.item(i);
                paintElement(g, child);
            }
        }
    }

    public void setDocument(IDocument document) {
        this.document = document;
        IElement body = document.getBody();
        size = new RectangleFactory().create(body);
        setSize(size.getWidth(), size.getHeight());
    }
}
