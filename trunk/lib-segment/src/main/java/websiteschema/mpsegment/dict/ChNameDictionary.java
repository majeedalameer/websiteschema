package websiteschema.mpsegment.dict;

import websiteschema.mpsegment.conf.ReadDataFile;
import java.io.*;
import java.util.Map;
import org.apache.log4j.Logger;
import websiteschema.mpsegment.util.SerializeHandler;

public class ChNameDictionary {

    private static Logger l = Logger.getLogger("segment");

    public ChNameDictionary() {
        factor = 0.88400000000000001D;
        totalXingFreq = 0;
        totalXingProb = 0.0D;
        xingTop = 1;
        mingTop = 1;
    }

    public void outSummary() {
        System.out.println((new StringBuilder("xingTop=")).append(xingTop).toString());
        System.out.println((new StringBuilder("totalXingFreq=")).append(totalXingFreq).toString());
        System.out.println((new StringBuilder("totalXingProb=")).append(totalXingProb).toString());
        System.out.println((new StringBuilder("mingTop=")).append(mingTop).toString());
        for (int i1 = 0; i1 < 3; i1++) {
            System.out.println((new StringBuilder("totalMingProb[i]=")).append(totalMingProb[i1]).toString());
        }
    }

    private int getXingProb(String s) {
        int i1 = get(xingHashMap, s);
        double d1 = 0.0D;
        if (i1 <= 0) {
            i1 = 0;
        } else {
            d1 = (double) xingFreq[i1] * (1.0D + xingProb[i1]);
        }
        return (int) d1;
    }

    private int getMingFreq0(String s) {
        int i1 = get(mingHashMap, s);
        if (i1 <= 0) {
            i1 = 0;
        } else {
            i1 = mingFreqs[i1][0];
        }
        return i1;
    }

    private int get(Map<String,Integer> map, String key) {
        if(map.containsKey(key)) {
            return map.get(key);
        }
        return 0;
    }

    private int getMingFreq1(String s) {
        int i1 = get(mingHashMap, s);
        if (i1 <= 0) {
            i1 = 0;
        } else {
            i1 = mingFreqs[i1][1];
        }
        return i1;
    }

    private int getMingFreq2(String s) {
        int i1 = get(mingHashMap, s);
        if (i1 <= 0) {
            i1 = 0;
        } else {
            i1 = mingFreqs[i1][2];
        }
        return i1;
    }

    public boolean isXing(String s) {
        int i1 = get(xingHashMap, s);
        return i1 > 0;
    }

    public double computeLgLP3(String s, String s1, String s2) {
        double d2 = getXingProb(s);
        double d3 = getMingFreq0(s1);
        double d4 = getMingFreq1(s2);
        d2 = d2 * factor * ((d3 + d4) / 1000000D);
        if (d4 <= 0.0D && d2 > 1.0D) {
            d2 *= 0.90000000000000002D;
        }
        return d2;
    }

    public double computeLgLP3_2(String s, String s1) {
        double d2 = getXingProb(s.substring(0, 1));
        double d3 = getMingFreq0(s.substring(1));
        double d4 = getMingFreq1(s1);
        d2 = d2 * factor * ((d3 + d4) / 1000000D);
        if (d4 <= 0.0D && d2 > 1.0D) {
            d2 *= 0.90000000000000002D;
        }
        return d2;
    }

    public double computeLgLP2(String s, String s1) {
        double d2 = getXingProb(s);
        double d3 = getMingFreq2(s1);
        d2 *= d3 / 1000000D;
        return d2;
    }

    public double computeLgMing23(String s, String s1) {
        double d1 = getMingFreq0(s);
        double d2 = getMingFreq1(s1);
        d1 += d2;
        d1 /= 1000D;
        return d1;
    }

    public double getRightBoundaryWordLP(String s) {
        int i1 = get(rightBoundaryHashMap, s);
        double d1 = 0.0D;
        if (i1 > 0) {
            d1 = (rightBoundaryProbs[i1] - 0.10000000000000001D) / 3D;
        }
        return d1;
    }

