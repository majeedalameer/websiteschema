// Source File Name:   ObjectArraySet.java
package websiteschema.mpsegment.graph;

public class ObjectArraySet {

    public ObjectArraySet() {
        top = 0;
        size = 1;
        objects = new Object[size];
    }

    public ObjectArraySet(int i) {
        top = 0;

        size = i;
        objects = new Object[size];
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

    public void add(Object obj) {
        if (top >= size) {
            size = size + 1024;
            Object objectsNew[] = new Object[size];
            for (int i = 0; i < objects.length; i++) {
                objectsNew[i] = objects[i];
            }
            objects = null;
            objects = objectsNew;
        }
        objects[top] = obj;
        top++;
    }

    public Object get(int i) {
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
    private Object objects[];
    public int top;
    public int size;
}
