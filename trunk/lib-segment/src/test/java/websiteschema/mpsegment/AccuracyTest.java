/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment;

import junit.framework.Assert;
import org.junit.Test;
import websiteschema.mpsegment.conf.Configure;
import websiteschema.mpsegment.core.SegmentEngine;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.tools.PFRCorpusLoader;
import websiteschema.mpsegment.util.StringUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author ray
 */
public class AccuracyTest {

    private PFRCorpusLoader loader;
    private int totalWords = 0;
    private int correct = 0;
    private int wrong = 0;

    public AccuracyTest() throws UnsupportedEncodingException {
        loader = new PFRCorpusLoader(AccuracyTest.class.getClassLoader().getResourceAsStream("PFR-199801-utf-8.txt"));
    }

    @Test
    public void should_be_higher_accuracy_rate_than_0_dot_93() {
        boolean xingMingSeparate = Configure.getInstance().isXingMingSeparate();
        Configure.getInstance().setXingmingseparate(true);
        try {
            SegmentResult expectResult = loader.readLine();
            while (expectResult != null) {
                String sentence = expectResult.toOriginalString();
                SegmentResult actualResult = SegmentEngine.getInstance().getSegmentWorker().segment(sentence);

                totalWords += expectResult.length();

                compare(expectResult, actualResult);
                expectResult = loader.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
          Configure.getInstance().setXingmingseparate(xingMingSeparate);
        }
        double accuracyRate = (double) correct / (double) totalWords;
        System.out.println("Accuracy rate of segment is: " + accuracyRate);
        System.out.println("There are " + wrong + " errors and total expect word is " + totalWords + " when doing accuracy test.");

        Assert.assertTrue(correct > 0 && totalWords > 0);
        Assert.assertTrue(accuracyRate > 0.937);
    }

    private void compare(SegmentResult expectResult, SegmentResult actualResult) {
        int lastMatchIndex = -1;
        for (int i = 0; i < expectResult.length(); i++) {
            String expectWord = expectResult.getWord(i);
            int indexInOriginalString = expectResult.getWordIndexInOriginalString(i);
            int match = lookupMatch(actualResult, expectWord, lastMatchIndex + 1, indexInOriginalString);
            if (match >= 0) {
                lastMatchIndex = match;
                correct++;
            } else {
                wrong++;
            }
        }
    }

    private int lookupMatch(SegmentResult actualResult, String expectWord, int start, final int indexInOriginalString) {
        for (int i = start; i < actualResult.length(); i++) {
            String actualWord = actualResult.getWord(i);
            if (isSameWord(expectWord, actualWord)) {
                int indexInOriginalStringOfActualWord = actualResult.getWordIndexInOriginalString(i);
                if (indexInOriginalStringOfActualWord == indexInOriginalString) {
                    return i;
                }
            }
        }
        System.out.println(expectWord + " in " + getWord(actualResult, start, indexInOriginalString, indexInOriginalString + expectWord.length()));
        return -1;
    }

    private String getWord(SegmentResult actualResult, int start, int from, int to) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = start; i < actualResult.length(); i++) {
            int indexInOriginalString = actualResult.getWordIndexInOriginalString(i);
            if(indexInOriginalString >= from && indexInOriginalString < to) {
                stringBuilder.append(actualResult.getWord(i)).append(" ");
            }
        }
        return stringBuilder.toString();
    }

    private boolean isSameWord(String expect, String actual) {
        String expectWord = StringUtil.doUpperCaseAndHalfShape(expect);
        if(expectWord.equalsIgnoreCase(actual)) {
            return true;
        }
        return false;
    }
}
