/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import websiteschema.element.Rectangle;
import websiteschema.element.factory.XPathFactory;

/**
 *
 * @author ray
 */
public class SeparatorList {

    List<Separator> list = new CopyOnWriteArrayList<Separator>();
    Rectangle page = null;

    public SeparatorList() {
        page = new Rectangle(0, 0, 0, 0);
        Separator sep = new Separator();
        sep.setRect(new Rectangle(0, 0, 0, 0));
        add(sep);
    }

    public void initPageSize(Rectangle rect) {
        if (null != rect) {
            int x4 = rect.getLeft() + rect.getWidth();
            int y4 = rect.getTop() + rect.getHeight();

            int _x4 = page.getWidth();
            int _y4 = page.getHeight();

            if (x4 > _x4) {
                page.setWidth(x4);
                list.get(0).getRect().setWidth(x4);
            }

            if (y4 > _y4) {
                page.setHeight(y4);
                list.get(0).getRect().setHeight(y4);
            }
        }
    }

    public final void add(Separator sep) {
        list.add(sep);
    }

    public void removeSeparatorAjacentBorder() {
        for (Iterator<Separator> it = list.iterator(); it.hasNext();) {
            Separator sep = it.next();
            Rectangle rect = sep.getRect();
            if ((rect.getLeft() == 0 && rect.getHeight() > rect.getWidth())
                    || (rect.getTop() == 0) && rect.getWidth() > rect.getHeight()) {
                list.remove(sep);
            } else if (((rect.getLeft() + rect.getWidth()) == page.getWidth() && rect.getHeight() > rect.getWidth())
                    || (rect.getTop() + rect.getHeight()) == page.getHeight() && rect.getWidth() > rect.getHeight()) {
                list.remove(sep);
            }
        }
    }

    /**
     * For every block in the pool, the relation of the block with each separator is evaluated.
     * @param block
     */
    public void evaluateBlock(VisionBlock block) {
        if (null != block.getRect() && !block.getEle().isTextNode()) {
            for (Iterator<Separator> it = list.iterator(); it.hasNext();) {
                Separator sep = it.next();
                if (doesSeparatorContainBlock(block, sep)) {
                    // If the block is contained in the separator, split the separator.
                    System.out.println("SeparatorContainBlock");
                    Separator newSep = splitWhileContained(sep.getRect(), block.getRect());
                    newSep.getRelativeBlocks().add(block);
                    sep.getRelativeBlocks().add(block);
                } else if (doesBlockCoverSeparator(block, sep)) {
                    // If the block covers the separator, remove the separator.
                    System.out.println("BlockCoverSeparator");
                    list.remove(sep);
                } else if (doesBlockAcrossSeparator(block, sep)) {
                    // If block across separator, then split separator.
                    System.out.println("BlockAcrossSeparator");
                    Separator newSep = split(sep.getRect(), block.getRect());
                    newSep.getRelativeBlocks().add(block);
                    sep.getRelativeBlocks().add(block);
                } else {
                    // If the block crosses with the separator, update the separator's parameters.
                    System.out.println("UpdateWhileBlockCrossedBorder");
                    doUpdateWhileBlockCrossedBorder(block.getRect(), sep.getRect());
                    sep.getRelativeBlocks().add(block);
                }
                caculateWeightOfSeparator(sep);
            }
        } else {
            String xpath = XPathFactory.getInstance().create(block.getEle());
            System.out.println(xpath + "  ---  is not leaf node.");
        }
    }

    /**
     * Split rect1 with rect2<br/>
     * 这是简化的判别方法，前提是两个矩形是交叉的。
     * @param rect1
     * @param rect2
     * @return
     */
    private Separator split(Rectangle rect1, Rectangle rect2) {
        if (rect1.getLeft() < rect2.getLeft()) {
            // split vertical
            int h = rect1.getHeight();
            int w = rect1.getWidth() - rect2.getWidth() - (rect2.getLeft() - rect1.getLeft());
            int l = rect2.getLeft() + rect2.getWidth();
            int t = rect1.getTop();
            Separator sep = new Separator();
            sep.setRect(new Rectangle(h, w, l, t));
            caculateWeightOfSeparator(sep);
            add(sep);
            rect1.setWidth(rect2.getLeft() - rect1.getLeft());
            return sep;
        } else {
            // split horizontal
            int h = rect1.getHeight();
            int w = rect1.getWidth() - rect2.getWidth() - (rect2.getLeft() - rect1.getLeft());
            int l = rect2.getLeft() + rect2.getWidth();
            int t = rect1.getTop();
            Separator sep = new Separator();
            sep.setRect(new Rectangle(h, w, l, t));
            caculateWeightOfSeparator(sep);
            add(sep);
            rect1.setHeight(rect2.getTop() - rect1.getTop());
            return sep;
        }
    }

