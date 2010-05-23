/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.util;

import java.util.Map;

/**
 *
 * @author ray
 */
public class Pair<K,V> implements Map.Entry<K,V> {

    K k = null;
    V v = null;

    public Pair(K key, V value){
        k = key;
        v = value;
    }

    public K getKey() {
        return k;
    }

    public V getValue() {
        return v;
    }

    public V setValue(V value) {
        v = value;
        return v;
    }

}
