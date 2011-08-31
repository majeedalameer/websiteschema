/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.analyzer.browser;

import java.util.Vector;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import websiteschema.vips.VisionBlock;

/**
 *
 * @author mupeng
 */
public class VipsTreeModel implements TreeModel{
    private boolean showAncestors;
    private Vector treeModelListeners = new Vector();
    private VisionBlock rootVisionBlock;

    public VipsTreeModel(VisionBlock root){
        showAncestors = false;
        rootVisionBlock = root;
    }
    /**
     * Used to toggle between show ancestors/show descendant and
     * to change the root of the tree.
     */
    public void showAncestor(boolean b, Object newRoot) {
        showAncestors = b;
        VisionBlock oldRoot = rootVisionBlock;
        if (newRoot != null) {
           rootVisionBlock = (VisionBlock)newRoot;
        }
        fireTreeStructureChanged(oldRoot);
    }

    /**
     * The only event raised by this model is TreeStructureChanged with the
     * root as path, i.e. the whole tree has changed.
     */
    protected void fireTreeStructureChanged(VisionBlock oldRoot) {
        int len = treeModelListeners.size();
        TreeModelEvent e = new TreeModelEvent(this,
                                              new Object[] {oldRoot});
        for (int i = 0; i < len; i++) {
            ((TreeModelListener)treeModelListeners.elementAt(i)).
                    treeStructureChanged(e);
        }
    }

    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     */
    public void addTreeModelListener(TreeModelListener l) {
        treeModelListeners.addElement(l);
    }

    /**
     * Returns the child of parent at index index in the parent's child array.
     */
    public Object getChild(Object parent, int index) {
        VisionBlock p = (VisionBlock)parent;
        if (showAncestors) {
            return p.getParent();
        }
        return p.getChild(index);
    }

    /**
     * Returns the number of children of parent.
     */
    public int getChildCount(Object parent) {
        VisionBlock p = (VisionBlock)parent;
        if (showAncestors) {
            int count = 0;
            if (p.getParent() != null) {
                count++;
            }
            return count;
        }
        return p.getChildCount();
    }

    /**
     * Returns the index of child in parent.
     */
    public int getIndexOfChild(Object parent, Object child) {
        VisionBlock p = (VisionBlock)parent;
        if (showAncestors) {
            int count = 0;
            VisionBlock father = p.getParent();
            if (father != null) {
                count++;
                if (father == child) {
                    return 0;
                }
            }
            return -1;
        }
        return p.getIndexOfChild((VisionBlock)child);
    }

    /**
     * Returns the root of the tree.
     */
    public Object getRoot() {
        return rootVisionBlock;
    }

    /**
     * Returns true if node is a leaf.
     */
    public boolean isLeaf(Object node) {
        VisionBlock p = (VisionBlock)node;
        if (showAncestors) {
            return ((p.getParent() == null));
        }
        return p.getChildCount() == 0;
    }

    /**
     * Removes a listener previously added with addTreeModelListener().
     */
    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.removeElement(l);
    }

    /**
     * Messaged when the user has altered the value for the item
     * identified by path to newValue.  Not used by this model.
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
        System.out.println("*** valueForPathChanged : "
                           + path + " --> " + newValue);
        
    }
}
