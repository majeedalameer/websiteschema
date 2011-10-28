/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;
import websiteschema.element.Rectangle;
import websiteschema.element.factory.XPathAttrFactory;

/**
 *
 * @author ray
 */
public class SeparatorList {

    Logger log = Logger.getRootLogger();
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

    public void expandAndRefineSeparator(List<VisionBlock> blocks) {
        expandSeparator();
        refineSeparator(blocks);
        removeSeparatorWhichAjacentBorder();
    }

    private void expandSeparator() {
        for (Iterator<Separator> it = list.iterator(); it.hasNext();) {
            Separator sep = it.next();
            Rectangle rect = sep.getRect();
            if (sep.isVertical()) {
                rect.setTop(0);
                rect.setHeight(page.getHeight());
            } else {
                rect.setLeft(0);
                rect.setWidth(page.getWidth());
            }
        }
    }

    private void refineSeparator(List<VisionBlock> blocks) {
        for (VisionBlock block : blocks) {
            evaluateBlock(block);
        }
    }

    public void removeSeparatorWhichAjacentBorder() {
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

    public void removeSeparatorWhichNoRelativeBlocks() {
        for (Iterator<Separator> it = list.iterator(); it.hasNext();) {
            Separator sep = it.next();
            if (sep.getRelativeBlocks().isEmpty()) {
                log.debug("remove separator which relative block is empty, and rectangle is " + sep.getRect());
                list.remove(sep);
            }
        }
    }

    public void setRelativeBlocks(List<VisionBlock> blocks) {
        for (VisionBlock block : blocks) {
            Rectangle rect = block.getRect();
            if (null != rect) {
                boolean isVertical = rect.getHeight() > rect.getWidth();
                Separator properSep = null;
                int d = Integer.MAX_VALUE;
                for (Iterator<Separator> it = list.iterator(); it.hasNext();) {
                    Separator sep = it.next();
                    if (isVertical && sep.isVertical()) {
                        if (isAjacentVertical(rect, sep.getRect())) {
                            properSep = sep;
                            break;
                        } else {
                            continue;
                        }
                    } else if (!isVertical && !sep.isVertical()) {
                        if (isAjacentHorizontal(rect, sep.getRect())) {
                            properSep = sep;
                            break;
                        }
                    } else if (isVertical && !sep.isVertical()) {
                        if (isAjacentHorizontal(rect, sep.getRect())) {
                            properSep = sep;
                            d = 0;
                        }
                    }
                    int d1 = getHorizontalDistance(rect, sep.getRect());
                    if (d1 < d) {
                        d = d1;
                        properSep = sep;
                    }
                }
                if (null != properSep) {
                    properSep.getRelativeBlocks().add(block);
                } else {
                    log.debug("can not find proper separator.\n" + rect);
                }
            }
        }
    }

    /**
     * hwlt(100,2,0,0) isAjacentVertical with hwlt(90,50,3,0)<br/>
     * hwlt = HeighWidthLeftTop
     * @param r
     * @param sep
     * @return
     */
    private boolean isAjacentVertical(Rectangle r, Rectangle sep) {
        int x1 = r.getLeft();
        int y1 = r.getTop();
        int x2 = x1 + r.getWidth();
        int y3 = y1 + r.getHeight();

        int _x1 = sep.getLeft();
        int _y1 = sep.getTop();
        int _x2 = _x1 + sep.getWidth();
        int _y3 = _y1 + sep.getHeight();

        if (_y1 <= y1 && y3 <= _y3) {
            int d = x1 - _x2;
            if (d >= 0 && d < 5) {
                return true;
            } else {
                d = _x1 - x2;
                if (d >= 0 && d < 5) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * hwlt(2,200,0,100) isAjacentHorizontal with hwlt(50,100,0,50)<br/>
     * hwlt = HeighWidthLeftTop
     * @param r
     * @param sep
     * @return
     */
    private boolean isAjacentHorizontal(Rectangle r, Rectangle sep) {
        int x1 = r.getLeft();
        int y1 = r.getTop();
        int x2 = x1 + r.getWidth();
        int y3 = y1 + r.getHeight();

        int _x1 = sep.getLeft();
        int _y1 = sep.getTop();
        int _x2 = _x1 + sep.getWidth();
        int _y3 = _y1 + sep.getHeight();

        if (_x1 <= x1 && x2 <= _x2) {
            int d = y1 - _y3;
            if (d >= 0 && d < 5) {
                return true;
            } else {
                d = _y1 - y3;
                if (d >= 0 && d < 5) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 2 = getHorizontalDistance(hwlt(50,100,0,48), hwlt(2,200,0,100))<br/>
     * hwlt = HeighWidthLeftTop
     * @param r
     * @param sep
     * @return
     */
    private int getHorizontalDistance(Rectangle r, Rectangle sep) {
        int x1 = r.getLeft();
        int y1 = r.getTop();
        int x2 = x1 + r.getWidth();
        int y3 = y1 + r.getHeight();

        int _x1 = sep.getLeft();
        int _y1 = sep.getTop();
        int _x2 = _x1 + sep.getWidth();
        int _y3 = _y1 + sep.getHeight();

        if (_x1 <= x1 && x2 <= _x2) {
            int d = y1 - _y3;
            if (d >= 0 && d < 5) {
                return d;
            } else {
                d = _y1 - y3;
                if (d >= 0 && d < 5) {
                    return d;
                }
            }
        }

        return Integer.MAX_VALUE;
    }

    public void caculateWeightOfSeparator() {
        for (Iterator<Separator> it = list.iterator(); it.hasNext();) {
            Separator sep = it.next();
            caculateWeightOfSeparator(sep);
        }
    }

    public List<Separator> getSortedList() {
        List<Separator> l = new ArrayList<Separator>(list);
        Collections.sort(l, new Comparator<Separator>() {

            @Override
            public int compare(Separator o1, Separator o2) {
                return o1.getWeight() - o2.getWeight();
            }
        });
        return l;
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
                    log.debug("SeparatorContainBlock");
                    Separator newSep = splitWhileContained(sep.getRect(), block.getRect());
//                    newSep.getRelativeBlocks().add(block);
//                    sep.getRelativeBlocks().add(block);
                } else if (doesBlockCoverSeparator(block, sep)) {
                    // If the block covers the separator, remove the separator.
                    log.debug("BlockCoverSeparator");
                    list.remove(sep);
                } else if (doesBlockAcrossSeparator(block, sep)) {
                    // If block across separator, then split separator.
                    log.debug("BlockAcrossSeparator");
                    Separator newSep = split(sep.getRect(), block.getRect());
//                    newSep.getRelativeBlocks().add(block);
//                    sep.getRelativeBlocks().add(block);
                } else {
                    // If the block crosses with the separator, update the separator's parameters.
                    log.debug("UpdateWhileBlockCrossedBorder");
                    doUpdateWhileBlockCrossedBorder(block.getRect(), sep.getRect());
//                    sep.getRelativeBlocks().add(block);
                }
//                caculateWeightOfSeparator(sep);
            }
        } else {
            String xpath = XPathAttrFactory.getInstance().create(block.getEle());
            log.debug(xpath + "  ---  is not leaf node.");
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

    private boolean isHeightLocateBetween(Rectangle r1, int t, int b) {
        int top = t;
        int bottom = b;
        if (t > b) {
            top = b;
            bottom = t;
        }
        if (r1.getTop() > top
                && r1.getTop() + r1.getHeight() < bottom) {
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
            log.debug("do nothing.");
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
//        sep.setWeight(rect.getHeight() > rect.getWidth() ? rect.getWidth() : rect.getHeight());
        sep.setWeight(rect.getHeight() * rect.getWidth());
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
