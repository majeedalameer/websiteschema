/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.dict;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.conf.Configure;
import websiteschema.mpsegment.util.FileUtil;

/**
 * @author ray
 */
public class HeadIndexerTest {


    @Test
    public void should_build_HeadIndexer() {
        HeadIndexer headIndexer = new HeadIndexer(new WordImpl("词1"));
        headIndexer.add(new WordImpl("词3"));
        headIndexer.add(new WordImpl("词2"));
        headIndexer.add(new WordImpl("词4"));

        IWord word = headIndexer.findWord("词42");
        Assert.assertEquals("词4", word.getWordName());
    }

    @Test
    public void should_build_HeadIndexer_with_head_length() {
        HeadIndexer headIndexer = new HeadIndexer(new WordImpl("词12"), 2);
        headIndexer.add(new WordImpl("词13"));
        headIndexer.add(new WordImpl("词12"));
        headIndexer.add(new WordImpl("词14"));

        IWord word = headIndexer.findWord("词142");
        Assert.assertEquals("词14", word.getWordName());

        word = headIndexer.findWord("词242");
        Assert.assertNull(word);
    }
}
