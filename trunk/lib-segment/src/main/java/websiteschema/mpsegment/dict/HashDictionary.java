package websiteschema.mpsegment.dict;

import websiteschema.mpsegment.conf.ReadDataFile;
import websiteschema.mpsegment.util.BufReader;
import websiteschema.mpsegment.util.ByteArrayReader;
import java.io.*;
import java.util.HashMap;

public class HashDictionary implements IDictionary {

    public HashDictionary(String s) {
        maxWordLength = 0;
        wordOccuredSum = 0;
        wordCount = 0;
        posArray = null;
        multiCandidate = true;
        if (!loaded) {
            loadDict(s);
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

    public void setMultiCandidate(boolean flag) {
        multiCandidate = flag;
    }

    public String dictoString() {
        StringBuilder stringbuffer = new StringBuilder();
        //不同首字数量
        stringbuffer.append((new StringBuilder("不同首字数量:")).append(getCapacity()).toString());
        //最长词长度
        stringbuffer.append((new StringBuilder(",最长词长度:")).append(getMaxWordLength()).toString());
        //所有词的全部出现次数
        stringbuffer.append((new StringBuilder(",所有词的全部出现次数:")).append(getWordOccuredSum()).append("\n").toString());
        //词性列表：(共出现次数
        stringbuffer.append((new StringBuilder("词性列表：(共出现次数")).append(posArray.getOccuredSum()).append(")\n").toString());
        stringbuffer.append(posArray.toString());

        System.out.println(stringbuffer.toString());
        stringbuffer.setLength(0);
        for (int k = 0; k < headIndexers.length; k++) {
            stringbuffer.append(headIndexers[k].toDBFString());
            System.out.print(stringbuffer.toString());
            stringbuffer.setLength(0);
        }

        return stringbuffer.toString();
    }

    private synchronized void loadDict(String s) {
        try {
            ByteArrayReader bytearrayreader = new ByteArrayReader(new ReadDataFile().getData("segment.dict"));
            loadDict(((BufReader) (bytearrayreader)));
            bytearrayreader.close();
            bytearrayreader = null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void loadDict(BufReader bufreader)
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
            headIndexersHashMap.put(headindexer.getHeadWord(), headindexer);
        }

    }

    public String toText() {
        String line = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        sb.append("//segment.dict").append(line);
        // number of head indexers
        sb.append("[headIndexers.length]").append(line);
        sb.append(headIndexers.length).append(line);
        sb.append("//iWordItems: wordName logFreq posDBBase wordPOSTable ").append(line);
        //Head Indexers
        sb.append("[HeadIndexers]").append(line);
        for (int i = 0; i < headIndexers.length; i++) {
            sb.append(headIndexers[i].toText());
        }

        return sb.toString();
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

    public void logToFile(String s) {
        File file = new File(s);
        try {
            BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(new FileOutputStream(file));
            StringBuilder stringbuffer = new StringBuilder();
            stringbuffer.append((new StringBuilder("不同首字数量:")).append(getCapacity()).toString());
            stringbuffer.append((new StringBuilder(",最长词长度:")).append(getMaxWordLength()).toString());
            stringbuffer.append((new StringBuilder(",所有词的全部出现次数:")).append(getWordOccuredSum()).append("\n").toString());
            stringbuffer.append((new StringBuilder("词性列表：(共出现次数")).append(posArray.getOccuredSum()).append(")\n").toString());
            stringbuffer.append(posArray.toString());
            bufferedoutputstream.write(stringbuffer.toString().getBytes());
            stringbuffer.setLength(0);
            for (int k = 0; k < headIndexers.length; k++) {
                stringbuffer.append(headIndexers[k].toDBFString());
                bufferedoutputstream.write(stringbuffer.toString().getBytes());
                stringbuffer.setLength(0);
            }

            bufferedoutputstream.close();
        } catch (IOException ioexception) {
            System.out.println((new StringBuilder("HashDictionary.logToFile() error:")).append(ioexception.getMessage()).toString());
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
            IWord aiworditem1[] = headindexer.findMultiWord(wordStr);
            if (multiCandidate && aiworditem1 != null) {
                return aiworditem1[0];
            }
        }
        return null;
    }

    @Override
    public IWord[] getWordItems(String wordStr) {
        String head = wordStr.substring(0, 1);
        HeadIndexer headindexer = lookupHeadIndexer(head);
        if (headindexer != null) {
            IWord aiworditem1[] = headindexer.findMultiWord(wordStr);
            if (multiCandidate && aiworditem1 != null) {
                return aiworditem1;
            }
        }
        return null;
    }

    public IWord getExactWordItem(String s) {
        IWord iworditem = null;
        String s1 = s.substring(0, 1);
        HeadIndexer headindexer = lookupHeadIndexer(s1);
        if (headindexer != null) {
            iworditem = headindexer.searchWord(s);
        }
        return iworditem;
    }
    private HashMap headIndexersHashMap = new HashMap();
    private HeadIndexer headIndexers[];
    private boolean loaded = false;
    private int maxWordLength;
    private int wordOccuredSum;
    private int wordCount;
    private POSArray posArray;
    private boolean multiCandidate;
}
