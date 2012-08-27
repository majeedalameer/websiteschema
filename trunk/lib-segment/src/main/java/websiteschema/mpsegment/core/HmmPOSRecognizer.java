package websiteschema.mpsegment.core;

import java.util.ArrayList;
import java.util.Collections;
import websiteschema.mpsegment.conf.Configure;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.dict.POSProbMatrix;
import websiteschema.mpsegment.graph.IGraph;
import websiteschema.mpsegment.graph.Path;

/**
 * Part of speech recognition using HMM.
 * @author ray
 */
public class HmmPOSRecognizer implements IPOSRecognizer {

    public HmmPOSRecognizer() {
        maxPathLength = 1024;
        maxPosPerWord = 7;
        pathLength = maxPathLength;
        posProbMatrix = new POSProbMatrix(Configure.getInstance().getPOSMatrix());
        posIndex = new int[maxPathLength][maxPosPerWord];
        posFreq = new int[maxPathLength][maxPosPerWord];
        wordProbsAsPos = new double[maxPathLength][maxPosPerWord];
        previousMaxPosIs = new int[maxPathLength][maxPosPerWord];
        intArrayList = new ArrayList<Integer>();
    }

    private void setSegPathLength(int i1) {
        pathLength = i1 + 2;
        if (pathLength > maxPathLength) {
            pathLength = maxPathLength;
        }
    }

    private int getSegPathLength() {
        return pathLength;
    }

    private int getPathLength() {
        return path.getLength();
    }

    public void clear() {
        for (int wordI = 0; wordI < getSegPathLength(); wordI++) {
            for (int pos = 0; pos < maxPosPerWord; pos++) {
                wordProbsAsPos[wordI][pos] = 0.0D;
                previousMaxPosIs[wordI][pos] = 0;
                posIndex[wordI][pos] = 0;
                posFreq[wordI][pos] = 0;
            }
        }
    }

    private void initializePOSTable() {
        if (getPathLength() < 1) {
            return;
        }
        for (int i = 0; i < getPathLength(); i++) {
            IWord iworditem = getWordInIndex(i);
            int[][] posTable = new int[maxPosPerWord][2];
            iworditem.getWordPOSTable(posTable);
            for (int posI = 0; posI < maxPosPerWord; posI++) {
                posIndex[i][posI] = posTable[posI][0];
                posFreq[i][posI] = posTable[posI][1];
            }
        }
    }

    private IWord getWordInIndex(int index) {
        return graph.getEdgeObject(path.iget(index), path.iget(index + 1));
    }

    private double getCoProb(int posIndex1, int posIndex2) {
        return posProbMatrix.getCoProb(posIndex1, posIndex2);
    }

    private double getWordProb(int posIndex1, int freq) {
        if (posProbMatrix.getTagFreqs(posIndex1) > 0) {
            return (double) (freq + 1) / (double) posProbMatrix.getTagFreqs(posIndex1);
        } else {
            return 1.0000000000000001E-005D;
        }
    }

    /**
     * Vertbi
     */
    private void disambiguate() {
        double maxCoProb = 999999D;

        setSegPathLength(getPathLength());
        for (int wordI = 1; wordI < getPathLength(); wordI++) {

            for (int posI1 = 0; posI1 < maxPosPerWord && posIndex[wordI][posI1] > 0; posI1++) {

                int maxCoProbIndex = maxPosPerWord;

                for (int posI2 = 0; posI2 < maxPosPerWord && posIndex[wordI - 1][posI2] > 0; posI2++) {

                    double coProb = getCoProb(posIndex[wordI - 1][posI2], posIndex[wordI][posI1]);
                    coProb = -Math.log(coProb);
                    coProb += wordProbsAsPos[wordI - 1][posI2];
                    if (maxCoProbIndex >= maxPosPerWord || coProb < maxCoProb) {
                        maxCoProbIndex = posI2;
                        maxCoProb = coProb;
                    }
                }
                previousMaxPosIs[wordI][posI1] = maxCoProbIndex;
                double wordProb = getWordProb(posIndex[wordI][posI1], posFreq[wordI][posI1]);  // the probability of a word acting as posI1
                wordProbsAsPos[wordI][posI1] = maxCoProb + -Math.log(wordProb);
            }

        }

    }

    private void setPosTagArrayList() {
        int i1 = 0;
        intArrayList.clear();
        for (int wordI = path.getLength() - 1; wordI >= 0; wordI--) {
            intArrayList.add(posIndex[wordI][i1]);
            i1 = previousMaxPosIs[wordI][i1];
        }

        Collections.reverse(intArrayList);
    }

    private int[] getPOSArray() {
        int ai[] = new int[intArrayList.size()];
        for (int i1 = 0; i1 < intArrayList.size(); i1++) {
            ai[i1] = intArrayList.get(i1);
        }

        return ai;
    }
    
    @Override
    public int[] findPOS(Path path, IGraph graph) {
        this.path = path;
        this.graph = graph;
        initializePOSTable();
        disambiguate();
        setPosTagArrayList();
        clear();
        return getPOSArray();
    }

    private int maxPathLength;
    private int maxPosPerWord;
    private IGraph graph;
    private Path path;
    private POSProbMatrix posProbMatrix;
    private int posIndex[][];
    private int posFreq[][];
    private double wordProbsAsPos[][];
    private int previousMaxPosIs[][];
    private ArrayList<Integer> intArrayList;
    private int pathLength;
}
