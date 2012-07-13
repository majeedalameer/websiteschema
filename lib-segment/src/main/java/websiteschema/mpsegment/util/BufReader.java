// Source File Name:   BufReader.java
package websiteschema.mpsegment.util;

import java.io.IOException;

public interface BufReader {

    public abstract void close() throws IOException;

    public abstract int read() throws IOException;

    public abstract int read(byte abyte0[]) throws IOException;

    public abstract int read(byte abyte0[], int i, int j) throws IOException;

    public abstract byte readByte() throws IOException;

    public abstract int readInt() throws IOException;

    public abstract int readIntByte() throws IOException;
}
