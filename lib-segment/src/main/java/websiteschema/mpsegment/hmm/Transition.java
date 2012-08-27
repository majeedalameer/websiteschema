/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.hmm;

import java.io.IOException;
import websiteschema.mpsegment.util.ISerialize;
import websiteschema.mpsegment.util.SerializeHandler;

/**
 *
 * @author ray
 */
public class Transition implements ISerialize {

    Trie root = null;

    public Trie getRoot() {
        return root;
    }
    NodeRepository stateBank = null;
    TreeNodeSortor sortor = null;

    public void setStateBank(NodeRepository stateBank) {
        this.stateBank = stateBank;
    }

    public void setSortor(TreeNodeSortor sortor) {
        this.sortor = sortor;
    }

    public Transition() {
        this.root = new Trie();
    }

    public Transition(Trie root, NodeRepository bank) {
        this.root = root;
        this.stateBank = bank;
    }

    public void setProb(int s1, int s2, double prob) {
        int[] ngram = new int[]{s1, s2};
        Trie node = root.insert(ngram, sortor);
        node.setProb(prob);
    }

    public double getProb(int[] s) {
        String[] ngram = new String[s.length];
        for (int i = 0; i < s.length; i++) {
            ngram[i] = stateBank.get(s[i]).getName();
        }
        return getProb(ngram, ngram.length);
    }

    public double getProb(int[] c, int s) {
        String[] ngram = new String[c.length + 1];
        for (int i = 0; i < c.length; i++) {
            ngram[i] = stateBank.get(c[i]).getName();
        }
        ngram[c.length] = stateBank.get(s).getName();

        return getProb(ngram, ngram.length);
    }

    public double getProb(int s1, int s2) {
        String[] ngram = new String[2];
        ngram[0] = stateBank.get(s1).getName();
        ngram[1] = stateBank.get(s2).getName();
        return getProb(ngram, 2);
    }

    public double getProb(String[] ngram, int n) {
        double ret = 0.00000001D;

        //bigram
        if (2 == n) {
            return getProb(ngram);
        }

        for (int i = n; i > 0; i--) {
            String[] igram = new String[i];
            for (int j = 1; j <= i; j++) {
                igram[i - j] = ngram[n - j];
            }
            ret += Flag.getInstance().labda(i - 1) * getProb(igram);
        }

        return ret;
    }

    private int[] getNodeArray(String[] ngram) {
        int[] array = new int[ngram.length];
        for(int i = 0; i < ngram.length; i++) {
            array[i] = stateBank.get(ngram[i]).getIndex();
        }
        return array;
    }
    
    private double getProb(String[] ngram) {
        double ret;

        Trie node = root.searchNode(getNodeArray(ngram));
        if (null != node) {
            ret = node.getProb();
        } else {
            ret = 1.0 / root.getCount();
        }

        return ret;
    }

    @Override
    public void save(SerializeHandler writeHandler) throws IOException {
        root.save(writeHandler);
        stateBank.save(writeHandler);
    }

    @Override
    public void load(SerializeHandler readHandler) throws IOException {
        root.load(readHandler);
        stateBank.load(readHandler);
    }
}
