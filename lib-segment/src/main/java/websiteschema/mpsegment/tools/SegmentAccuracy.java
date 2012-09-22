/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.tools;

import websiteschema.mpsegment.conf.Configure;
import websiteschema.mpsegment.core.SegmentEngine;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.util.StringUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ray
 */
public class SegmentAccuracy {

    private PFRCorpusLoader loader;
    private int totalWords = 0;
    private int correct = 0;
    private int wrong = 0;
    private double accuracyRate;
    private int errorNewWord = 0;
    private int errorContain = 0;
    private int errorOther = 0;

    private Set<String> possibleNewWords = new HashSet<String>();
    private Set<String> wordsWithContainDisambiguate = new HashSet<String>();

    public SegmentAccuracy(String testCorpus) throws IOException {
        loader = new PFRCorpusLoader(getClass().getClassLoader().getResourceAsStream("PFR-199801-utf-8.txt"));
    }

    public void checkSegmentAccuracy() {
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
        assert (correct > 0 && totalWords > 0);
        accuracyRate = (double) correct / (double) totalWords;
    }

    public double getAccuracyRate() {
        return accuracyRate;
    }

    public int getWrong() {
        return wrong;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public int getErrorNewWord() {
        return errorNewWord;
    }

    public int getErrorContain() {
        return errorContain;
    }

    public int getErrorOther() {
        return errorOther;
    }

    public Set<String> getPossibleNewWords() {
        return possibleNewWords;
    }

    public Set<String> getWordsWithContainDisambiguate() {
        return wordsWithContainDisambiguate;
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
        analyzeErrorReason(actualResult, expectWord, start, indexInOriginalString, indexInOriginalString + expectWord.length());
        return -1;
    }

    private String analyzeErrorReason(SegmentResult actualResult, String expect, int start, int from, int to) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = start; i < actualResult.length(); i++) {
            int indexInOriginalString = actualResult.getWordIndexInOriginalString(i);
            if (indexInOriginalString >= from && indexInOriginalString < to) {
                stringBuilder.append(actualResult.getWord(i)).append(" ");
            }
        }

        String errorSegment = stringBuilder.toString().trim();

        if (errorSegment.length() == 0) {
            // Bigger word contains the littler words.
            // Should remove the word from dictionary.
            errorContain++;
//            System.out.println(expect + " in " + errorSegment);
        } else if (errorSegment.replaceAll(" ", "").equals(expect)) {
            // Need to add expected word into dictionary
            // Or found a new word
            errorNewWord++;
            possibleNewWords.add(expect);
//            System.out.println(expect + " in " + errorSegment);
        } else if (errorSegment.contains(expect)){
            errorContain++;
            wordsWithContainDisambiguate.add(errorSegment);
//            System.out.println(expect + " in " + errorSegment);
        } else {
            errorOther++;
            System.out.println(expect + " in " + errorSegment);
        }

        return errorSegment;
    }

    private boolean isSameWord(String expect, String actual) {
        String expectWord = StringUtil.doUpperCaseAndHalfShape(expect);
        if (expectWord.equalsIgnoreCase(actual)) {
            return true;
        }
        return false;
    }
}
