/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips;

import java.awt.Point;
import websiteschema.element.Rectangle;

/**
 *
 * @author ray
 */
public class Seperator {

    Rectangle rect;
    int weight;

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

    public Point getStartPoint() {
        return new Point(rect.getLeft(), rect.getTop());
    }

    public Point getEndPoint() {
        return new Point(rect.getLeft() + rect.getWidth(), rect.getTop() + rect.getHeight());
    }
}