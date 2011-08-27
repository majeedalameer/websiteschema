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
import websiteschema.vips.extraction.BlockExtractor;

/**
 *
 * @author ray
 */
public class VisualPageSegmenter {

    int PDoC;
    BlockPool pool = new BlockPool();
    BlockExtractor extractor;

    public SemanticBlock pageSegment(IDocument document) {
        // Block Extraction
        divideDomTree(document.getBody(), 0);

        return null;
    }

    private void divideDomTree(IElement ele, int level) {
        int divide = dividable(ele, level);
        if (BlockExtractor.Dividable == divide) {
            IElementCollection children = ele.getChildElements();
            for (int i = 0; i < children.length(); i++) {
                IElement child = children.item(i);
                divideDomTree(child, level + 1);
            }
        } else if (BlockExtractor.UnDividable == divide) {
            SemanticBlock block = new SemanticBlock();
            block.setEle(ele);
            pool.add(block);
        } else {
            // do nothing for cutting node
        }
    }

    private int dividable(IElement ele, int level) {
        return extractor.dividable(ele, level);
    }

    public int getPDoC() {
        return PDoC;
    }

    public void setPDoC(int PDoC) {
        this.PDoC = PDoC;
    }

    class BlockPool {

        List<SemanticBlock> pool = new ArrayList<SemanticBlock>();

        public void add(SemanticBlock block) {
            pool.add(block);
        }
    }
}
