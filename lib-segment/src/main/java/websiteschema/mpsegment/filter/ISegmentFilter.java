package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.core.SegmentResult;

public interface ISegmentFilter {

    public abstract void filter(SegmentResult segmentResult);
}