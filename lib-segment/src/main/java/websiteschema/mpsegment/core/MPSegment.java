package websiteschema.mpsegment.core;

import websiteschema.mpsegment.conf.Configure;
import websiteschema.mpsegment.dict.DictionaryFactory;
import websiteschema.mpsegment.dict.HashDictionary;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.dict.domain.DomainDictFactory;
import websiteschema.mpsegment.dict.domain.DomainDictionary;
import websiteschema.mpsegment.graph.Dijkstra;
import websiteschema.mpsegment.graph.IGraph;
import websiteschema.mpsegment.graph.Path;
import websiteschema.mpsegment.graph.SingleMatrixGraph;

public class MPSegment {

    public MPSegment() {
        maxWordLength = 18;
        useCache = true;
        lastSection = false;
        lastSectionStr = "";
        logCorpus = 0.0D;
        useDomainDictionary = true;
        initialize();
    }

    private void initialize() {
        initializeGraph();
        initializeCache();
        logCorpus = Math.log(CorpusSize) * 100;
        maxWordLength = Configure.getInstance().isSegmentMin() ? 4 : Configure.getInstance().getMaxWordLength();
        initializePOSTagging();
    }

    private void initializeCache() {
        if (useCache) {
            objectCache = new UnknownWordCache(1024);
            objectCache.setCache(true);
        } else {
            objectCache = new UnknownWordCache();
            objectCache.setCache(false);
        }
    }

    private HashDictionary getHashDictionary() {
        return DictionaryFactory.getInstance().getCoreDictionary();
    }

    private DomainDictionary getDomainDictionary() {
        return DomainDictFactory.getInstance().getDomainDictionary();
    }

    private void initializeGraph() {
        graph = new SingleMatrixGraph(1024);
        dijk = new Dijkstra(1024);
    }

    private void initializePOSTagging() {
        posTagging = new POSRecognizer();
    }

    public int getLogCorpus() {
        return (int) logCorpus;
    }

    public IWord getItem(String word) {
        IWord iworditem = null;
        if ((loadDomainDictionary || loadUserDictionary) && word.length() > 1) {
            iworditem = getDomainDictionary().getWordItem(word);
        }
        if (iworditem == null) {
            iworditem = getHashDictionary().getWordItem(word);
        }
        return iworditem;
    }

    private void buildGraph(final String sen, final int startPos) {
        GraphBuilder builder = new GraphBuilder(graph, logCorpus, useDomainDictionary, objectCache);
        builder.setUseContextFreqSegment(useContextFreqSegment);
        builder.buildGraph(sen, startPos);
    }

    private SegmentResult sectionSegmentWithPOS(String sentence) {
        SegmentResult result = new SegmentResult();
        lastSectionStr = "";
        int length = sentence.length();
        buildGraph(sentence, 0);
        dijk.setGraph(graph);
        int endVertex = -2;
        if (!lastSection) {
            endVertex = graph.getStopVertex(length - 20, length);
            if (endVertex > 1 && endVertex > length - 20 && endVertex < length) {
                lastSectionStr = sentence.substring(endVertex - 1);
            } else {
                lastSectionStr = "";
                endVertex = length + 1;
            }
        } else {
            endVertex = length + 1;
        }
        Path p = dijk.getShortestPath(1, endVertex);
        if (p.getLength() < 0) {
            lastSectionStr = "";
            p = dijk.getShortestPath(1, length + 1);
        }
        setPathMarks(p, result);
        result.setPOSArray(posTagging.findPOS(p, graph));
        clear();
        return result;
    }

    private SegmentResult sectionSegment(String sentence) {
        SegmentResult result = new SegmentResult();
        if (lastSectionStr != null && lastSectionStr.length() > 0) {
            sentence = (new StringBuilder(String.valueOf(lastSectionStr))).append(sentence).toString();
            lastSectionStr = "";
        }
        int length = sentence.length();
        buildGraph(sentence, 0);
        dijk.setGraph(graph);
        int endVertex = -2;
        if (!lastSection) {
            endVertex = graph.getStopVertex(length - 20, length);
            if (endVertex > 1 && endVertex > length - 20 && endVertex < length) {
                lastSectionStr = sentence.substring(endVertex - 1);
            } else {
                lastSectionStr = "";
                endVertex = length + 1;
            }
        } else {
            endVertex = length + 1;
        }
        path = dijk.getShortestPath(1, endVertex);
        setPathMarks(path, result);
        clear();
        return result;
    }

    private Path getShortestPath(String sentence) {
        buildGraph(sentence, 0);
        int sentenceLength = sentence.length();
        dijk.setGraph(graph);
        Path p = dijk.getShortestPath(1, sentenceLength + 1);
        return p;
    }

