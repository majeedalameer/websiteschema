package websiteschema.mpsegment.core;

import websiteschema.mpsegment.filter.SegmentResultFilter;

public class SegmentWorker {

    public SegmentWorker() {
        unKnownFilter = null;
        maxSegStrLength = 400000;
        mpSegment = new MPSegment();
        unKnownFilter = new SegmentResultFilter();
    }

    public void setUseDomainDictionary(boolean flag) {
        mpSegment.setUseDomainDictionary(flag);
    }

    public boolean getUseDomainDictionary() {
        return mpSegment.isUseDomainDictionary();
    }

    public SegmentResult segment(String sentence) {
        SegmentResult result = null;
        if (sentence != null && sentence.length() > 0) {
            if (sentence.length() > maxSegStrLength) {
                sentence = sentence.substring(0, maxSegStrLength);
            }
            result = mpSegment.segmentMP(sentence, recognizePOS);
            unKnownFilter.filter(result);
        } else {
            result = new SegmentResult(0);
        }
        return result;
    }

    public boolean isUseContextFreqSegment() {
        return mpSegment.isUseContextFreqSegment();
    }

    public void setUseContextFreqSegment(boolean useContextFreqSegment) {
        mpSegment.setUseContextFreqSegment(useContextFreqSegment);
    }
    private SegmentResultFilter unKnownFilter;
    private int maxSegStrLength;
    private MPSegment mpSegment;
    private boolean recognizePOS = true;
}