    /**
     * Split rect1 with rect2
     * @param rect1
     * @param rect2
     * @return 
     */
    private Separator splitWhileContained(Rectangle rect1, Rectangle rect2) {
        if (rect2.getHeight() > rect2.getWidth()) {
            // split vertical
            int h = rect1.getHeight();
            int w = rect1.getWidth() - rect2.getWidth() - (rect2.getLeft() - rect1.getLeft());
            int l = rect2.getLeft() + rect2.getWidth();
            int t = rect1.getTop();
            Separator sep = new Separator();
            sep.setRect(new Rectangle(h, w, l, t));
            caculateWeightOfSeparator(sep);
            add(sep);
            rect1.setWidth(rect2.getLeft() - rect1.getLeft());
            return sep;
        } else {
            // split horizontal
            int h = rect1.getHeight() - rect2.getHeight() - (rect2.getTop() - rect1.getTop());
            int w = rect1.getWidth();
            int l = rect1.getLeft();
            int t = rect2.getTop() + rect2.getHeight();
            Separator sep = new Separator();
            sep.setRect(new Rectangle(h, w, l, t));
            caculateWeightOfSeparator(sep);
            add(sep);
            rect1.setHeight(rect2.getTop() - rect1.getTop());
            return sep;
        }
    }

    /**
     * Return true if separator contains block.
     * @param block
     * @param sep
     * @return
     */
    private boolean doesSeparatorContainBlock(VisionBlock block, Separator sep) {
        return isContained(block.getRect(), sep.getRect());
    }

    /**
     * Return true if separator contains block.
     * @param block
     * @param sep
     * @return
     */
    private boolean doesBlockCoverSeparator(VisionBlock block, Separator sep) {
        return isContained(sep.getRect(), block.getRect());
    }

    private boolean doesBlockAcrossSeparator(VisionBlock block, Separator sep) {
        return isAcross(block.getRect(), sep.getRect());
    }

    /**
     * Return true if rect2 contains rect1.
     * @param block
     * @param sep
     * @return
     */
    private boolean isContained(Rectangle rect1, Rectangle rect2) {
        int x1 = rect2.getLeft();
        int y1 = rect2.getTop();
        int x2 = rect2.getLeft() + rect2.getWidth();
        int y2 = rect2.getTop() + rect2.getHeight();

        int x3 = rect1.getLeft();
        int y3 = rect1.getTop();
        int x4 = rect1.getLeft() + rect1.getWidth();
        int y4 = rect1.getTop() + rect1.getHeight();

        if ((x1 <= x3 && y1 <= y3) && (x4 <= x2 && y4 <= y2)) {
            return true;
        }

        return false;
    }

    /**
     * Return true if rect1 acrossed rect2.
     * @param block
     * @param sep
     * @return
     */
    public boolean isAcross(Rectangle rect1, Rectangle rect2) {
        Rectangle higher = rect1;
        Rectangle lower = rect2;
        if (rect1.getHeight() >= rect2.getHeight()) {
            higher = rect1;
            lower = rect2;
        } else {
            higher = rect2;
            lower = rect1;
        }

        if (isWidthLocateBetween(higher, lower.getLeft(), lower.getLeft() + lower.getWidth())) {
            if (isHeightLocateBetween(lower, higher.getTop(), higher.getTop() + higher.getHeight())) {
                return true;
            }
        }

        return false;
    }

    /**
     * If r1's left &gt; left and r1's right &lt; right, return true.
     * @param r1
     * @param r2
     * @returnthe lower, return true.
     */
    private boolean isWidthLocateBetween(Rectangle r1, int l, int r) {
        int left = l;
        int right = r;
        if (left > right) {
            left = r;
            right = l;
        }
        if (r1.getLeft() > left
                && r1.getLeft() + r1.getWidth() < right) {
            return true;
        }
        return false;
    }

