/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips;

import com.webrenderer.swing.dom.IDocument;
import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import org.apache.log4j.Logger;
import websiteschema.context.BrowserContext;
import websiteschema.element.factory.RectangleFactory;
import websiteschema.element.factory.XPathFactory;
import websiteschema.utils.ElementUtil;
import websiteschema.vips.extraction.BlockExtractor;
import websiteschema.vips.extraction.rule.DivideRule;

/**
 *
 * @author ray
 */
public class VisionBasedPageSegmenter {

    Logger l = Logger.getRootLogger();
    int PDoC;
    BlockPool pool = new BlockPool();
    SeparatorList separatorList = null;
    BlockExtractor extractor = null;
    VBTreeConstructor constructor = new VBTreeConstructor();
    int iterateTimes = BrowserContext.getConfigure().getIntProperty("VIPS", "IterateTimes", 5);
    boolean showsUp = BrowserContext.getConfigure().getBooleanProperty("VIPS", "ShowsUp", true);

    public VisionBlock pageSegment(IDocument document) {
        init(document);
        debugFrame(document);

        int times = 0;
        do {
            l.debug(" ---- ---- iterate time: " + times);
            // Block Extraction.
            extractBlock();
        } while (!meetGranularityNeed() && ++times < iterateTimes);

        // Separator Detection.
        separatorDetect();
        // Content Structure Construction.
        VisionBlock blk = constructor.reconstructionVisionBlockTree(separatorList, pool);

        return blk;
    }

    private void debugFrame(IDocument document) {
        if (showsUp) {
            VipsFrame vipsFrame = new VipsFrame();
            VipsCanvas vipsCanvas = new VipsCanvas();
            vipsFrame.setCanvas(vipsCanvas);
            vipsFrame.setVisible(true);
            vipsFrame.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
            vipsCanvas.setDocument(document);
            vipsCanvas.setSeparatorList(separatorList);
            vipsCanvas.setBlockList(pool.getPool());
        }
    }

    private void separatorDetect() {
        int size = pool.getPool().size();
        for (int i = 0; i < size; i++) {
            VisionBlock block = pool.getPool().get(i);
            separatorList.initPageSize(block.getRect());
        }

        for (int i = 0; i < size; i++) {
            VisionBlock block = pool.getPool().get(i);
            if (pool.isLeafNode(block)) {
                separatorList.evaluateBlock(block);
            }
        }

        separatorList.removeSeparatorWhichAjacentBorder();
        separatorList.expandAndRefineSeparator(pool.getLeafNodes());
        separatorList.setRelativeBlocks(pool.getLeafNodes());
        separatorList.removeSeparatorWhichNoRelativeBlocks();
        separatorList.caculateWeightOfSeparator();
    }

    private void extractBlock() {
        int size = pool.getPool().size();
        for (int i = 0; i < size; i++) {
            VisionBlock block = pool.getPool().get(i);
            l.trace(block.getEle() + " Level: " + block.getLevel() + " DoC:" + block.getDoC());
            if (pool.isLeafNode(block) && !meetGranularityNeed(block)) {
                divideDomTree(block.getEle(), 0, block);
            }
        }
    }

    public void drawBorder() {
        int size = pool.getPool().size();
        for (int i = 0; i < size; i++) {
            VisionBlock block = pool.getPool().get(i);
            if (pool.isLeafNode(block)) {
                ElementUtil.getInstance().drawRectangleInPage(block.getEle(), "red");
            }
        }
    }

    private boolean meetGranularityNeed() {
        int size = pool.getPool().size();
        for (int i = 0; i < size; i++) {
            VisionBlock block = pool.getPool().get(i);
            if (pool.isLeafNode(block) && !meetGranularityNeed(block)) {
                l.trace("doesn't meet granularity need. " + XPathFactory.getInstance().create(block.getEle()));
                return false;
            }
        }
        return true;
    }

    private boolean meetGranularityNeed(VisionBlock block) {
        return block.getDoC() >= PDoC;
    }

    private void init(IDocument document) {
        separatorList = new SeparatorList();

        IElement body = document.getBody();
        VisionBlock block = new VisionBlock();
        block.setEle(body);
        block.setRect(RectangleFactory.getInstance().create(body));
        pool.add(block);
        IDocument[] childFrames = document.getChildFrames();
        if (null != childFrames) {
            for (IDocument doc : childFrames) {
                IElement ele = doc.getBody();
                if (null != ele) {
                    if (NodeFeature.getInstance().isValidNode(ele)) {
                        VisionBlock b = new VisionBlock();
                        b.setEle(ele);
                        b.setDoC(8);
                        block.setRect(RectangleFactory.getInstance().create(ele));
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
                block.setRect(RectangleFactory.getInstance().create(ele));
                int DoC = divide.getDoC(ele, level);
                block.setDoC(DoC);
                block.setLevel(level);
                // Add child to ancestor.
                ancestor.getChildren().add(block);
                pool.add(block);

                String xpath = XPathFactory.getInstance().create(block.getEle());
                l.debug(xpath + " Level: " + block.getLevel() + " DoC:" + block.getDoC());
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
}
