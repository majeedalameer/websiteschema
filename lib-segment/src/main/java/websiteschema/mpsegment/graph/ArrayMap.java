package websiteschema.mpsegment.graph;

import java.util.Arrays;

public class ArrayMap {

    public ArrayMap() {
        top = 0;
        size = 1;
        indexes = new int[size];
        keys = new int[size];
        values = new int[size];
    }

    public ArrayMap(int i) {
        top = 0;
        size = 1;
        size = i + 1;
        indexes = new int[size];
        keys = new int[size];
        values = new int[size];
    }

    public void clear() {
        top = 0;
        Arrays.fill(indexes, -1);
        Arrays.fill(keys, 0);
        Arrays.fill(values, 0);
    }

    public void put(int key, int value) {
        int k = 0;
        k = getIndex(key);
        if (k < 0) {
            indexes[key] = top;
            keys[top] = key;
            values[top] = value;
            top++;
        } else {
            values[k] = value;
        }
    }

    public boolean containsKey(int i) {
        return indexes[i] >= 0;
    }

    public int getIndex(int i) {
        return indexes[i];
    }

    public int get(int i) {
        if (indexes[i] >= 0) {
            return values[indexes[i]];
        } else {
            return -1;
        }
    }
    private int indexes[];
    private int keys[];
    private int values[];
    private int top;
    private int size;
}
