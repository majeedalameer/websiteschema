package websiteschema.mpsegment.dict;

import websiteschema.mpsegment.util.BufReader;
import java.io.*;
import websiteschema.mpsegment.conf.Configure;

public class WordImpl
        implements Serializable, Comparable, IWord {

    public WordImpl(String s) {
        log2Freq = 0;
        wordName = s;
        domaintype = 0;
    }

    public WordImpl(String s, int i) {
        log2Freq = 0;
        wordName = s;
        domaintype = i;
    }

    public WordImpl(BufReader bufreader)
            throws IOException {
        log2Freq = 0;
        load(bufreader);
    }

    @Override
    public void setOccuredCount(String posName, int freq) {
        POSAndFreq.setFreq(indexOfPosDB, wordPOSNumber, POSUtil.getPOSIndex(posName), freq);
    }

    @Override
    public void setOccuredCount(int factor) {
        POSAndFreq.setScaleFreq(indexOfPosDB, wordPOSNumber, factor);
        log2Freq = 0;
        getLog2Freq();
    }

    @Override
    public POSArray getPOSArray() {
        POSArray posArray = new POSArray();
        for (int j = 0; j < wordPOSNumber; j++) {
            String name = POSUtil.getPOSString(POSAndFreq.getPOS(indexOfPosDB + j));
            int count = POSAndFreq.getFreq(indexOfPosDB + j);
            POS pos = new POS(name, count);
            posArray.add(pos);
        }
        return posArray;
    }

    @Override
    public int getWordMaxPOS() {
        return POSAndFreq.getMaxOccuriedPOS(indexOfPosDB, wordPOSNumber);
    }

    @Override
    public int getWordPOSTable(int posTable[][]) {
        for (int i = 0; i < wordPOSNumber; i++) {
            if (i < posTable.length) {
                posTable[i][0] = POSAndFreq.getPOS(indexOfPosDB + i);
                posTable[i][1] = POSAndFreq.getFreq(indexOfPosDB + i);
            }
        }
        for (int j = wordPOSNumber; j < posTable.length; j++) {
            posTable[j][0] = 0;
            posTable[j][1] = 0;
        }
        return wordPOSNumber;
    }

    @Override
    public String getWordName() {
        return wordName;
    }

    @Override
    public void setWordName(String s) {
        wordName = s;
    }

    @Override
    public int getWordLength() {
        return wordName.length();
    }

    @Override
    public void setDomainType(int i) {
        domaintype = i;
    }

    @Override
    public int getDomainType() {
        return domaintype;
    }

    @Override
    public int getLog2Freq() {
        if (log2Freq == 0) {
            log2Freq = (int) (Math.log(getOccuredSum() + 1L) * 100D);
        }
        return log2Freq;
    }

    @Override
    public long getOccuredSum() {
        return (long) POSAndFreq.getFreqSum(indexOfPosDB, wordPOSNumber);
    }

    @Override
    public long getOccuredCount(String s) {
        int i = POSUtil.getPOSIndex(s);
        return (long) POSAndFreq.getPOSFreq(indexOfPosDB, wordPOSNumber, i);
    }

    @Override
    public void incOccuredCount(String s) {
        int i = POSUtil.getPOSIndex(s);
        POSAndFreq.incPOSFreq(indexOfPosDB, wordPOSNumber, i);
    }

    public void setPOSFreq(int i, int j) {
        indexOfPosDB = i;
        wordPOSNumber = j;
    }

    public final void load(RandomAccessFile randomaccessfile)
            throws IOException {
        byte byte0 = randomaccessfile.readByte();
        byte abyte0[] = new byte[byte0];
        randomaccessfile.read(abyte0);
        wordName = new String(abyte0, Configure.getInstance().getFileEncoding());
        domaintype = randomaccessfile.readInt();
        domaintype /= 100;
        indexOfPosDB = randomaccessfile.readInt();
        wordPOSNumber = randomaccessfile.readInt();
    }

    public String toText() {
        String space = " ";
        StringBuilder sb = new StringBuilder();

        sb.append(wordName).append(space);
        sb.append(domaintype).append(space);
        sb.append(indexOfPosDB).append(space);
        sb.append(wordPOSNumber).append(space);
        return sb.toString();
    }

    private void load(BufReader bufreader)
            throws IOException {
        int i = bufreader.readIntByte();
        byte wordNameBytes[] = new byte[i];
        bufreader.read(wordNameBytes);
        wordName = new String(wordNameBytes, Configure.getInstance().getFileEncoding());
        domaintype = bufreader.readInt();
        domaintype /= 100;
        indexOfPosDB = bufreader.readInt();
        wordPOSNumber = bufreader.readInt();
    }

    @Override
    public void save(RandomAccessFile randomaccessfile)
            throws IOException {
        byte abyte0[] = wordName.getBytes(Configure.getInstance().getFileEncoding());
        randomaccessfile.write((byte) abyte0.length);
        randomaccessfile.write(abyte0);
        domaintype *= 100;
        int i = domaintype;
        randomaccessfile.writeInt(i);
        randomaccessfile.writeInt(indexOfPosDB);
        randomaccessfile.writeInt(wordPOSNumber);
    }

    @Override
    public int compareTo(Object obj) {
        if (obj != null && (obj instanceof String)) {
            return wordName.compareTo((String) obj);
        }
        if (obj != null && (obj instanceof WordImpl)) {
            return wordName.compareTo(((WordImpl) obj).wordName);
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof WordImpl)) {
            return wordName.equals(((WordImpl) obj).wordName);
        }
        if (obj != null && (obj instanceof String)) {
            return wordName.equals(obj);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringbuffer = new StringBuilder();
        stringbuffer.append((new StringBuilder(String.valueOf(getWordName()))).append("\n").toString());
        stringbuffer.append(getPOSArray().toString());
        return stringbuffer.toString();
    }

    @Override
    public String toWordString() {
        StringBuilder stringbuffer = new StringBuilder();
        stringbuffer.append((new StringBuilder(String.valueOf(getWordName()))).append("\\").toString());
        return stringbuffer.toString();
    }

    @Override
    public String toDBFString() {
        StringBuilder stringbuffer = new StringBuilder();
        stringbuffer.append(getPOSArray().toDBFString(getWordName()));
        return stringbuffer.toString();
    }
    private String wordName;
    private int domaintype;
    private int log2Freq;
    private int indexOfPosDB;
    private int wordPOSNumber;
}
