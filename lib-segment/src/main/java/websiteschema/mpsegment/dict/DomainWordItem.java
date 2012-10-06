package websiteschema.mpsegment.dict;

import websiteschema.mpsegment.conf.Configure;
import websiteschema.mpsegment.util.BufReader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;

public class DomainWordItem
        implements Serializable, Comparable, IWord {

    public DomainWordItem(String wordName) {
        log2Freq = 0;
        this.wordName = wordName;
        domainType = 0;
    }

    public DomainWordItem(String wordName, int domainType) {
        log2Freq = 0;
        this.wordName = wordName;
        this.domainType = domainType;
    }

    public DomainWordItem(RandomAccessFile randomaccessfile)
            throws IOException {
        log2Freq = 0;
        load(randomaccessfile);
    }

    public DomainWordItem(BufReader bufreader)
            throws IOException {
        log2Freq = 0;
        load(bufreader);
    }

    @Override
    public void setOccuredCount(String pos, int freq) {
        posArray.setPOSCount(pos, freq);
    }

    @Override
    public void setOccuredSum(int factor) {
        posArray.setPOSCount(factor);
        log2Freq = 0;
        getLog2Freq();
    }

    @Override
    public POSArray getPOSArray() {
        return posArray;
    }

    @Override
    public int[][] getWordPOSTable() {
        return getPOSArray().getWordPOSTable();
    }

    @Override
    public int getWordMaxPOS() {
        return posArray.getWordMaxPOS();
    }

    @Override
    public int getWordPOSTable(int ai[][]) {
        return posArray.getWordPOSTable(ai);
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
    public void setDomainType(int domainType) {
        this.domainType = domainType;
    }

    @Override
    public int getDomainType() {
        return domainType;
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
        return posArray.getOccurredSum();
    }

    @Override
    public long getOccuredCount(String s) {
        return posArray.getOccurredCount(s);
    }

    @Override
    public void incOccuredCount(String s) {
        posArray.incPOSCount(s);
    }

    private void load(RandomAccessFile randomaccessfile)
            throws IOException {
        byte numByteWordName = randomaccessfile.readByte();
        byte byteWordName[] = new byte[numByteWordName];
        randomaccessfile.read(byteWordName);
        wordName = new String(byteWordName, Configure.getInstance().getFileEncoding());
        domainType = randomaccessfile.readInt();
        id = randomaccessfile.readInt();
        posIndex = randomaccessfile.readInt();
        freq = randomaccessfile.readInt();
        posIndex1 = randomaccessfile.readInt();
        freq1 = randomaccessfile.readInt();

        addPOS(posIndex, freq);
        addPOS(posIndex1, freq1);
    }

    private void load(BufReader bufreader)
            throws IOException {
        int numByteWordName = bufreader.readIntByte();
        byte byteWordName[] = new byte[numByteWordName];
        bufreader.read(byteWordName);
        wordName = new String(byteWordName, Configure.getInstance().getFileEncoding());
        domainType = bufreader.readInt();
        id = bufreader.readInt();
        posIndex = bufreader.readInt();
        freq = bufreader.readInt();
        posIndex1 = bufreader.readInt();
        freq1 = bufreader.readInt();

        addPOS(posIndex, freq);
        addPOS(posIndex1, freq1);
    }

    @Override
    public void save(RandomAccessFile randomaccessfile)
            throws IOException {
        byte abyte0[] = wordName.getBytes(Configure.getInstance().getFileEncoding());
        randomaccessfile.write((byte) abyte0.length);
        randomaccessfile.write(abyte0);
        int t = domainType;
        randomaccessfile.writeInt(t);
        randomaccessfile.writeInt(id);

        int[][] pos_array = posArray.getWordPOSTable();
        if (null != pos_array) {
            if (pos_array.length > 0) {
                posIndex = pos_array[0][0];
                freq = pos_array[0][1];
                if (pos_array.length > 1) {
                    posIndex1 = pos_array[1][0];
                    freq1 = pos_array[1][1];
                }
            }
        }

        randomaccessfile.writeInt(posIndex);
        randomaccessfile.writeInt(freq);
        randomaccessfile.writeInt(posIndex1);
        randomaccessfile.writeInt(freq1);
    }

    @Override
    public int compareTo(Object obj) {
        if (obj != null && (obj instanceof String)) {
            return wordName.compareTo((String) obj);
        }
        if (obj != null && (obj instanceof DomainWordItem)) {
            String otherString = ((DomainWordItem) obj).wordName;
            return wordName.compareTo(otherString);
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof DomainWordItem)) {
            return wordName.equals(((DomainWordItem) obj).wordName);
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

    public int getId() {
        return id;
    }

    public void setId(int i) {
        id = i;
    }

    private void addPOS(int posIndex, int posFreq) {
        if (posIndex > 0) {
            String posString = POSUtil.getPOSString(posIndex);
            posArray.add(new POS(posString, posFreq));
        }
    }
    //
    private String wordName;
    private int domainType;
    private int log2Freq;
    private int id;
    private int posIndex;
    private int freq;
    private int posIndex1;
    private int freq1;
    POSArray posArray = new POSArray();
}
