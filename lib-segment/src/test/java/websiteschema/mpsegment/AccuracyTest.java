/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment;

import junit.framework.Assert;
import org.junit.Test;
import websiteschema.mpsegment.tools.SegmentAccuracy;

import java.io.IOException;

/**
 * @author ray
 */
public class AccuracyTest {

    @Test
    public void should_be_higher_accuracy_rate_than_0_dot_93() throws IOException {
        SegmentAccuracy segmentAccuracy = new SegmentAccuracy("PFR-199801-utf-8.txt");
        segmentAccuracy.checkSegmentAccuracy();
        System.out.println("Accuracy rate of segment is: " + segmentAccuracy.getAccuracyRate());
        System.out.println("There are " + segmentAccuracy.getWrong() + " errors and total expect word is " + segmentAccuracy.getTotalWords() + " when doing accuracy test.");

        Assert.assertTrue(segmentAccuracy.getAccuracyRate() > 0.937);

        System.out.println("There are " + segmentAccuracy.getErrorNewWord() + " errors because of new word");
        System.out.println("There are " + segmentAccuracy.getErrorContain() + " errors because of contain disambiguate");
        System.out.println("There are " + segmentAccuracy.getErrorOther() + " other errors");

        System.out.println("Possible " + segmentAccuracy.getPossibleNewWords().size() + "new words:");
        System.out.println(segmentAccuracy.getPossibleNewWords());
        System.out.println("Those " + segmentAccuracy.getWordsWithContainDisambiguate().size() + " words maybe could delete from dictionary: ");
        System.out.println(segmentAccuracy.getWordsWithContainDisambiguate());
    }
}
