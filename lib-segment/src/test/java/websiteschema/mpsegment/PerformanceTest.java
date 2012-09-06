/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import junit.framework.Assert;
import org.junit.Test;
import websiteschema.mpsegment.core.SegmentEngine;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.core.SegmentWorker;

/**
 *
 * @author ray
 */
public class PerformanceTest {

    @Test
    public void should_segment_Sophies_World_within_1_seconds() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("Sophie's_World.txt"), "UTF-8"));
        SegmentWorker segmenter = SegmentEngine.getInstance().getSegmentWorker();
        segmenter.setRecognizePOS(false);
        segmenter.segment("世界您好！");
        String line;
        long beginTime = System.currentTimeMillis();
        int total = 0;
        do {
            line = reader.readLine();
            SegmentResult result = segmenter.segment(line);
            total += result.length();
        } while (null != line);
        long endTime = System.currentTimeMillis();
        long milSeconds = endTime - beginTime;
        System.out.println("should_segment_Sophies_World_within_1_seconds");
        System.out.println("    Spend total " + milSeconds + " ms.");
        System.out.println("    Segment words " + total + ", the velocity is " + (total * 1000 / milSeconds) + " num/sec.");
        Assert.assertTrue(milSeconds < 1000);
    }

    @Test
    public void should_segment_Sophies_World_with_POS_and_without_Domain_Dictionary_within_2_seconds() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("Sophie's_World.txt"), "UTF-8"));
        SegmentWorker segmenter = SegmentEngine.getInstance().getSegmentWorker();
        segmenter.setRecognizePOS(true);
        segmenter.setUseDomainDictionary(false);
        segmenter.segment("世界您好！");
        String line;
        long beginTime = System.currentTimeMillis();
        int total = 0;
        do {
            line = reader.readLine();
            SegmentResult result = segmenter.segment(line);
            total += result.length();
        } while (null != line);
        segmenter.setUseDomainDictionary(true);
        long endTime = System.currentTimeMillis();
        long milSeconds = endTime - beginTime;
        System.out.println("should_segment_Sophies_World_with_POS_and_without_Domain_Dictionary_within_2_seconds");
        System.out.println("    Spend total " + milSeconds + " ms.");
        System.out.println("    Segment words " + total + " with POS recognition and without Domain dictionary, the velocity is " + (total * 1000 / milSeconds) + " num/sec.");
        Assert.assertTrue(milSeconds < 2000);
    }

    @Test
    public void should_segment_Sophies_World_with_POS_within_2_seconds() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("Sophie's_World.txt"), "UTF-8"));
        SegmentWorker segmenter = SegmentEngine.getInstance().getSegmentWorker();
        segmenter.setRecognizePOS(true);
        segmenter.segment("世界您好！");
        String line;
        long beginTime = System.currentTimeMillis();
        int total = 0;
        do {
            line = reader.readLine();
            SegmentResult result = segmenter.segment(line);
            total += result.length();
        } while (null != line);
        long endTime = System.currentTimeMillis();
        long milSeconds = endTime - beginTime;
        System.out.println("should_segment_Sophies_World_with_POS_within_2_seconds");
        System.out.println("    Spend total " + milSeconds + " ms.");
        System.out.println("    Segment words " + total + " with POS recognition, the velocity is " + (total * 1000 / milSeconds) + " num/sec.");
        Assert.assertTrue(milSeconds < 2000);
    }

    @Test
    public void should_segment_Sophies_World_with_POS_and_Context_within_2_seconds() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("Sophie's_World.txt"), "UTF-8"));
        SegmentWorker segmenter = SegmentEngine.getInstance().getSegmentWorker();
        segmenter.setRecognizePOS(true);
        segmenter.setUseContextFreqSegment(true);
        segmenter.segment("世界您好！");
        String line;
        long beginTime = System.currentTimeMillis();
        int total = 0;
        do {
            line = reader.readLine();
            SegmentResult result = segmenter.segment(line);
            total += result.length();
        } while (null != line);
        segmenter.setUseContextFreqSegment(false);
        long endTime = System.currentTimeMillis();
        long milSeconds = endTime - beginTime;
        System.out.println("should_segment_Sophies_World_with_POS_and_Context_within_2_seconds");
        System.out.println("    Spend total " + milSeconds + " ms.");
        System.out.println("    Segment words " + total + " with POS recognition and context, the velocity is " + (total * 1000 / milSeconds) + " num/sec.");
        Assert.assertTrue(milSeconds < 2000);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Run for performance profile.");
        SegmentWorker segmenter = SegmentEngine.getInstance().getSegmentWorker();
        segmenter.setRecognizePOS(true);
        segmenter.segment("世界您好！");
        while (true) {
            segmenter.setUseContextFreqSegment(true);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                    PerformanceTest.class.getClassLoader().getResourceAsStream("Sophie's_World.txt"), "UTF-8"));

            String line;
            do {
                line = reader.readLine();
                segmenter.segment(line);
            } while (null != line);
            segmenter.setUseContextFreqSegment(false);
        }
    }
}
