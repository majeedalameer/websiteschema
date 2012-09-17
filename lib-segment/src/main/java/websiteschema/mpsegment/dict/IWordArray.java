/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.dict;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;


/**
 *
 * @author twer
 */
public interface IWordArray {

    public IWord find(String word);
    
    public IWord[] getWordItems();

    public void add(IWord word);
}


class BinaryWordArray implements IWordArray {

    IWord[] wordItems;

    BinaryWordArray(IWord[] wordItems) {
        this.wordItems = wordItems;
    }

    @Override
    public IWord find(String word) {
        int index = lookupWordItem(word);
        if (index >= 0) {
            return wordItems[index];
        }
        return null;
    }

    @Override
    public void add(IWord word) {
        IWord[] temp = new IWord[wordItems.length + 1];
        System.arraycopy(wordItems, 0, temp, 0, wordItems.length);
        temp[wordItems.length] = word;
        Arrays.sort(temp, new Comparator<IWord>() {
            @Override
            public int compare(IWord o1, IWord o2) {
                return o1.getWordName().compareTo(o2.getWordName());
            }
        });
        wordItems = temp;
    }

    private int lookupWordItem(String word) {
        int left = 0;
        for (int right = wordItems.length - 1; left <= right;) {
            int mid = (left + right) / 2;
            int comp = wordItems[mid].getWordName().compareTo(word);
            if (comp == 0) {
                return mid;
            }
            if (comp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }

    @Override
    public IWord[] getWordItems() {
        return wordItems;
    }
}

class HashWordArray implements IWordArray {

    HashMap<String, Integer> wordIndex;
    IWord[] wordItems;

    HashWordArray(IWord[] words) {
        this.wordIndex = new HashMap<String, Integer>(words.length);
        int i = 0;
        for(IWord word : words) {
            wordIndex.put(word.getWordName(), i++);
        }
        
        wordItems = words;
    }

    @Override
    public IWord find(String word) {
        Integer index = wordIndex.get(word);
        if (null != index && index >= 0) {
            return wordItems[index];
        }
        return null;
    }

    @Override
    public IWord[] getWordItems() {
        return wordItems;
    }

    @Override
    public void add(IWord word) {
        throw new UnsupportedOperationException();
    }
}

