// Source File Name:   ByteArrayReader.java
package websiteschema.mpsegment.util;

import java.io.EOFException;
import java.io.IOException;

// Referenced classes of package cnnlp.util:
//            BufReader
public class ByteArrayReader
        implements BufReader {

    public ByteArrayReader(byte abyte0[]) {
        a = 0L;
        b = 0;
        c = 0;
        d = abyte0;
        b = d.length;
    }

    public void close()
            throws IOException {
        d = null;
    }

    public int read()
            throws IOException {
        if (c < b) {
            e = d[c];
            c++;
            return 0xff & e;
        } else {
            throw new EOFException();
        }
    }

    public byte readByte()
            throws IOException {
        if (c < b) {
            e = d[c];
            c++;
            return e;
        } else {
            throw new EOFException();
        }
    }

    public int readIntByte()
            throws IOException {
        int i = read();
        if (i < 0) {
            throw new EOFException();
        } else {
            return i;
        }
    }

    public int readInt()
            throws IOException {
        int i = read();
        int j = read();
        int k = read();
        int l = read();
        if ((i | j | k | l) < 0) {
            throw new EOFException();
        } else {
            return (i << 24) + (j << 16) + (k << 8) + l;
        }
    }

    public int read(byte abyte0[])
            throws IOException {
        return read(abyte0, 0, abyte0.length);
    }

    public int read(byte abyte0[], int i, int j)
            throws IOException {
        if (c + j < b) {
            for (int k = 0; k < j; k++) {
                abyte0[k] = d[c];
                c++;
            }

            return 1;
        } else {
            return -1;
        }
    }
    private long a;
    private int b;
    private int c;
    private byte d[];
    private byte e;
}