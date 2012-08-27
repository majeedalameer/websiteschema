/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.core;

import java.util.HashMap;
import java.util.Map;
import websiteschema.mpsegment.conf.Configure;
import websiteschema.mpsegment.dict.DictionaryFactory;
import websiteschema.mpsegment.dict.HashDictionary;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.dict.domain.DomainDictFactory;
import websiteschema.mpsegment.dict.domain.DomainDictionary;
import websiteschema.mpsegment.graph.IGraph;

/**
 *
 * @author ray
 */
public class GraphBuilder {

    private final IGraph graph;
    private final double logCorpus;
    private final HashDictionary hashDictionary = DictionaryFactory.getInstance().getCoreDictionary();
    private final boolean useDomainDictionary;
    private final UnknownWordCache objectCache;
    private Map<String, Integer> contextFreqMap = new HashMap<String, Integer>();
    private boolean useContextFreqSegment = false;

    public GraphBuilder(IGraph graph, double logCorpus, boolean useDomainDictionary, UnknownWordCache objectCache) {
        this.graph = graph;
        this.logCorpus = logCorpus;
        this.useDomainDictionary = useDomainDictionary;
        this.objectCache = objectCache;
    }

    private boolean isWordStartWithAlphabeticalOrDigital(char ch) {
        boolean flag = false;
        if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch >= '0' && ch <= '9' || ch >= '\uFF21' && ch <= '\uFF41' || ch >= '\uFF3A' && ch <= '\uFF5A' || ch >= '\uFF10' && ch <= '\uFF19') {
            flag = true;
        }
        return flag;
    }

    private String doUpperCaseAndHalfShape(String word) {
        if (isUpperCaseOrHalfShapeAll) {
            char chArray[] = word.toCharArray();
            for (int i1 = 0; i1 < chArray.length; i1++) {
                if (isWordStartWithAlphabeticalOrDigital(chArray[i1])) {
                    if (isHalfShapeAll && (chArray[i1] >= '\uFF21' && chArray[i1] <= '\uFF41' || chArray[i1] >= '\uFF3A' && chArray[i1] <= '\uFF5A' || chArray[i1] >= '\uFF10' && chArray[i1] <= '\uFF19')) {
                        chArray[i1] = (char) (chArray[i1] - 65248);
                    }
                    if (isUpperCaseAll && chArray[i1] >= 'a' && chArray[i1] <= 'z') {
                        chArray[i1] = (char) (chArray[i1] - 32);
                    }
                }
            }

            return new String(chArray);
        }
        return word;
    }

    private int scanEnglishWordAndShorten(int begin) {
        final int slen = sentence.length();
        int index = begin;
        if (isWordStartWithAlphabeticalOrDigital(sentence.charAt(index))) {
            while (index < slen && isWordStartWithAlphabeticalOrDigital(sentence.charAt(index))) {
                index++;
            }
            index--;
        }
        return index;
    }

    private int getLastMinWordLength(int begin) {
        int minWordLen = begin;
        int index = scanEnglishWordAndShorten(begin);
        minWordLen = (index - begin) + 1;
        return minWordLen;
    }

    private String getCandidateSentence(int begin) {
        String candidateWord = "";
        final int slen = sentence.length();
        final int rest = slen - begin;
        if (maxWordLength <= rest) {
            int end = (begin + maxWordLength + lastMinWordLen) - 1 < slen
                    ? (begin + maxWordLength + lastMinWordLen) - 1 : slen;
            candidateWord = sentence.substring(begin, end);
        } else {
            candidateWord = sentence.substring(begin);
        }
        return candidateWord;
    }

    private int getWeight(IWord iworditem) {
        if (useContextFreqSegment) {
            String word = iworditem.getWordName();
            int weight = 1;
            if (word.length() > 1) {
                int contextFreq = contextFreqMap.containsKey(word) ? contextFreqMap.get(word) : 1;
                int freq = getFreqWeight(iworditem);
                weight = (int) (freq + getContextFreqWeight(freq, contextFreq));
            } else {
                weight = getFreqWeight(iworditem);
            }
            if (weight <= 0) {
                weight = 1;
            }
            return weight;
        }
        int weight = getFreqWeight(iworditem);
        return weight;
    }

    private int getFreqWeight(IWord iworditem) {
        int log2Freq = iworditem.getLog2Freq();
        if (logCorpus > log2Freq) {
            int freqWeight = (int) (logCorpus - iworditem.getLog2Freq());
            return freqWeight;
        } else {
            return 1;
        }
    }

    private int getContextFreqWeight(int freq, int contextFreq) {
        int weight = -(int) ((1 - Math.exp(-0.1 * (contextFreq - 1))) * freq);
        return weight;
    }

    private void addEdgeObject(int head, int tail, int weight, IWord word) {
//        System.out.println(word.getWordName() + " weight " + weight);
        graph.addEdge(head, tail, weight, word);
    }

    public void scanContextFreq(final int startPos) {
        lastMinWordLen = 0;
        try {
            final int slen = sentence.length();
            for (int begin = startPos; begin < slen; begin += lastMinWordLen) {
                lastMinWordLen = getLastMinWordLength(begin);

                //find all possible slices except single word
                final String candidateWord = getCandidateSentence(begin);
                final IWord iworditem = getItem(candidateWord, lastMinWordLen);
                if (iworditem != null && iworditem.getWordLength() > 1) {
                    //查找结果不为空且不是单字词
                    increaseContextFreq(iworditem.getWordName());
                    if (the2ndMatchWord != null && the2ndMatchWord.getWordLength() > 1) {
                        increaseContextFreq(the2ndMatchWord.getWordName());
                    }
                    if (the3rdMatchWord != null && the3rdMatchWord.getWordLength() > 1) {
                        increaseContextFreq(the3rdMatchWord.getWordName());
                    }
                }
            }
        } finally {
            lastMinWordLen = 0;
            the2ndMatchWord = null;
            the3rdMatchWord = null;
        }
    }

    private void increaseContextFreq(String word) {
        if (contextFreqMap.containsKey(word)) {
            int freq = contextFreqMap.get(word);
            contextFreqMap.put(word, freq + 1);
        } else {
            contextFreqMap.put(word, 1);
        }
    }

    public void setSentence(String sen) {
        sentence = doUpperCaseAndHalfShape(sen);
    }

    public void buildGraph(final String sen, final int startPos) {
        setSentence(sen);
        if (useContextFreqSegment) {
            scanContextFreq(startPos);
        }
        final int slen = sentence.length();
        for (int i = 0; i < slen; i++) {
            graph.addVertex();
        }

        for (int begin = startPos; begin < slen; begin += lastMinWordLen) {
            lastMinWordLen = getLastMinWordLength(begin);

            //find single char word or multi-chars alpha-numeric word
            String atomWord = sentence.substring(begin, begin + lastMinWordLen);
            IWord singleCharWord = getItem(atomWord, lastMinWordLen); // single char word 单字词
            if (singleCharWord == null) {
                singleCharWord = initAsUnknownWord(atomWord);//Unknown Word
            }

            //find all possible slices except single word
            final String candidateWord = getCandidateSentence(begin);
            final IWord iworditem = getItem(candidateWord, lastMinWordLen);
            //将查找到的词添加到图中。
            //为了减少图的分支，同时因为单字词在中文中往往没有太多意义。
            //如果存在多个多字词，则不向图中添加单字词
            if (iworditem != null && iworditem.getWordLength() > 1) {
                //查找结果不为空且不是单字词
                addEdgeObject(begin + 1, begin + iworditem.getWordLength() + 1, getWeight(iworditem), iworditem);
                {
                    if (the2ndMatchWord != null) {
                        addEdgeObject(begin + 1, begin + the2ndMatchWord.getWordLength() + 1, getWeight(the2ndMatchWord), the2ndMatchWord);
                    } else {
                        //仅有一个多字词，需要向图中添加单字词
                        addEdgeObject(begin + 1, begin + lastMinWordLen + 1, getWeight(singleCharWord), singleCharWord);
                    }
                    if (the3rdMatchWord != null) {
                        addEdgeObject(begin + 1, begin + the3rdMatchWord.getWordLength() + 1, getWeight(the3rdMatchWord), the3rdMatchWord);
                    }
                }
            } else {
                addEdgeObject(begin + 1, begin + lastMinWordLen + 1, getWeight(singleCharWord), singleCharWord);
            }
        }
    }

    private IWord get1stMatchWord(IWord[] iworditems) {
        return null != iworditems && iworditems.length > 0 ? iworditems[0] : null;
    }

    private IWord get2ndMatchWord(IWord[] iworditems) {
        return null != iworditems && iworditems.length > 1 ? iworditems[1] : null;
    }

    private IWord get3rdMatchWord(IWord[] iworditems) {
        return null != iworditems && iworditems.length > 2 ? iworditems[2] : null;
    }

    private IWord getMultiWordsInHashDictionary(String candidateWord) {
        IWord iworditem = null;
        IWord[] iworditems = hashDictionary.getWordItems(candidateWord);
        if (null != iworditems) {
            iworditem = get1stMatchWord(iworditems);
            if (iworditem != null) {
                the2ndMatchWord = get2ndMatchWord(iworditems);
                the3rdMatchWord = get3rdMatchWord(iworditems);
            }
        }
        return iworditem;
    }

    private IWord getItem(String candidateWord, int minWordLength) {
        IWord iworditem = null;
        if (useDomainDictionary && (loadDomainDictionary || loadUserDictionary) && candidateWord.length() > 1) {
            iworditem = getDomainDictionary().getWordItem(candidateWord, minWordLength);
        }
        if (iworditem == null) {
            //如果在领域词典中没有找到对应的词
            //则在核心词典中继续查找
            iworditem = getMultiWordsInHashDictionary(candidateWord);
        } else {
            //如果在领域词典中找到对应的词
            //继续在核心词典中查找，并排除在返回结果中和领域词典里相同的词
            IWord[] iworditems = hashDictionary.getWordItems(candidateWord);
            the2ndMatchWord = get1stMatchWord(iworditems);
            if (the2ndMatchWord != null) {
                if (iworditem.getWordName().length() == the2ndMatchWord.getWordName().length()) {
                    //如果用户辞典或领域辞典的长度和普通辞典相同
                    the2ndMatchWord = get2ndMatchWord(iworditems);
                    the3rdMatchWord = get3rdMatchWord(iworditems);
                    if (the2ndMatchWord != null) {
                        if (iworditem.getWordName().length() == the2ndMatchWord.getWordName().length()) {
                            the2ndMatchWord = null;
                        }
                        if (the3rdMatchWord != null && iworditem.getWordName().length() == the3rdMatchWord.getWordName().length()) {
                            the3rdMatchWord = null;
                        }
                        if (the2ndMatchWord == null) {
                            the2ndMatchWord = the3rdMatchWord;
                            the3rdMatchWord = null;
                        }
                    }
                } else {
                    //如果用户辞典或领域辞典的长度和普通辞典不相同
                    the3rdMatchWord = get2ndMatchWord(iworditems);
                    if (the3rdMatchWord != null) {
                        if (iworditem.getWordName().length() == the3rdMatchWord.getWordName().length()) {
                            the3rdMatchWord = get3rdMatchWord(iworditems);
                        }
                        if (the3rdMatchWord != null && iworditem.getWordName().length() == the3rdMatchWord.getWordName().length()) {
                            the3rdMatchWord = get3rdMatchWord(iworditems);
                        }
                    }
                }
            }
        }
        return iworditem;
    }

    private DomainDictionary getDomainDictionary() {
        return DomainDictFactory.getInstance().getDomainDictionary();
    }

    private IWord initAsUnknownWord(String unknownWord) {
        return objectCache.getNewWordItem(unknownWord);
    }

    public Map<String, Integer> getContextFreqMap() {
        return contextFreqMap;
    }

    public boolean isUseContextFreqSegment() {
        return useContextFreqSegment;
    }

    public void setUseContextFreqSegment(boolean useContextFreqSegment) {
        this.useContextFreqSegment = useContextFreqSegment;
    }
    private final static boolean isUpperCaseAll = Configure.getInstance().isUpperCaseAll();
    private final static boolean isHalfShapeAll = Configure.getInstance().isHalfShapeAll();
    private final static boolean isUpperCaseOrHalfShapeAll = isUpperCaseAll || isHalfShapeAll;
    private final static int maxWordLength = Configure.getInstance().isSegmentMin() ? 4 : Configure.getInstance().getMaxWordLength();
    private final static boolean loadDomainDictionary = Configure.getInstance().isLoadDomainDictionary();
    private final static boolean loadUserDictionary = Configure.getInstance().isLoadUserDictionary();
    private IWord the2ndMatchWord;
    private IWord the3rdMatchWord;
    int lastMinWordLen = 0;
    String sentence = "";
}
