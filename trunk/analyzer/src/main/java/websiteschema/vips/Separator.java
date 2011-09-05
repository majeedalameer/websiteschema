/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips;

import java.awt.Point;
import java.util.HashSet;
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
}
