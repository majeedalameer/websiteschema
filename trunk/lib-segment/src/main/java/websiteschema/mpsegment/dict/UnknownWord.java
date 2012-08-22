package websiteschema.mpsegment.dict;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author ray
 */
public class UnknownWord implements IWord {

    final static int log2Freq = (int) (Math.log(1) * 100D);
    String wordName;

    public UnknownWord(String wordName) {
        this.wordName = wordName;
    }

    @Override
    public int compareTo(Object obj) {
        if (obj instanceof UnknownWord) {
            wordName.compareTo(((UnknownWord) obj).wordName);
        }
        return 1;
    }

    @Override
    public int getLog2Freq() {
        return log2Freq;
    }

    @Override
    public int getDomainType() {
        return 0;
    }

    @Override
    public long getOccuredCount(String s) {
        if (s.equals(POSUtil.getPOSString(POSUtil.POS_UNKOWN))) {
            return 1L;
        }
        return 0L;
    }

    @Override
    public long getOccuredSum() {
        return 1L;
    }

    @Override
    public POSArray getPOSArray() {
        POSArray posArray = new POSArray();
        POS pos = new POS(POSUtil.getPOSString(POSUtil.POS_UNKOWN), 1);
        posArray.add(pos);
        return posArray;
    }

    @Override
    public int getWordLength() {
        return wordName.length();
    }

    @Override
    public String getWordName() {
        return wordName;
    }

    @Override
    public void incOccuredCount(String s) {
    }

    @Override
    public void save(RandomAccessFile randomaccessfile) throws IOException {
    }

    @Override
    public void setDomainType(int i) {
    }

    @Override
    public void setOccuredCount(String s, int i) {
    }

    @Override
    public void setOccuredCount(int i) {
    }

    @Override
    public void setWordName(String s) {
        this.wordName = s;
    }

    @Override
    public int getWordPOSTable(int[][] ai) {
        for (int i = 0; i < 1; i++) {
            if (i < ai.length) {
                ai[i][0] = POSUtil.POS_UNKOWN;
                ai[i][1] = 1;
            }
        }
        for (int j = 1; j < ai.length; j++) {
            ai[j][0] = 0;
            ai[j][1] = 0;
        }
        return 1;
    }

    @Override
    public int getWordMaxPOS() {
        return POSUtil.POS_UNKOWN;
    }

    @Override
    public String toDBFString() {
        StringBuilder stringbuffer = new StringBuilder();
        stringbuffer.append(getPOSArray().toDBFString(getWordName()));
        return stringbuffer.toString();
    }

    @Override
    public String toWordString() {
        StringBuilder stringbuffer = new StringBuilder();
        stringbuffer.append((new StringBuilder(String.valueOf(getWordName()))).append("\\").toString());
        return stringbuffer.toString();
    }
}