/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.conf.Configure;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.dict.POSAndFreq;
import websiteschema.mpsegment.dict.HashDictionary;
import websiteschema.mpsegment.util.FileUtil;

/**
 * @author ray
 */
public class HashDictionaryTest {

    static HashDictionary hashDictionary = null;

    static {
        try {
            hashDictionary = loadDictionary();
//            System.out.println(hashDictionary.dictoString());
        } catch (Exception e) {
            assert (false);
        }
    }

    @Test
    public void should_Load_Core_Dictionary() {

        IWord[] words = hashDictionary.getWordItems("乒乓球");
        assert (null != words);
        Assert.assertEquals(words[0].getWordName(), "乒乓球");
        Assert.assertEquals(words[1].getWordName(), "乒乓");
        for (IWord word : words) {
            System.out.println("词：" + word.getWordName() + "\n" + word.getPOSArray());
        }
    }

    @Test
    public void should_contains_word_() {
        IWord[] words = hashDictionary.getWordItems("丘吉尔");
        for (IWord word : words) {
            System.out.println("词：" + word.getWordName() + "\n" + word.getPOSArray());
        }
    }

    @Test
    public void should_Load_POS_and_Freq_Which_Top_is_98350() {
        String segment_dict = Configure.getInstance().getSegmentDict();
        String segment_dict_fre = (new StringBuilder(String.valueOf(FileUtil.removeExtension(segment_dict)))).append(".fre").toString();
        POSAndFreq.loadPOSDb(segment_dict_fre);
        System.out.println(POSAndFreq.toText());
        Assert.assertEquals(POSAndFreq.getTop(), 98350);
    }

    private static HashDictionary loadDictionary() {
        String segment_dict = Configure.getInstance().getSegmentDict();
        String segment_dict_fre = (new StringBuilder(String.valueOf(FileUtil.removeExtension(segment_dict)))).append(".fre").toString();
        long l1 = System.currentTimeMillis();
        POSAndFreq.loadPOSDb(segment_dict_fre);
        HashDictionary dict = new HashDictionary(Configure.getInstance().getSegmentDict());

        l1 = System.currentTimeMillis() - l1;
        System.out.println("loaded hash dictionary successful, elapse " + l1);
        return dict;
    }
}
