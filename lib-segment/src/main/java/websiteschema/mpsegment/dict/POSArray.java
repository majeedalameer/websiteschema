package websiteschema.mpsegment.dict;

import websiteschema.mpsegment.util.BufReader;
import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;

public class POSArray
        implements Serializable {

    private static final Logger l = Logger.getLogger("segment");

    public POSArray(RandomAccessFile randomaccessfile)
            throws IOException {
        load(randomaccessfile);
    }

    public POSArray(BufReader bufreader)
            throws IOException {
        load(bufreader);
    }

    public POSArray() {
        posTable = new HashMap<String, POS>();
    }

    public void setPOSCount(String pos, int freq) {
        if (pos == null || pos.trim().equals("")) {
            return;
        }
        POS a1 = (POS) posTable.get(pos.trim());
        if (a1 != null) {
            a1.setCount(freq);
        } else {
            a1 = new POS(pos.trim(), freq);
        }
        posTable.put(pos.trim(), a1);
    }

    public void setPOSCount(int count) {
        double discount = (double) getOccuredSum();
        if (discount > 0.0D) {
            discount = (double) count / discount;
        }
        for (String name : posTable.keySet()) {
            POS pos = (POS) posTable.get(name);
            double d1 = ((double) pos.getCount()) * discount;
            setPOSCount(name, (int) d1);
        }

    }

    public HashMap<String, POS> getPOSTable() {
        return posTable;
    }

    public void add(POSArray posArray) {
        if (posArray == null) {
            return;
        }

        for (String name : posArray.getPOSTable().keySet()) {
            add(posArray.getPOSTable().get(name));
        }
    }

    public void add(POS pos) {
        if (pos == null) {
            return;
        }
        POS a2 = (POS) posTable.get(pos.getName());
        if (a2 != null) {
            a2.setCount(a2.getCount() + pos.getCount());
        } else {
            a2 = new POS(pos.getName(), pos.getCount());
        }
        posTable.put(a2.getName(), a2);
    }

    public void incPOSCount(String s) {
        if (s == null || s.trim().equals("")) {
            return;
        }
        POS a1 = (POS) posTable.get(s.trim());
        if (a1 != null) {
            a1.setCount(a1.getCount() + 1);
        } else {
            a1 = new POS(s.trim(), 1);
        }
        posTable.put(s.trim(), a1);
    }

    public int getSize() {
        return posTable.size();
    }

    public int[][] getWordPOSTable() {
        POS arrayPOS[] = new POS[getSize()];
        int i = 0;
        for (String name : posTable.keySet()) {
            arrayPOS[i++] = posTable.get(name);
        }
        int arrayPOSAndFreq[][] = new int[arrayPOS.length][2];
        for (int j = 0; j < arrayPOS.length; j++) {
            arrayPOSAndFreq[j][0] = POSUtil.getPOSIndex(arrayPOS[j].getName());
            arrayPOSAndFreq[j][1] = arrayPOS[j].getCount();
        }

        return arrayPOSAndFreq;
    }

    public int getWordPOSTable(int arrayPOSAndFreq[][]) {
        int i = 0;
        int j = 0;
        for (String name : posTable.keySet()) {
            POS pos = posTable.get(name);
            if (j < arrayPOSAndFreq.length) {
                arrayPOSAndFreq[j][0] = POSUtil.getPOSIndex(pos.getName());
                arrayPOSAndFreq[j][1] = pos.getCount();
            }
            j++;
        }

        i = j;
        for (int k = i; k < arrayPOSAndFreq.length; k++) {
            arrayPOSAndFreq[k][0] = 0;
            arrayPOSAndFreq[k][1] = 0;
        }

        return i;
    }

    public int getWordMaxPOS() {
        int posIndex = 1;
        int count = 0;
        for (String name : posTable.keySet()) {
            POS pos = posTable.get(name);
            if (pos.getCount() > count) {
                posIndex = POSUtil.getPOSIndex(pos.getName());
                count = pos.getCount();
            }
        }

        return posIndex;
    }

    private void load(RandomAccessFile resources)
            throws IOException {
        byte numPos = resources.readByte();
        posTable = new HashMap<String, POS>(numPos);
        for (int i = 0; i < numPos; i++) {
            byte nameLength = resources.readByte();
            byte nameBytes[] = new byte[nameLength];
            resources.read(nameBytes);
            String name = new String(nameBytes);
            int count = resources.readInt();
            l.trace(name + "->" + count);
            POS pos = new POS(name, count);
            posTable.put(name, pos);
        }

    }

    private void load(BufReader resources)
            throws IOException {
        int size = resources.readIntByte();
        posTable = new HashMap<String, POS>(size);
        for (int i = 0; i < size; i++) {
            int len = resources.readIntByte();
            byte nameBytes[] = new byte[len];
            resources.read(nameBytes);
            String name = new String(nameBytes);
            int count = resources.readInt();
            l.trace(name + "->" + count);
            POS pos = new POS(name, count);
            posTable.put(name, pos);
        }

    }

    public void save(RandomAccessFile randomaccessfile)
            throws IOException {
        randomaccessfile.write((byte) getSize());
        POS pos[] = new POS[getSize()];
        int i = 0;
        for (String name : posTable.keySet()) {
            pos[i++] = posTable.get(name);
        }
        Arrays.sort(pos);
        for (int j = 0; j < pos.length; j++) {
            byte abyte0[] = pos[j].getName().getBytes();
            randomaccessfile.write((byte) abyte0.length);
            randomaccessfile.write(abyte0);
            randomaccessfile.writeInt(pos[j].getCount());
        }

    }

    public long getOccuredCount(String s) {
        if (s == null || s.trim().equals("")) {
            return 0L;
        }
        POS pos = (POS) posTable.get(s.trim());
        if (pos == null) {
            return 0L;
        } else {
            return (long) pos.getCount();
        }
    }

    public long getOccuredSum() {
        long sum = 0L;
        for (String name : posTable.keySet()) {
            POS pos = posTable.get(name);
            sum += pos.getCount();
        }

        return sum;
    }

    @Override
    public String toString() {
        StringBuilder stringbuffer = new StringBuilder();
        for (String name : posTable.keySet()) {
            POS pos = posTable.get(name);
            stringbuffer.append((new StringBuilder(String.valueOf(pos.toString()))).append("\n").toString());
        }

        return stringbuffer.toString();
    }

    public String toDBFString(String s) {
        StringBuilder stringbuffer = new StringBuilder();
        for (String name : posTable.keySet()) {
            POS pos = posTable.get(name);
            stringbuffer.append((new StringBuilder(String.valueOf(s))).append(pos.toDBFString()).append("\n").toString());
        }

        return stringbuffer.toString();
    }
    HashMap<String, POS> posTable;
}