    private SegmentResult segmentWithPOS(String sentence) {
        SegmentResult result = new SegmentResult();
        dijk.setGraph(graph);
        Path p = getShortestPath(sentence);
        setPathMarks(p, result);
        result.setPOSArray(posTagging.findPOS(p, graph));
        clear();
        return result;
    }

    private void clear() {
        graph.clear();
        objectCache.clear();
    }

    private SegmentResult segment(String sentence) {
        SegmentResult result = new SegmentResult();
        Path p = getShortestPath(sentence);
        setPathMarks(p, result);
        clear();
        return result;
    }

    public SegmentResult segmentMP(String sentence, boolean withPOS) {
        if (sentence == null || sentence.length() < 1) {
            return null;
        }
        int sentenceLength = sentence.length();
        SegmentResult result;
        if (sentenceLength < 1023) {
            lastSection = true;
            lastSectionStr = "";
            result = withPOS ? segmentWithPOS(sentence) : segment(sentence);
        } else {
            result = new SegmentResult();
            lastSectionStr = "";
            int sections = sentenceLength / 1000;
            if (sentenceLength != sections * 1000) {
                sections++;
            }
            lastSection = false;

            int startIndex = 0;
            for (; sentenceLength > 0; sentenceLength -= 1000) {
                String sectionedSentence;
                if (sentenceLength > 1000) {
                    sectionedSentence = sentence.substring(startIndex, startIndex + 1000);
                    startIndex += 1000;
                } else {
                    sectionedSentence = sentence.substring(startIndex);
                    lastSection = true;
                }
                SegmentResult sectionResult = withPOS ? sectionSegmentWithPOS(sectionedSentence) : sectionSegment(sectionedSentence);
                result.append(sectionResult);
                sectionResult = null;
                if (!lastSection && lastSectionStr.length() > 0) {
                    startIndex -= lastSectionStr.length();
                    sentenceLength += lastSectionStr.length();
                }
            }
        }
        return result;
    }

    private void setPathMarks(Path path, SegmentResult wordatoms) {
        int length = path.getLength();
        String wordNames[] = new String[length];
        int log2Freqs[] = new int[length];
        if (length < 1) {
            return;
        }
        for (int j2 = 0; j2 < length; j2++) {
            int edgeWeight = graph.getEdgeWeight(path.iget(j2), path.iget(j2 + 1));
            if (edgeWeight == 0) {
                IWord iworditem = (IWord) graph.getEdgeObject(path.iget(j2), path.iget(j2 + 1));
                wordNames[j2] = iworditem.getWordName();
                log2Freqs[j2] = iworditem.getDomainType();
            } else {
                IWord iworditem1 = (IWord) graph.getEdgeObject(path.iget(j2), path.iget(j2 + 1));
                wordNames[j2] = iworditem1.getWordName();
                log2Freqs[j2] = iworditem1.getDomainType();
            }
        }

        wordatoms.setWords(wordNames);
        wordatoms.setConcepts(log2Freqs);
    }

    public int getTF(String word) {
        IWord iworditem = null;
        int i1 = 0;
        if ((loadDomainDictionary || loadUserDictionary) && word.length() > 1) {
            iworditem = getDomainDictionary().getWordItem(word);
        }
        if (iworditem == null) {
            iworditem = getHashDictionary().getWordItem(word);
        }
        if (iworditem != null) {
            i1 = (int) iworditem.getOccuredSum();
        }
        return i1;
    }

    public boolean isUseDomainDictionary() {
        return useDomainDictionary;
    }

    public void setUseDomainDictionary(boolean flag) {
        useDomainDictionary = flag;
    }

    public int getMaxWordLength() {
        return maxWordLength;
    }

    public void setMaxWordLength(int len) {
        maxWordLength = len;
    }

    public boolean isUseContextFreqSegment() {
        return useContextFreqSegment;
    }

    public void setUseContextFreqSegment(boolean useContextFreqSegment) {
        this.useContextFreqSegment = useContextFreqSegment;
    }
    private int maxWordLength;
    private boolean loadDomainDictionary = Configure.getInstance().isLoadDomainDictionary();
    private boolean loadUserDictionary = Configure.getInstance().isLoadUserDictionary();
    private Dijkstra dijk;
    private IGraph graph;
    private IPOSRecognizer posTagging;
    private boolean useCache;
    private UnknownWordCache objectCache;
    private boolean lastSection;
    private String lastSectionStr;
    private Path path;
    private double logCorpus;
    private boolean useDomainDictionary;
    private final static int CorpusSize = 8000000;
    private boolean useContextFreqSegment = false;
}
