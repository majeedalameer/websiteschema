/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import websiteschema.element.Rectangle;

/**
 *
 * @author ray
 */
public class Separator {

    Rectangle rect;
    int weight = 0;
    Set<VisionBlock> relativeBlocks = new HashSet<VisionBlock>();
    Separator parent;
    List<Separator> children = new ArrayList<Separator>();

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Point getStartPixel() {
        return new Point(rect.getLeft(), rect.getTop());
    }

    public Point getEndPixel() {
        return new Point(rect.getLeft() + rect.getWidth(), rect.getTop() + rect.getHeight());
    }

    public Set<VisionBlock> getRelativeBlocks() {
        return relativeBlocks;
    }

    public void setRelativeBlocks(Set<VisionBlock> relativeBlocks) {
        this.relativeBlocks = relativeBlocks;
    }

    public List<Separator> getChildren() {
        return children;
    }

    public void setChildren(List<Separator> children) {
        this.children = children;
    }

    public Separator getParent() {
        return parent;
    }

    public void setParent(Separator parent) {
        this.parent = parent;
    }

    public boolean isVertical() {
        if (rect.getHeight() > rect.getWidth()) {
            return true;
        }

        return false;
    }

    public VisionBlock getTopBlock() {
        for (VisionBlock block : relativeBlocks) {
            Rectangle r = block.getRect();
            int y1 = r.getTop();
            int y3 = y1 + r.getHeight();
            {
                int _y1 = rect.getTop();
                if (y3 == _y1) {
                    return block;
                }
            }
        }
        return null;
    }

    public VisionBlock getLeftBlock() {
        for (VisionBlock block : relativeBlocks) {
            Rectangle r = block.getRect();
            int x1 = r.getLeft();
            int x2 = x1 + r.getWidth();
            {
                int _x1 = rect.getLeft();
                if (x2 == _x1) {
                    return block;
                }
            }
        }
        return null;
    }

    public VisionBlock getRightBlock() {
        for (VisionBlock block : relativeBlocks) {
            Rectangle r = block.getRect();
            int x1 = r.getLeft();
            {
                int _x1 = rect.getLeft();
                int _x2 = _x1 + rect.getWidth();
                if (x1 == _x2) {
                    return block;
                }
            }
        }
        return null;
    }

    public VisionBlock getBottomBlock() {
        for (VisionBlock block : relativeBlocks) {
            Rectangle r = block.getRect();
            int y1 = r.getTop();
            {
                int _y1 = rect.getTop();
                int _y3 = _y1 + rect.getHeight();
                if (y1 == _y3) {
                    return block;
                }
            }
        }
        return null;
    }

    private boolean isAjacent(Rectangle r1, Rectangle r2) {
        int x1 = r1.getLeft();
        int y1 = r1.getTop();
        int x2 = x1 + r1.getWidth();
        int y2 = y1;
        int x3 = x1;
        int y3 = y1 + r1.getHeight();
        int x4 = x2;
        int y4 = y3;

        if (isAjacent(x1, y1, x2, y2, r2)) {
            return true;
        } else if (isAjacent(x1, y1, x3, y3, r2)) {
            return true;
        } else if (isAjacent(x3, y3, x4, y4, r2)) {
            return true;
        } else if (isAjacent(x2, y2, x4, y4, r2)) {
            return true;
        }

        return false;
    }

    private boolean isAjacent(int x, int y, int x2, int y2, Rectangle r) {
        int _x1 = r.getLeft();
        int _y1 = r.getTop();
        int _x2 = _x1 + r.getWidth();
        int _y3 = _y1 + r.getHeight();

        if (y == y2) {
            if (y == _y1 || y == _y3) {
                return true;
            }
        } else if (x == x2) {
            if (x == _x1 || x == _x2) {
                return true;
            }
        }

        return false;
    }

    public boolean equals(Separator other) {
        String s1 = null != rect ? rect.toString() : null;
        String s2 = null != other.getRect() ? other.getRect().toString() : null;
        if(null != s1 && null != s2) {
            return s1.equals(s2);
        }
        return false;
    }

//    height: 20 width: 790 left: 255 top: 601
//    height: 16 width: 796 left: 252 top: 585
//    public static void main(String args[]) {
//        Rectangle r1 = new Rectangle(20, 790, 255, 601);
//        Rectangle r2 = new Rectangle(16, 796, 252, 585);
//        Separator sep = new Separator();
//        System.out.println(sep.isAjacent(r1,r2));
//    }
}
