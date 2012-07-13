/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment;

import websiteschema.mpsegment.dict.domain.DomainDictionary;
import websiteschema.mpsegment.dict.domain.DomainDictFactory;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.core.SegmentEngine;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.core.SegmentWorker;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.conf.Configure;

/**
 *
 * @author ray
 */
public class DictionaryTest {

    @Test
    public void should_Loaded_Some_Words_from_User_Dictionary() {
        String str = "贝因美是中国品牌";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();
        worker.setUseDomainDictionary(true);
        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.length(), 4);
        Assert.assertEquals(words.getWord(0), "贝因美");
        Assert.assertEquals(words.getWord(1), "是");
        DomainDictionary dd = DomainDictFactory.getInstance().getDomainDictionary();

        List<IWord> list = dd.getAllWords();
        assert (list.size() > 1);
    }

    @Test
    public void should_Loaded_Some_Synonyms_Like_PC() {
        SegmentEngine.getInstance(); // Init dictionary
        try {
            List<IWord> syns = DomainDictFactory.getInstance().getDomainDictionary().getSynonymSet("个人电脑");
            Assert.assertEquals(syns.get(0).getWordName(), "PC机");
            Assert.assertEquals(syns.get(1).getWordName(), "个人电脑");
        } catch (Exception ex) {
            Assert.fail();
        }
        try {
            List<IWord> syns = DomainDictFactory.getInstance().getDomainDictionary().getSynonymSet("PC机");
            Assert.assertEquals(syns.get(0).getWordName(), "PC机");
            Assert.assertEquals(syns.get(1).getWordName(), "个人电脑");
        } catch (Exception ex) {
            Assert.fail();
        }
    }
}
