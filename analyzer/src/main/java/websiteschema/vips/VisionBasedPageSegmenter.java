/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips;

import com.webrenderer.swing.dom.IDocument;
import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import java.util.ArrayList;
import java.util.List;
import websiteschema.element.factory.XPathFactory;
import websiteschema.utils.Configure;
import websiteschema.utils.ElementUtil;
import websiteschema.vips.extraction.BlockExtractor;
import websiteschema.vips.extraction.rule.DivideRule;

/**
 *
 * @author ray
 */
public class VisionBasedPageSegmenter {

    int PDoC;
    BlockPool pool = new BlockPool();
    BlockExtractor extractor = null;
    int iterateTimes = Configure.getDefaultConfigure().getIntProperty("VIPS", "IterateTimes", 5);

    public VisionBlock pageSegment(IDocument document) {
        initBlocks(document);

        int times = 0;
        do {
            System.out.println("time: " + times);
            oneRound();
        } while (!meetGranularityNeed() && ++times < iterateTimes);

        return pool.getRoot();
    }

    public void oneRound() {
        // Block Extraction
        int size = pool.getPool().size();
        for (int i = 0; i < size; i++) {
            VisionBlock block = pool.getPool().get(i);
//            System.out.println(XPathFactory.getInstance().create(block.getEle() + " Level: " + block.getLevel() + " DoC:" + block.getDoC());
            if (isLeafNode(block) && block.getDoC() < getPDoC()) {
                divideDomTree(block.getEle(), 0, block);
            }
        }

    }

    public void drawBorder() {
        int size = pool.getPool().size();
        for (int i = 0; i < size; i++) {
            VisionBlock block = pool.getPool().get(i);
            ElementUtil.getInstance().drawRectangleInPage(block.getEle());
        }
    }

    private boolean meetGranularityNeed() {
        int size = pool.getPool().size();
        for (int i = 0; i < size; i++) {
            VisionBlock block = pool.getPool().get(i);
            if (isLeafNode(block)) {
                if (!meetGranularityNeed(block)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean meetGranularityNeed(VisionBlock block) {
        return block.getDoC() >= PDoC;
    }

    private boolean isLeafNode(VisionBlock block) {
        return null == block.getChildren() || block.getChildren().isEmpty();
    }

    private void initBlocks(IDocument document) {
        IElement body = document.getBody();
        VisionBlock block = new VisionBlock();
        block.setEle(body);
        pool.add(block);
        IDocument[] childFrames = document.getChildFrames();
        if (null != childFrames) {
            for (IDocument doc : childFrames) {
                IElement ele = doc.getBody();
                if (null != ele) {
                    if (NodeFeature.getInstance().isValidNode(ele)) {
                        VisionBlock b = new VisionBlock();
                        b.setEle(ele);
                        pool.add(b);
                    }
                }
            }
        }
    }

    private void divideDomTree(IElement ele, int level, VisionBlock ancestor) {
        DivideRule divide = dividable(ele, level);
        if (null != divide) {
            if (BlockExtractor.Dividable == divide.dividable()) {
                IElementCollection children = ele.getChildElements();
                for (int i = 0; i < children.length(); i++) {
                    IElement child = children.item(i);
                    divideDomTree(child, level + 1, ancestor);
                }
            } else if (BlockExtractor.UnDividable == divide.dividable()) {
                VisionBlock block = new VisionBlock();
                block.setEle(ele);
                int DoC = divide.getDoC(ele, level);
                block.setDoC(DoC);
                block.setLevel(level);
                // Add child to ancestor.
                ancestor.getChildren().add(block);
                pool.add(block);

                String xpath = XPathFactory.getInstance().create(block.getEle());
                System.out.println(xpath + " Level: " + block.getLevel() + " DoC:" + block.getDoC());
            } else {
                // do nothing for cutting node
                //ElementUtil.getInstance().drawRectangleInPage(ele);
            }
        }
    }

    private DivideRule dividable(IElement ele, int level) {
        return extractor.dividable(ele, level);
    }

    public int getPDoC() {
        return PDoC;
    }

    public void setPDoC(int PDoC) {
        this.PDoC = PDoC;
    }

    public BlockExtractor getExtractor() {
        return extractor;
    }

    public void setExtractor(BlockExtractor extractor) {
        this.extractor = extractor;
    }

    class BlockPool {

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
}
