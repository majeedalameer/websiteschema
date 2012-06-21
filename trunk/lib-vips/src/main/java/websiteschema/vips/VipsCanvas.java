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
import java.util.List;
import websiteschema.element.Rectangle;
import websiteschema.element.factory.RectangleFactory;

/**
 *
 * @author ray
 */
public class VipsCanvas extends Canvas {

    IDocument document = null;
    SeparatorList sepList = null;
    Rectangle size = null;
    List<VisionBlock> blockList = null;

    public VipsCanvas() {
        this.setBackground(Color.white);
    }

    @Override
    public void paint(Graphics g) {
//        System.out.println("paint VipsCanvas.");
        this.setBackground(Color.white);
//        if (null != document) {
//            IElement body = document.getBody();
//            paintElement(g, body);
//        }
        paintSeparator(g);
        paintBlock(g);
    }

    private void paintBlock(Graphics g) {
        g.setColor(Color.BLACK);
        for (VisionBlock blk : blockList) {
            if (0 == blk.getChildCount()) {
                Rectangle rect = blk.getRect();
                if (null != rect) {
                    g.drawRect(rect.getLeft(), rect.getTop(), rect.getWidth(), rect.getHeight());
                }
            }
        }
    }

    private void paintSeparator(Graphics g) {
        g.setColor(Color.RED);
        List<Separator> list = sepList.list;
        for (Separator sep : list) {
            Rectangle rect = sep.getRect();
            g.fillRect(rect.getLeft(), rect.getTop(), rect.getWidth(), rect.getHeight());
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

    public void setSeparatorList(SeparatorList sepList) {
        this.sepList = sepList;
    }

    public void setBlockList(List<VisionBlock> blockList) {
        this.blockList = blockList;
    }
}
