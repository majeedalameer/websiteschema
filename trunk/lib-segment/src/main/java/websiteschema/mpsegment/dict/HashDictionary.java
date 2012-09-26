package websiteschema.mpsegment.dict;

import websiteschema.mpsegment.util.BufReader;
import websiteschema.mpsegment.util.ByteArrayReader;
import websiteschema.mpsegment.util.FileUtil;

import java.io.*;
import java.util.HashMap;

public class HashDictionary implements IDictionary {

    public HashDictionary(String dictResource) {
        maxWordLength = 0;
        wordOccuredSum = 0;
        wordCount = 0;
        posArray = null;
        if (!loaded) {
            loadDict(dictResource);
            loaded = true;
        }
    }

    public int getWordOccuredSum() {
        return wordOccuredSum;
    }

    public int getCapacity() {
        return headIndexers.length;
    }

    public int getMaxWordLength() {
        return maxWordLength;
    }

    public int getWordCount() {
        return wordCount;
    }

    public HeadIndexer[] getHeadIndexers() {
        return headIndexers;
    }

    public synchronized void clear() {
        headIndexersHashMap.clear();
    }

    public String dictoString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((new StringBuilder("不同首字数量:")).append(getCapacity()).toString());
        stringBuilder.append((new StringBuilder(",最长词长度:")).append(getMaxWordLength()).toString());
        stringBuilder.append((new StringBuilder(",所有词的全部出现次数:")).append(getWordOccuredSum()).append("\n").toString());
        stringBuilder.append((new StringBuilder("词性列表：(共出现次数")).append(posArray.getOccuredSum()).append(")\n").toString());
        stringBuilder.append(posArray.toString());

        System.out.println(stringBuilder.toString());
        stringBuilder.setLength(0);
        for (int k = 0; k < headIndexers.length; k++) {
            stringBuilder.append(headIndexers[k].toDBFString());
            System.out.print(stringBuilder.toString());
            stringBuilder.setLength(0);
        }

        return stringBuilder.toString();
    }

    private synchronized void loadDict(String s) {
        try {
            BufReader bufReader = new ByteArrayReader(FileUtil.getResourceAsStream("segment.dict"));
            loadDict(bufReader);
            bufReader.close();
            bufReader = null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadDict(BufReader bufreader)
            throws IOException {
        wordOccuredSum = 0;
        maxWordLength = 0;
        wordCount = 0;
        posArray = new POSArray();
        int totalHeader = bufreader.readInt();
        headIndexers = new HeadIndexer[totalHeader];
        for (int i = 0; i < totalHeader; i++) {
            HeadIndexer headindexer = new HeadIndexer(bufreader, posArray);
            wordOccuredSum += headindexer.getWordOccuredSum();
            if (maxWordLength < headindexer.getMaxWordLength()) {
                maxWordLength = headindexer.getMaxWordLength();
            }
            wordCount += headindexer.getWordCount();
            headIndexers[i] = headindexer;
            headIndexersHashMap.put(headindexer.getHeadStr(), headindexer);
        }
    }

    public void saveDict(RandomAccessFile randomaccessfile) {
        try {
            randomaccessfile.writeInt(getCapacity());
            for (int k = 0; k < headIndexers.length; k++) {
                headIndexers[k].save(randomaccessfile);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void saveToFile(String s) {
        File file = new File(s);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile randomaccessfile = new RandomAccessFile(file, "rw");
            saveDict(randomaccessfile);
            randomaccessfile.close();
            randomaccessfile = null;
        } catch (IOException ioexception) {
            System.out.println((new StringBuilder("HashDictionary.saveToFile() error:")).append(ioexception.getMessage()).toString());
        }
    }

    public void dumpToFile(String s) {
        File file = new File(s);
        try {
            BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(new FileOutputStream(file));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((new StringBuilder("不同首字数量:")).append(getCapacity()).toString());
            stringBuilder.append((new StringBuilder(",最长词长度:")).append(getMaxWordLength()).toString());
            stringBuilder.append((new StringBuilder(",所有词的全部出现次数:")).append(getWordOccuredSum()).append("\n").toString());
            stringBuilder.append((new StringBuilder("词性列表：(共出现次数")).append(posArray.getOccuredSum()).append(")\n").toString());
            stringBuilder.append(posArray.toString());
            bufferedoutputstream.write(stringBuilder.toString().getBytes());
            stringBuilder.setLength(0);
            for (int k = 0; k < headIndexers.length; k++) {
                stringBuilder.append(headIndexers[k].toDBFString());
                bufferedoutputstream.write(stringBuilder.toString().getBytes());
                stringBuilder.setLength(0);
            }

            bufferedoutputstream.close();
        } catch (IOException ioexception) {
            System.out.println((new StringBuilder("HashDictionary.dumpToFile() error:")).append(ioexception.getMessage()).toString());
        }
    }

    private HeadIndexer lookupHeadIndexer(String s) {
        return (HeadIndexer) headIndexersHashMap.get(s);
    }

    @Override
    public IWord getWordItem(String wordStr) {
        String head = wordStr.substring(0, 1);
        HeadIndexer headindexer = lookupHeadIndexer(head);
        if (headindexer != null) {
            IWord word = headindexer.findWord(wordStr);
            if (word != null) {
                return word;
            }
        }
        return null;
    }

    @Override
    public IWord[] getWordItems(String sentenceStr) {
        String head = sentenceStr.substring(0, 1);
        HeadIndexer headIndexer = lookupHeadIndexer(head);
        if (headIndexer != null) {
            IWord words[] = headIndexer.findMultiWord(sentenceStr);
            if (words != null) {
                return words;
            }
        }
        return null;
    }

    public IWord getExactWordItem(String wordStr) {
        IWord word = null;
        String head = wordStr.substring(0, 1);
        HeadIndexer headindexer = lookupHeadIndexer(head);
        if (headindexer != null) {
            word = headindexer.get(wordStr);
        }
        return word;
    }
    private HashMap headIndexersHashMap = new HashMap();
    private HeadIndexer headIndexers[];
    private boolean loaded = false;
    private int maxWordLength;
    private int wordOccuredSum;
    private int wordCount;
    private POSArray posArray;
}
