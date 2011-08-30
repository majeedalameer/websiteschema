/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips.extraction.rule;

import com.webrenderer.swing.dom.IElement;
import websiteschema.element.Rectangle;
import websiteschema.element.factory.RectangleFactory;
import websiteschema.vips.extraction.BlockExtractor;
import websiteschema.vips.NodeFeature;

/**
 *
 * @author ray
 */
public abstract class AbstractRule implements DivideRule {

    NodeFeature nodeFeature = NodeFeature.getInstance();

    @Override
    public int dividable() {
        return BlockExtractor.Dividable;
    }

    @Override
    public int getDoC(IElement ele, int level) {
        if (BlockExtractor.Dividable == dividable()
                || BlockExtractor.Cut == dividable()) {
            return 0;
        } else {
            return getDoCBasedTagAndSize(ele, level);
        }
    }
    private static final int standardPageSize = 1024 * 768;

    private int getDoCBasedTagAndSize(IElement ele, int level) {
        int DoC = 0;
        Rectangle rect = RectangleFactory.getInstance().create(ele);
        int size = rect.getHeight() * rect.getWidth();
        double relativeSize = (double)size / (double)standardPageSize;
        if (relativeSize >= 1.0) {
            return 1;
        } else {
            DoC = (int) ((1 - relativeSize) * 8);
        }
        boolean hasSmallChildren = nodeFeature.areChildrenSmallNode(ele);
        if (hasSmallChildren) {
            DoC += 2;
        }
        if (nodeFeature.hasLineBreakChildNode(ele)) {
            DoC -= 1;
        }

//        if(DoC == 0) {
//            System.out.println("DoC = 0 ????");
//        }
        return DoC;
    }
}
