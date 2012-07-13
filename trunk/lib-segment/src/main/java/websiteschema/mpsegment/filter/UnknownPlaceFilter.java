/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.dict.POSUtil;

/**
 *
 * @author ray
 */
public class UnknownPlaceFilter {

    private void processNS() {
        int length = segmentResult.length();
        for (int wordI = 0; wordI < length; wordI++) {
            if (wordPosIndexes[wordI] <= 0) {
                int pos = segmentResult.getPOS(wordI);
                if (pos == POSUtil.POS_NS && wordI + 1 < length) {
                    String word = segmentResult.getWord(wordI);
                    if (segmentResult.getWord(wordI + 1).length() == 1 && adminLevels.indexOf(segmentResult.getWord(wordI + 1)) > 0 && word.lastIndexOf(segmentResult.getWord(wordI + 1)) != length - 1) {
                        modifyWordPosIndexes(wordI, wordI + 1, POSUtil.POS_NS);
                    }
                }
            }
        }

    }

    private void modifyWordPosIndexes(int wordI1, int wordI2, int pos) {
        int length = segmentResult.length();
        int posIndex = pos;
        if (wordI1 < 0 || wordI2 >= length || wordI1 >= wordI2) {
            return;
        }
        if (wordPosIndexes[wordI1] > 0) {
            int posI1 = wordPosIndexes[wordI1];
            for (wordI1--; wordI1 >= 0 && wordPosIndexes[wordI1] == posI1; wordI1--);
            wordI1++;
        }
        if (wordI1 >= 1 && wordPosIndexes[wordI1 - 1] > 0 && wordPosIndexes[wordI1 - 1] < 200) {
            posIndex += 200;
        }
        for (int k2 = wordI1; k2 <= wordI2; k2++) {
            wordPosIndexes[k2] = posIndex;
        }

    }

    private static String adminLevels = " 省市县区乡镇村旗州";
    private int wordPosIndexes[];
    private SegmentResult segmentResult;

}
