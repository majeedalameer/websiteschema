/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips;

import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import java.util.ArrayList;
import java.util.List;
import websiteschema.element.Rectangle;

/**
 *
 * @author ray
 */
public class VisionBlock {

    int DoC;
    int level;
    Rectangle rect;
    IElement ele;
    IElementCollection elements;
    List<VisionBlock> children = new ArrayList<VisionBlock>();
    VisionBlock parent;

    public int getDoC() {
        return DoC;
    }

    public void setDoC(int DoC) {
        this.DoC = DoC;
    }

    public List<VisionBlock> getChildren() {
        return children;
    }

    public VisionBlock getChild(int index) {
        return children.get(index);
    }

    public int getChildCount() {
        return children.size();
    }

    public int getIndexOfChild(VisionBlock vb) {
        return children.indexOf(vb);
    }

    public void setChildren(List<VisionBlock> children) {
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

    public VisionBlock getParent() {
        return parent;
    }

    public void setParent(VisionBlock parent) {
        this.parent = parent;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "VB" + level + "(" + getDoC() + ")";
    }
}
