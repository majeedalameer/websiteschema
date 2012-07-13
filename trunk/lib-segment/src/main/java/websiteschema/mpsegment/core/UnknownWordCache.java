package websiteschema.mpsegment.core;

import java.util.HashMap;
import java.util.Map;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.dict.UnknownWord;

public class UnknownWordCache {

    public UnknownWordCache() {
        this(2);
    }

    public UnknownWordCache(int size) {
        top = 1;
        isCached = true;
        this.size = size;
        wordItems = new IWord[size];
        wordNameToIndexMap = new HashMap<String, Integer>(size);
    }

    public void setCache(boolean flag) {
        isCached = flag;
    }

    public void clear() {
        if (top > size) {
            top = size;
        }
        for (int i = 0; i < top; i++) {
            wordItems[i] = null;
        }
        top = 1;
        wordNameToIndexMap.clear();
    }

    public void add(IWord iworditem) {
        IWord elder = wordItems[top];
        if (elder != null) {
            wordNameToIndexMap.remove(elder.getWordName());
        }
        wordItems[top] = iworditem;
        wordNameToIndexMap.put(iworditem.getWordName(), top);
        top = (top + 1) % size;
    }

    public IWord getNewWordItem(String s) {
        if (isCached) {
            int i = getIndex(s);
            if (i > 0) {
                return wordItems[i];
            }
            if (s != null) {
                IWord worditem2_1 = new UnknownWord(s);
                add(worditem2_1);
                return worditem2_1;
            } else {
                return null;
            }
        }
        if (s != null) {
            IWord worditem2 = new UnknownWord(s);
            return worditem2;
        } else {
            return null;
        }
    }

    public IWord get(int i) {
        return wordItems[i];
    }

    public int getIndex(String s) {
        int i = -1;
        if (s != null && contains(s)) {
            i = wordNameToIndexMap.get(s);
        }
        return i;
    }

    public boolean contains(String s) {
        return wordNameToIndexMap.containsKey(s);
    }
    private IWord wordItems[];
    private Map<String, Integer> wordNameToIndexMap;
    public int top;
    public int size;
    private boolean isCached;
}
