/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips;

import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import websiteschema.element.Rectangle;

/**
 *
 * @author ray
 */
public class SemanticBlock {

    int DoC;
    Rectangle rect;
    IElement ele;
    IElementCollection elements;
    SemanticBlock[] children;
    SemanticBlock parent;

    public int getDoC() {
        return DoC;
    }

    public void setDoC(int DoC) {
        this.DoC = DoC;
    }

    public SemanticBlock[] getChildren() {
        return children;
    }

    public void setChildren(SemanticBlock[] children) {
        this.children = children;
    }

    public IElement getEle() {
        return ele;
    }

    public void setEle(IElement ele) {
        this.ele = ele;
    }

    public IElementCollection getElements() {
        return elements;
    }

    public void setElements(IElementCollection elements) {
        this.elements = elements;
    }

    public SemanticBlock getParent() {
        return parent;
    }

    public void setParent(SemanticBlock parent) {
        this.parent = parent;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    
}