    public void testName(String s) {
        System.out.print(s);
        boolean flag = false;
        byte mingLength = 3;
        int i1 = s.length();
        String s3 = "";
        if (get(fuXing, s.substring(0, 2)) > 0) {
            flag = true;
        }
        String s1;
        String s2;
        if (flag) {
            s1 = s.substring(0, 2);
            if (i1 > 3) {
                s2 = s.substring(2, 3);
                s3 = s.substring(3, 4);
            } else {
                s2 = s.substring(2, 3);
                mingLength = 2;
            }
        } else {
            s1 = s.substring(0, 1);
            if (i1 > 2) {
                s2 = s.substring(1, 2);
                s3 = s.substring(2, 3);
            } else {
                s2 = s.substring(1, 2);
                mingLength = 2;
            }
        }
        if (mingLength == 2) {
            System.out.println((new StringBuilder(",")).append(computeLgLP2(s1, s2)).toString());
        } else {
            if (computeLgLP3(s1, s2, s3) > computeLgLP2(s2, s3)) {
                System.out.print((new StringBuilder(",")).append(computeLgLP3(s1, s2, s3)).toString());
                System.out.println((new StringBuilder(",")).append(computeLgLP2(s2, s3)).toString());
            } else {
                System.out.print((new StringBuilder(",")).append(computeLgLP3(s1, s2, s3)).toString());
                System.out.println((new StringBuilder(",")).append(computeLgLP2(s2, s3)).append(",1").toString());
            }
            System.out.println((new StringBuilder(String.valueOf(computeLgLP3(s1, s2, s3)))).append("  ").append(computeLgLP2(s2, s3)).toString());
        }
    }

    public void readFuxing()
            throws Exception {
        FileReader filereader = new FileReader("D:/dwp2007/dictionary/name/train1/fuxing.txt");
        BufferedReader bufferedreader = new BufferedReader(filereader);
        String s = "";
        for (String s1 = ""; s1 != null;) {
            s1 = bufferedreader.readLine();
            if (s1 == null) {
                break;
            }
            s1 = s1.trim();
            fuXing.put(s1, 1);
        }

        bufferedreader.close();
        filereader.close();
    }

    public void testNameFile(String s)
            throws Exception {
        FileReader filereader = new FileReader(s);
        BufferedReader bufferedreader = new BufferedReader(filereader);
        String s1 = "";
        for (String s2 = ""; s2 != null;) {
            s2 = bufferedreader.readLine();
            if (s2 == null) {
                break;
            }
            s2 = s2.trim();
            if (s2.length() >= 2) {
                testName(s2);
            }
        }

        bufferedreader.close();
        filereader.close();
    }

    public String toText() {
        String ls = System.getProperty("line.separator");
        String space = " ";
        StringBuilder sb = new StringBuilder();
        sb.append("//ChName.dict").append(ls);
        sb.append("[xingHashMap] //").append(xingHashMap.size()).append(ls);
        for (String key : xingHashMap.keySet()) {
            sb.append(key).append(space).append(xingHashMap.get(key)).append(space);
        }
        sb.append(ls);
        sb.append("[xingFreq] //").append(xingFreq.length).append(ls);
        for (int i = 0; i < xingFreq.length; i++) {
            sb.append(xingFreq[i]).append(space);
        }

        sb.append(ls);
        sb.append("[totalXingProb]").append(ls);
        sb.append(totalXingProb).append(ls);
        sb.append("[mingHashMap] //").append(mingHashMap.size()).append(ls);

        for (String key : mingHashMap.keySet()) {
            sb.append(key).append(space).append(mingHashMap.get(key)).append(space);
        }
        sb.append(ls);
        sb.append("[mingFreqs] //").append(mingFreqs.length).append(ls);
        for (int i = 0; i < mingFreqs.length; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(mingFreqs[i][j]).append(space);
            }
            sb.append("\t");
        }

        sb.append(ls);
        sb.append("[totalMingProb] //").append(totalMingProb.length).append(ls);
        for (int i = 0; i < totalMingProb.length; i++) {
            sb.append(totalMingProb[i]).append(space);
        }

