package websiteschema.mpsegment.dict;

import java.io.IOException;
import java.io.RandomAccessFile;

public interface IWord {

    public abstract int compareTo(Object obj);

    public abstract int getLog2Freq();

    public abstract int getDomainType();

    public abstract long getOccuredCount(String s);

    public abstract long getOccuredSum();

    public abstract POSArray getPOSArray();

    public abstract int getWordLength();

    public abstract String getWordName();

    public abstract void incOccuredCount(String s);

    public abstract void save(RandomAccessFile randomaccessfile)
            throws IOException;

    public abstract void setDomainType(int i);

    public abstract void setOccuredCount(String s, int i);

    public abstract void setOccuredCount(int i);

    public abstract void setWordName(String s);

    public abstract int getWordPOSTable(int ai[][]);

    public abstract int getWordMaxPOS();

    public abstract String toDBFString();

    public abstract String toWordString();
}