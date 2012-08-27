// Source File Name:   ObjectArraySet.java
package websiteschema.mpsegment.graph;

import websiteschema.mpsegment.dict.IWord;

public class ObjectArraySet {

    public ObjectArraySet() {
        top = 0;
        size = 1;
        objects = new IWord[size];
    }

    public ObjectArraySet(int i) {
        top = 0;

        size = i;
        objects = new IWord[size];
    }

    public void clear() {
        if (top > size) {
            top = size;
        }
        for (int i = 0; i < top; i++) {
            objects[i] = null;
        }

        top = 0;
    }

    public void add(IWord obj) {
        if (top >= size) {
            size = size + 1024;
            IWord objectsNew[] = new IWord[size];
            System.arraycopy(objects, 0, objectsNew, 0, objects.length);
            objects = null;
            objects = objectsNew;
        }
        objects[top] = obj;
        top++;
    }

    public IWord get(int i) {
        return objects[i];
    }

    public boolean contains(Object obj) {
        for (int i = 0; i < top;) {
            if (objects[i] != obj) {
                i++;
            } else {
                return true;
            }
        }

        return false;
    }
    private IWord objects[];
    public int top;
    public int size;
}
