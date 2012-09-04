package websiteschema.mpsegment.dict;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import websiteschema.mpsegment.conf.Configure;
import websiteschema.mpsegment.util.BufReader;

public class HeadIndexer {

    public HeadIndexer(BufReader bufreader, POSArray posArray)
            throws IOException {
        wordItems = null;
        load(bufreader, posArray);
    }

    public int getWordOccuredSum() {
        return wordOccuredSum;
    }

    public int getWordCount() {
        return wordItems.length;
    }

    public final int getMaxWordLength() {
        return maxWordLength;
    }

    public String getHeadStr() {
        return headStr;
    }

    @Override
    public String toString() {
        StringBuilder stringbuffer = new StringBuilder();
        stringbuffer.append((new StringBuilder("首字:")).append(getHeadStr()).append("(不同词数量:").append(getWordCount()).toString());
        stringbuffer.append((new StringBuilder(",最长词长度:")).append(getMaxWordLength()).toString());
        stringbuffer.append((new StringBuilder(",所有词的全部出现次数:")).append(getWordOccuredSum()).append(")\n").toString());
        for (int i = 0; i < wordItems.length; i++) {
            IWord iworditem = wordItems[i];
            stringbuffer.append(iworditem.toString());
        }

        return stringbuffer.toString();
    }

    public String toDBFString() {
        StringBuilder stringbuffer = new StringBuilder();
        for (int i = 0; i < wordItems.length; i++) {
            IWord iworditem = wordItems[i];
            stringbuffer.append(iworditem.toDBFString());
            stringbuffer.append("\n");
        }

        return stringbuffer.toString();
    }

    public final void load(BufReader bufreader, POSArray posArray)
            throws IOException {
        int headWordByte = bufreader.readIntByte();
        byte headWordBytes[] = new byte[headWordByte];
        bufreader.read(headWordBytes);
        headStr = new String(headWordBytes, Configure.getInstance().getFileEncoding());
        int numWordItem = bufreader.readInt();
        maxWordLength = 0;
        wordOccuredSum = 0;
        wordItems = new WordImpl[numWordItem];
        indexMap = new HashMap<String, Integer>(numWordItem);
        for (int i = 0; i < numWordItem; i++) {
            WordImpl word = new WordImpl(bufreader);
            wordOccuredSum += word.getOccuredSum();
            if (maxWordLength < word.getWordLength()) {
                maxWordLength = word.getWordLength();
            }
            wordItems[i] = word;
            indexMap.put(word.getWordName(), i);
            if(i == 0) {
                headWord = word;
            }
            posArray.add(word.getPOSArray());
        }

    }

    public final String toText() {
        String ls = System.getProperty("line.separator");
        String space = " ";
        StringBuilder sb = new StringBuilder();
        sb.append("[headIndexer]").append(ls);
        sb.append(headStr).append(space);
        sb.append(wordItems.length).append(ls);
        for (int i = 0; i < wordItems.length; i++) {
            sb.append(((WordImpl) wordItems[i]).toText());
        }
        sb.append(ls);
        return sb.toString();
    }

    public final void save(RandomAccessFile randomaccessfile)
            throws IOException {
        byte abyte0[] = headStr.getBytes(Configure.getInstance().getFileEncoding());
        randomaccessfile.write((byte) abyte0.length);
        randomaccessfile.write(abyte0);
        randomaccessfile.writeInt(getWordCount());
        for (int i = 0; i < wordItems.length; i++) {
            wordItems[i].save(randomaccessfile);
        }

    }

    public IWord searchWord(String s) {
        int i = lookupWordItem(s);
        if (i >= 0) {
            return wordItems[i];
        } else {
            return null;
        }
    }

    public IWord[] findMultiWord(String wordStr) {
        int firstWordIndex = -1;
        int foundWordCount = 0;
        int secondWordIndex = -1;
        int thirdWordIndex = -1;
        if (wordStr.length() == 1) {
            if (wordStr.equals(wordItems[0].getWordName())) {
                IWord words[] = new IWord[1];
                words[0] = headWord;
                return words;
            } else {
                return null;
            }
        }
        int maxWordLen = getMaxWordLength();
        if (wordStr.length() < maxWordLen) {
            maxWordLen = wordStr.length();
        }
        for (int i = 1; i < maxWordLen; i++) {
            String candidateWord = wordStr.substring(0, i + 1);
            int wordIndex = lookupWordItem(candidateWord);
            if (wordIndex < 0) {
                continue;
            }
            if (++foundWordCount == 2) {
                secondWordIndex = firstWordIndex;
            } else if (foundWordCount == 3) {
                thirdWordIndex = firstWordIndex;
            }
            firstWordIndex = wordIndex;
            if (firstWordIndex + 1 >= wordItems.length || !wordItems[firstWordIndex + 1].getWordName().startsWith(candidateWord, 0)) {
                break;
            }
        }

        if (firstWordIndex >= 0) {
            IWord words[] = new IWord[foundWordCount];
            words[0] = wordItems[firstWordIndex];
            if (secondWordIndex > 0) {
                words[1] = wordItems[secondWordIndex];
            }
            if (thirdWordIndex > 0) {
                words[2] = wordItems[thirdWordIndex];
            }
            return words;
        } else {
            return null;
        }
    }
    
    public IWord findWord(String wordStr) {
        if (wordStr.length() == 1) {
            if (wordStr.equals(headStr)) {
                return headWord;
            } else {
                return null;
            }
        }
        int maxWordLen = getMaxWordLength();
        if (wordStr.length() < maxWordLen) {
            maxWordLen = wordStr.length();
        }
        for (int i = 1; i < maxWordLen; i++) {
            String candidateWord = wordStr.substring(0, i + 1);
            IWord word = searchWord(candidateWord);
            if (null == word) {
                continue;
            }
            return word;
        }
        return null;
    }

    private int lookupWordItem(String word) {
        return indexMap.containsKey(word) ? indexMap.get(word) : -1;
    }
    private String headStr;
    private int maxWordLength;
    private int wordOccuredSum;
    private IWord headWord;
    private IWord wordItems[];
    private HashMap<String, Integer> indexMap;
}
