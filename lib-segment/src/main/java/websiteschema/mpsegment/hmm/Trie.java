/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.hmm;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ray
 */
public class Trie<T> implements java.io.Serializable {

    T key = null;
    int count = 0;
    double prob = 0.0;
    Trie<T> descendant[] = null;

    public T getKey() {
        return key;
    }

    public void buildIndex(int c, TreeNodeSortor<T> sortor) {
        prob = (double) count / (double) (c + 1.0);
        if (null != descendant) {
            for (Trie<T> node : descendant) {
                node.buildIndex(count, sortor);
            }
            sortor.sort(descendant);
        }
    }

    public Trie<T> insert(T[] ngram, TreeNodeSortor<T> sortor, Comparator<T> comparator) {
        count++;
        if (ngram.length > 0) {
            T k = ngram[0];
            Trie<T> n = null != descendant ? binarySearch(descendant, descendant.length, k, comparator) : null;
            if (null == n) {
                n = new Trie<T>();
                n.key = k;
                add(n);
                descendant = sortor.sort(descendant);
            }

            T rec[] = (T[]) new Object[ngram.length - 1];
            for (int i = 1; i < ngram.length; i++) {
                rec[i - 1] = ngram[i];
            }
            return n.insert(rec, sortor, comparator);
        } else {
            return this;
        }
    }

    private void add(Trie<T> e) {
        int i = 0;
        if (null == descendant) {
            descendant = new Trie[1];
        } else {
            Trie[] tmp = new Trie[descendant.length + 1];
            System.arraycopy(descendant, 0, tmp, 0, descendant.length);
            i = descendant.length;
            descendant = tmp;
        }
        descendant[i] = e;
    }

    public Trie<T> searchNode(T[] ngram, Comparator<T> comparator) {
        T k = ngram[0];
        Trie<T> n = searchNode(k, comparator);
        if (null != n && ngram.length > 1) {
            T rec[] = (T[]) new Object[ngram.length - 1];
            for (int i = 1; i < ngram.length; i++) {
                rec[i - 1] = ngram[i];
            }
            return n.searchNode(rec, comparator);
        }
        return n;
    }

    public Trie<T> searchNode(T k, Comparator<T> comparator) {
        return null != descendant ? binarySearch(descendant, descendant.length, k, comparator) : null;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getProb() {
        return prob;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

    public Trie<T> binarySearch(Trie<T>[] list, int listLength, T searchItem, Comparator<T> comparator) {
        if (null == list) {
            return null;
        }
        int first = 0;
        int last = listLength - 1;
        int mid = -1;

        boolean found = false;
        while (first <= last && !found) {
            mid = (first + last) / 2;

            int i = comparator.compare(list[mid].key, searchItem);

            if (i == 0) {
                found = true;
            } else {
                if (i > 0) {
                    last = mid - 1;
                } else {
                    first = mid + 1;
                }
            }
        }

        if (found) {
            return list[mid];
        } else {
            return null;
        }
    }

    public void printTreeNode(String indent) {
        System.out.println(indent + key + " - " + count + " - " + prob);
        if (null != descendant) {
            for (Trie node : descendant) {
                node.printTreeNode(indent + "  ");
            }
        }
    }

    public static Character[] stringToCharArray(String s) {
        Character array[] = new Character[s.length()];
        for (int i = 0; i < s.length(); i++) {
            array[i] = s.charAt(i);
        }
        return array;
    }

    public long getNumberOfNodeWhichCountLt(int lt) {
        long c = count < lt ? 1 : 0;

        if (null != descendant) {
            for (Trie<T> node : descendant) {
                c += node.getNumberOfNodeWhichCountLt(lt);
            }
        }

        return c;
    }

    public void cutCountLowerThan(int lt) {
        if (lt == 1) {
            return;
        }
        if (null != descendant) {
            List<Trie<T>> l = new LinkedList<Trie<T>>();
            for (int i = 0; i < descendant.length; i++) {
                Trie<T> node = descendant[i];
                if (node.getCount() >= lt) {
                    l.add(node);
                    node.cutCountLowerThan(lt);
                }
            }

            descendant = l.toArray(new Trie[0]);
        }
    }
}
