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
import websiteschema.utils.ElementUtil;
import websiteschema.vips.extraction.BlockExtractor;
import websiteschema.vips.extraction.rule.DivideRule;
import websiteschema.vips.extraction.VipsBlockExtractor;

/**
 *
 * @author ray
 */
public class VisualPageSegmenter {

    int PDoC;
    BlockPool pool = new BlockPool();
    BlockExtractor extractor = null;

    public SemanticBlock pageSegment(IDocument document) {
        initFirstBlock(document);

        oneRound();

        return pool.getRoot();
    }

    public void oneRound() {
        // Block Extraction
        int size = pool.getPool().size();
        for (int i = 0; i < size; i++) {
            SemanticBlock block = pool.getPool().get(i);
            divideDomTree(block.getEle(), 0);
        }

    }

    private void initFirstBlock(IDocument document) {
        IElement body = document.getBody();
        SemanticBlock block = new SemanticBlock();
        block.setEle(body);
        pool.add(block);
    }

    private void divideDomTree(IElement ele, int level) {
        DivideRule divide = dividable(ele, level);
        if (BlockExtractor.Dividable == divide.dividable()) {
            IElementCollection children = ele.getChildElements();
            for (int i = 0; i < children.length(); i++) {
                IElement child = children.item(i);
                divideDomTree(child, level + 1);
            }
        } else if (BlockExtractor.UnDividable == divide.dividable()) {
            SemanticBlock block = new SemanticBlock();
            block.setEle(ele);
            ElementUtil.getInstance().drawRectangleInPage(ele);
            int DoC = divide.getDoC(ele, level);
            block.setDoC(DoC);
            pool.add(block);
        } else {
            // do nothing for cutting node
            ElementUtil.getInstance().drawRectangleInPage(ele);
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

        List<SemanticBlock> pool = new ArrayList<SemanticBlock>();

        public void add(SemanticBlock block) {
            pool.add(block);
        }

        public List<SemanticBlock> getPool() {
            return pool;
        }

        public SemanticBlock getRoot() {
            return pool.get(0);
        }
    }
}
