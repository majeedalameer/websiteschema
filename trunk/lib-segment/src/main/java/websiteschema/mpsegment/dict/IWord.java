package websiteschema.mpsegment.dict;

import java.io.IOException;
import java.io.RandomAccessFile;

public interface IWord {

    public int compareTo(Object obj);

    public int getLog2Freq();

    public int getDomainType();

    public long getOccuredCount(String s);

    public long getOccuredSum();

    public POSArray getPOSArray();

    public int[][] getWordPOSTable();

    public int getWordLength();

    public String getWordName();

    public void incOccuredCount(String s);

    public void save(RandomAccessFile randomaccessfile) throws IOException;

    public void setDomainType(int i);

    public void setOccuredCount(String s, int i);

    public void setOccuredCount(int i);

    public void setWordName(String s);

    public int getWordPOSTable(int ai[][]);

    public int getWordMaxPOS();

    public String toDBFString();

    public String toWordString();
}