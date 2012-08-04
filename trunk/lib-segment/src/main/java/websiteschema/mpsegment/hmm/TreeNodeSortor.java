/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.hmm;

import java.util.Comparator;

/**
 *
 * @author ray
 */
public interface TreeNodeSortor<T> {

    public void setComparator(Comparator<Trie<T>> comparator);

    public Trie<T>[] sort(Trie<T>[] values);
}
