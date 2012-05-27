/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import websiteschema.element.Rectangle;

/**
 *
 * @author ray
 */
public class BlockPool {

    List<VisionBlock> pool = new ArrayList<VisionBlock>();

    public void add(VisionBlock block) {
        pool.add(block);
    }

    public List<VisionBlock> getPool() {
        return pool;
    }

    public VisionBlock getRoot() {
        return pool.get(0);
    }

    public boolean isLeafNode(VisionBlock block) {
        return null == block.getChildren() || block.getChildren().isEmpty();
    }

    public List<VisionBlock> getLeafNodes() {
        List<VisionBlock> leafs = new ArrayList<VisionBlock>();

        for (VisionBlock blk : pool) {
            if (isLeafNode(blk)) {
                leafs.add(blk);
            }
        }

        return leafs;
    }
}
