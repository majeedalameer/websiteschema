package websiteschema.mpsegment.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class BufferedRandomAccessFile
        implements BufReader {

    public BufferedRandomAccessFile(InputStream is)
            throws IOException {
        size = 0;
        pos = 0;
        channelSize = 0;
        wrap(is);
    }

    private void wrap(InputStream is) throws IOException {
        int count = 0;
        int step = 1000000;
        int len = 0;
        byte buf[] = new byte[step];
        Arrays.fill(buf, (byte) 0);
        dis = new DataInputStream(is);
        do {
            byte tmp[] = new byte[step];
            Arrays.fill(tmp, (byte) 0);
            len = dis.read(tmp);
            if (len > 0) {
                if (count + len > buf.length) {
                    byte buff[] = new byte[buf.length + step];
                    Arrays.fill(buff, (byte) 0);
                    System.arraycopy(buf, 0, buff, 0, buf.length);
                    buf = buff;
                }
                System.arraycopy(tmp, 0, buf, count, len);
                count += len;
            }
        } while (len >= 0);

        buffer = ByteBuffer.wrap(buf, 0, count);
        size = count;
        channelSize = (int) size;

    }

    @Override
    public void close()
            throws IOException {
        if (null != dis) {
            dis.close();
        }
        if (null != buffer) {
            buffer.clear();
        }
        if (null != channel) {
            channel.close();
        }
        if (null != file) {
            file.close();
        }
    }

    @Override
    public int read()
            throws IOException {
        if (pos < channelSize) {
            b = buffer.get(pos);
            pos++;
            return 0xff & b;
        } else {
            throw new EOFException();
        }
    }

    @Override
    public byte readByte()
            throws IOException {
        if (pos < channelSize) {
            b = buffer.get(pos);
            pos++;
            return b;
        } else {
            throw new EOFException();
        }
    }

    @Override
    public int readIntByte()
            throws IOException {
        int i = read();
        if (i < 0) {
            throw new EOFException();
        } else {
            return i;
        }
    }

    @Override
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

    @Override
    public int read(byte abyte0[])
            throws IOException {
        return read(abyte0, 0, abyte0.length);
    }

    @Override
    public int read(byte abyte0[], int i, int j)
            throws IOException {
        if (pos + j < channelSize) {
            for (int k = 0; k < j; k++) {
                abyte0[k] = buffer.get(pos);
                pos++;
            }

            return 1;
        } else {
            return -1;
        }
    }
    private long size;
    private int channelSize;
    private int pos;
    private DataInputStream dis = null;
    private RandomAccessFile file;
    private FileChannel channel;
    private ByteBuffer buffer;
    private byte b;
}
