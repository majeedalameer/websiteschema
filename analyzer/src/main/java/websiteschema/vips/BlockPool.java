/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips;

import java.util.ArrayList;
import java.util.List;

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
}
