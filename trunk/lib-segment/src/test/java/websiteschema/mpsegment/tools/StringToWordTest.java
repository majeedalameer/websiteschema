package websiteschema.mpsegment.tools;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.dict.POSUtil;

public class StringToWordTest {

    @Test
    public void should_convert_string_to_word_with_word_name_and_single_POS() {
        String wordString = "\"测试\" = {domainType:2,POSTable:{N:100,V:20}}";
        IWord word = new StringWordConverter().convert(wordString);

        Assert.assertEquals("测试", word.getWordName());
        Assert.assertEquals(2, word.getDomainType());
        int[][] posTable = word.getWordPOSTable();
        Assert.assertEquals(2, posTable.length);
        Assert.assertEquals(POSUtil.POS_N, posTable[0][0]);
        Assert.assertEquals(100, posTable[0][1]);
        Assert.assertEquals(POSUtil.POS_V, posTable[1][0]);
        Assert.assertEquals(20, word.getOccuredCount(POSUtil.getPOSString(POSUtil.POS_V)));
        Assert.assertEquals(120, word.getOccuredSum());
    }

    @Test
    public void should_convert_string_to_word_with_special_characters() {
        String wordString = "\",%22%28%29%5B%5D%7B%7D\" = {domainType:2}";
        IWord word = new StringWordConverter().convert(wordString);

        Assert.assertEquals(",\"()[]{}", word.getWordName());
        Assert.assertEquals(2, word.getDomainType());
    }
}