    private boolean isHeightLocateBetween(Rectangle r1, int t, int l) {
        int top = t;
        int lower = l;
        if (t > l) {
            top = l;
            lower = t;
        }
        if (r1.getTop() > top
                && r1.getTop() + r1.getHeight() < lower) {
            return true;
        }
        return false;
    }

    /**
     * If rect1 cross the border of rect2, then update parameters of rect2.
     * @param block
     * @param sep
     * @return
     */
    public void doUpdateWhileBlockCrossedBorder(Rectangle rect1, Rectangle rect2) {
        int _x1 = rect1.getLeft();
        int _y1 = rect1.getTop();
        int _x2 = rect1.getLeft() + rect1.getWidth();
        int _y2 = _y1;
        int _x3 = _x1;
        int _y3 = rect1.getTop() + rect1.getHeight();
        int _x4 = _x2;
        int _y4 = _y3;

        if (isInArea(_x1, _y1, rect2)) {

            int size1 = (_y1 - rect2.getTop()) * rect2.getWidth();
            int size2 = rect2.getHeight() * (_x1 - rect2.getLeft());

            if (size1 >= size2) {
                rect2.setHeight(_y1 - rect2.getTop());
            } else {
                rect2.setWidth(_x1 - rect2.getLeft());
            }
        } else if (isInArea(_x2, _y2, rect2)) {
            int size1 = (_y2 - rect2.getTop()) * rect2.getWidth();
            int size2 = rect2.getHeight() * (rect1.getWidth() - (_x2 - rect2.getLeft()));
            if (size1 >= size2) {
                rect2.setHeight(_y2 - rect2.getTop());
            } else {
                rect2.setWidth(rect1.getWidth() - (_x2 - rect2.getLeft()));
                rect2.setLeft(_x2);
            }
        } else if (isInArea(_x3, _y3, rect2)) {

            int size1 = (rect2.getHeight() - (_y3 - rect2.getTop())) * rect2.getWidth();
            int size2 = rect2.getHeight() * (_x3 - rect2.getLeft());

            if (size1 >= size2) {
                rect2.setHeight(rect2.getHeight() - (_y3 - rect2.getTop()));
                rect2.setTop(_y3);
            } else {
                rect2.setWidth(_x3 - rect2.getLeft());
            }
        } else if (isInArea(_x4, _y4, rect2)) {
            int size1 = (rect2.getHeight() - (_y4 - rect2.getTop())) * rect2.getWidth();
            int size2 = rect2.getHeight() * (rect1.getWidth() - (_x4 - rect2.getLeft()));
            if (size1 >= size2) {
                rect2.setHeight(rect2.getHeight() - (_y4 - rect2.getTop()));
                rect2.setTop(_y4);
            } else {
                rect2.setWidth(rect1.getWidth() - (_x4 - rect2.getLeft()));
            }
        } else {
            System.out.println("do nothing.");
        }
    }

    public boolean isInArea(int x, int y, Rectangle rect) {
        int x1 = rect.getLeft();
        int y1 = rect.getTop();
        int x2 = rect.getLeft() + rect.getWidth();
        int y2 = y1;
        int x3 = x1;
        int y3 = rect.getTop() + rect.getHeight();
        int x4 = x2;
        int y4 = y3;

        if (x > x1 && x < x2) {
            if (y > y1 && y < y3) {
                return true;
            }
        }

        return false;
    }

    private void caculateWeightOfSeparator(Separator sep) {
        Rectangle rect = sep.getRect();
        sep.setWeight(rect.getHeight() > rect.getWidth() ? rect.getWidth() : rect.getHeight());
    }

//    public static void main(String args[]) {
//        Rectangle page = new Rectangle(768, 1024, 0, 0);
//        Rectangle r1 = new Rectangle(100, 500, 0, 100);
//        Rectangle r2 = new Rectangle(500, 100, 100, 0);
//        SeparatorList list = new SeparatorList();
//        list.initPageSize(page);
//        System.out.println(list.isAcross(r1, r2));
//        System.out.println(list.isAcross(r2, r1));
//    }
}
