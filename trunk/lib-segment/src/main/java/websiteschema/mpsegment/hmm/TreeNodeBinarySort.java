/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.hmm;

import java.util.*;

/**
 *
 * @author ray
 */
public class TreeNodeBinarySort<T> implements TreeNodeSortor<T> {

    Comparator<Trie<T>> comparator = null;

    public void setComparator(Comparator<Trie<T>> comparator) {
        this.comparator = comparator;
    }

    public Trie<T>[] sort(Trie<T>[] values) {
        List<Trie<T>> tmp = new ArrayList<Trie<T>>();
        for(Trie<T> node : values){
            tmp.add(node);
        }
        Collections.sort(tmp, comparator);
        return tmp.toArray(values);
    }
}
