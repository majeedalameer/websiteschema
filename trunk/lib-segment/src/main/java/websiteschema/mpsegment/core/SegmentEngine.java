package websiteschema.mpsegment.core;

import java.util.concurrent.locks.ReentrantLock;
import websiteschema.mpsegment.dict.DictionaryFactory;

public class SegmentEngine {

    private final static ThreadLocal<SegmentWorker> workers = new ThreadLocal();
    private final static SegmentEngine self = new SegmentEngine();
    private final ReentrantLock lock = new ReentrantLock();
    private int numWorker = 0;

    private SegmentEngine() {
        DictionaryFactory.getInstance().loadDictionary();
        DictionaryFactory.getInstance().loadDomainDictionary();
        DictionaryFactory.getInstance().loadUserDictionary();
    }

    public static SegmentEngine getInstance() {
        return self;
    }

    public void freeWorker(SegmentWorker segmentworker) {
    }

    public SegmentWorker getSegmentWorker() {
        SegmentWorker segmentWorker = null;

        segmentWorker = workers.get();
        if (null == segmentWorker) {

            lock.lock();
            try {
                segmentWorker = new SegmentWorker();
                workers.set(segmentWorker);
                numWorker++;
            } finally {
                lock.unlock();
            }

        }

        return segmentWorker;
    }
}
