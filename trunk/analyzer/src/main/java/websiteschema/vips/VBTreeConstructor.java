/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips;

import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author ray
 */
public class VBTreeConstructor {

    Logger l = Logger.getRootLogger();
    Map<String, Separator> repos = new HashMap<String, Separator>();

    public VisionBlock reconstructionVisionBlockTree(SeparatorList sepList, BlockPool pool) {
        VisionBlock root = copyBlock(pool.getRoot());
        List<Separator> sortedList = sepList.getSortedList();

        for (int i = 0; i < sortedList.size(); i++) {
            merge(sortedList, i);
        }

        for (int i = 0; i < sortedList.size(); i++) {
            Separator sep = sortedList.get(i);
            Set<VisionBlock> blocks = sep.getRelativeBlocks();
            VisionBlock blk = combine(blocks);
            if (null != sep.getParent()) {
                Separator parent = sep.getParent();
                parent.getRelativeBlocks().add(blk);
            } else {
                root.getChildren().add(blk);
            }
        }

        addTextBlockIntoVBTree(pool);
        return root;
    }

    private VisionBlock combine(Set<VisionBlock> blocks) {
        VisionBlock blk = new VisionBlock();
        if (blocks.size() > 1) {
            for (VisionBlock block : blocks) {
                blk.getChildren().add(block);
            }
        } else {
            blk = blocks.toArray(new VisionBlock[0])[0];
        }
        return blk;
    }

    private void merge(List<Separator> sortedSepList, int start) {
        Separator sep = sortedSepList.get(start);
        l.debug(sep.getRect());
        findParentAndAddAsChild(sep, sortedSepList.subList(start + 1, sortedSepList.size()));
    }

    private void findParentAndAddAsChild(Separator sep, List<Separator> subList) {
        Separator parent = null;
        int d = Integer.MAX_VALUE;
        for (Separator s2 : subList) {
            if (s2.getWeight() > sep.getWeight()) {
                if (s2.isVertical() == sep.isVertical()) {
                    int x1 = Math.abs(s2.getRect().getLeft() - sep.getRect().getLeft());
                    int x2 = Math.abs((s2.getRect().getLeft() + s2.getRect().getWidth()) - (sep.getRect().getLeft() + sep.getRect().getWidth()));
                    if (x1 < 5 || x2 < 5) {
                        int d1 = 0;
                        if (s2.isVertical()) {
                            if (s2.getRect().getLeft() > sep.getRect().getLeft()) {
                                d1 = s2.getRect().getLeft() - (sep.getRect().getLeft() + sep.getRect().getWidth());
                            } else {
                                d1 = sep.getRect().getLeft() - (s2.getRect().getLeft() + s2.getRect().getWidth());
                            }
                        } else {
                            if (s2.getRect().getTop() > sep.getRect().getTop()) {
                                d1 = s2.getRect().getTop() - (sep.getRect().getTop() + sep.getRect().getHeight());
                            } else {
                                d1 = sep.getRect().getTop() - (s2.getRect().getTop() + s2.getRect().getHeight());
                            }
                        }
                        if (d1 < d) {
                            d = d1;
                            parent = s2;
                        }
                    }
                } else if (sep.isVertical() && !s2.isVertical()) {
                    if (s2.getRect().getTop() == sep.getRect().getTop()) {
                        int d1 = sep.getRect().getTop() - s2.getRect().getTop();
                        if (0 <= d1 && d1 < 5) {
                            d = d1;
                            parent = s2;
                        }
                    }
                }
            }
        }
        if (null != parent) {
            sep.setParent(parent);
        }
    }

    public void addTextBlockIntoVBTree(BlockPool pool) {
    }

    private VisionBlock copyBlock(VisionBlock blk) {
        if (null != blk) {
            VisionBlock block = new VisionBlock();
            block.setLevel(blk.getLevel());
            block.setRect(blk.getRect());
            block.setEle(blk.getEle());
            block.setDoC(blk.getDoC());
            return block;
        } else {
            return null;
        }
    }
}