        sb.append(ls);
        sb.append("[fuXing] //").append(fuXing.size()).append(ls);
        for (String key : fuXing.keySet()) {
            sb.append(key).append(space).append(fuXing.get(key)).append(space);
        }
        sb.append(ls);
        sb.append("[xingProb] //").append(xingProb.length).append(ls);
        for (int i = 0; i < xingProb.length; i++) {
            sb.append(xingProb[i]).append(space);
        }
        sb.append(ls);
        sb.append("[rightBoundaryHashMap] //").append(rightBoundaryHashMap.size()).append(ls);
        for (String key : rightBoundaryHashMap.keySet()) {
            sb.append(key).append(space).append(rightBoundaryHashMap.get(key)).append(space);
        }
        sb.append(ls);
        sb.append("[rightBoundaryProbs] //").append(rightBoundaryProbs.length).append(ls);
        for (int i = 0; i < rightBoundaryProbs.length; i++) {
            sb.append(rightBoundaryProbs[i]).append(space);
        }
        sb.append(ls);
        return sb.toString();
    }

    public void saveNameDict(String s) {
        try {
            SerializeHandler writeHandler = new SerializeHandler(new File(s), SerializeHandler.MODE_WRITE_ONLY);
            writeHandler.serializeMapStringInt(xingHashMap);
            writeHandler.serializeArrayInt(xingFreq);
            writeHandler.serializeDouble(totalXingProb);
            writeHandler.serializeMapStringInt(mingHashMap);
            writeHandler.serialize2DArrayInt(mingFreqs);
            writeHandler.serializeArrayDouble(totalMingProb);
            writeHandler.serializeMapStringInt(fuXing);
            writeHandler.serializeArrayDouble(xingProb);
            writeHandler.serializeMapStringInt(rightBoundaryHashMap);
            writeHandler.serializeArrayDouble(rightBoundaryProbs);
            writeHandler.close();
        } catch (Exception exception) {
            System.out.println((new StringBuilder("Error: saveNameDict.save(")).append(s).append(") ").append(exception.getMessage()).toString());
            l.error((new StringBuilder("Error: saveNameDict.save(")).append(s).append(") ").append(exception.getMessage()).toString());
        }
    }

    public void loadNameDict(String dictFile) {
        try {
            ObjectInputStream objectinputstream = null;
            File f = new File(dictFile);
            SerializeHandler readHandler = null;
            if (f.exists()) {
                readHandler = new SerializeHandler(f, SerializeHandler.MODE_READ_ONLY);
            } else {
                objectinputstream = new ObjectInputStream(
                        new ByteArrayInputStream(new ReadDataFile().getData(dictFile)));
                readHandler = new SerializeHandler(objectinputstream);
            }
            xingHashMap = readHandler.deserializeMapStringInt();//(TObjectIntHashMap) objectinputstream.readObject();
            xingFreq = readHandler.deserializeArrayInt();//(int[]) objectinputstream.readObject();
            totalXingProb = readHandler.deserializeDouble();//objectinputstream.readDouble();
            mingHashMap = readHandler.deserializeMapStringInt();//(TObjectIntHashMap) objectinputstream.readObject();
            mingFreqs = readHandler.deserialize2DArrayInt();//(int[][]) objectinputstream.readObject();
            totalMingProb = readHandler.deserializeArrayDouble();//(double[]) objectinputstream.readObject();
            fuXing = readHandler.deserializeMapStringInt();//(TObjectIntHashMap) objectinputstream.readObject();
            xingProb = readHandler.deserializeArrayDouble();//(double[]) objectinputstream.readObject();
            rightBoundaryHashMap = readHandler.deserializeMapStringInt();//(TObjectIntHashMap) objectinputstream.readObject();
            rightBoundaryProbs = readHandler.deserializeArrayDouble();//(double[]) objectinputstream.readObject();
            objectinputstream.close();
        } catch (Exception exception) {
            System.out.println((new StringBuilder()).append("[Segment] ").append(dictFile).append("没找到！").append(exception.getMessage()).toString());
            l.debug((new StringBuilder()).append("[Segment] ").append(dictFile).append("没找到！").append(exception.getMessage()).toString());
        }
    }
    private double factor;
    private Map<String, Integer> xingHashMap;
    private int xingFreq[];
    private double xingProb[];
    private int totalXingFreq;
    private double totalXingProb;
    private Map<String, Integer> mingHashMap;
    private int mingFreqs[][];
    private double totalMingProb[];
    private int xingTop;
    private int mingTop;
    private Map<String, Integer> fuXing;

    public Map<String, Integer> getFuXing() {
        return fuXing;
    }
    private Map<String, Integer> rightBoundaryHashMap;
    private double rightBoundaryProbs[];
}
