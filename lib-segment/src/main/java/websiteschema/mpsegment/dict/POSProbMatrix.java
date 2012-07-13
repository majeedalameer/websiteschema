package websiteschema.mpsegment.dict;

import websiteschema.mpsegment.conf.ReadDataFile;
import websiteschema.mpsegment.util.BufReader;
import websiteschema.mpsegment.util.ByteArrayReader;
import java.io.*;

public class POSProbMatrix {

    public POSProbMatrix() {
        numPOS = 45;
        posFreq = new int[numPOS];
        posBigram = new int[numPOS][numPOS];
        corpusSize = 1;
    }

    public POSProbMatrix(String s) {
        numPOS = 45;
        posFreq = new int[numPOS];
        posBigram = new int[numPOS][numPOS];
        corpusSize = 1;
        loadProbMatrix(s);
    }

    public int getCorpusSize() {
        return corpusSize;
    }

    public void addMatrix(String s, String s1) {
        int i = 0;
        int j = 0;
        if (s.length() > 0) {
            i = POSUtil.getPOSIndex(s);
        }
        if (s1.length() > 0) {
            j = POSUtil.getPOSIndex(s1);
        }
        if (i >= 0 && j >= 0) {
            posBigram[i][j]++;
            posFreq[j]++;
            if (j >= 44) {
                System.out.println((new StringBuilder("---")).append(s1).toString());
            }
        }
    }

    public void outMatrix() {
        for (int i = 0; i < numPOS; i++) {
            for (int l = 0; l < numPOS; l++) {
                System.out.print((new StringBuilder(String.valueOf(posBigram[i][l]))).append(" ").toString());
            }

            System.out.println((new StringBuilder("---")).append(i).toString());
        }

        System.out.println("---------------------------");
        for (int j = 0; j < numPOS; j++) {
            System.out.println((new StringBuilder(String.valueOf(POSUtil.getPOSString(j)))).append(" ").append(posFreq[j]).toString());
        }

        System.out.println("---------------------------");
        for (int k = 0; k < numPOS; k++) {
            System.out.println(posFreq[k]);
        }

        System.out.println("---------------------------");
    }

    public int getTagFreqs(int i) {
        return posFreq[i];
    }

    public double getCoProb(int posIndex1, int posIndex2) {
        double d1 = 0.0D;
        if (posFreq[posIndex1] == 0) {
            return 1.0000000000000001E-005D;
        } else {
            double d2 = (0.29999999999999999D * (double) (posFreq[posIndex2] + 1)) / (double) corpusSize + (0.69999999999999996D * (double) (posBigram[posIndex1][posIndex2] + 1)) / (double) posFreq[posIndex1];
            return d2;
        }
    }

    public void loadProbMatrix(BufReader bufreader)
            throws IOException {
        try {
            numPOS = bufreader.readInt();
            for (int i = 0; i < numPOS; i++) {
                posFreq[i] = bufreader.readInt();
                corpusSize += posFreq[i];
            }

            for (int j = 0; j < numPOS; j++) {
                for (int k = 0; k < numPOS; k++) {
                    posBigram[j][k] = bufreader.readInt();
                }

            }

        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }


    public String toText(){

       String ls = System.getProperty("line.separator");
       String space = " ";
       StringBuffer sb=new StringBuffer();

       sb.append("//POSMatrix.fre"+ls);
       sb.append("[numPOS]"+ls);
       sb.append(numPOS+ls);
       sb.append("[posFreq]"+ls);
       for (int i=0;i<numPOS;i++)
           sb.append(posFreq[i]+space);

       sb.append(ls);

       sb.append("[posBigram]"+ls);

       for (int i=0;i<numPOS;i++)
       {
           for (int j=0;j<numPOS;j++)
               sb.append(posBigram[i][j]+space);

           sb.append(ls);
       }
       


       return sb.toString();

    }

    public void loadProbMatrix(String s) {
        try {
            ByteArrayReader bytearrayreader = new ByteArrayReader(new ReadDataFile().getData("POSMatrix.fre"));
            loadProbMatrix(((BufReader) (bytearrayreader)));
            bytearrayreader.close();
            bytearrayreader = null;
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }

    public void saveProbMatrix(RandomAccessFile randomaccessfile) {
        try {
            randomaccessfile.writeInt(numPOS);
            for (int i = 0; i < numPOS; i++) {
                randomaccessfile.writeInt(posFreq[i]);
            }

            for (int j = 0; j < numPOS; j++) {
                for (int k = 0; k < numPOS; k++) {
                    randomaccessfile.writeInt(posBigram[j][k]);
                }

            }

        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }

    public void saveToFile(String s) {
        File file = new File(s);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile randomaccessfile = new RandomAccessFile(file, "rw");
            saveProbMatrix(randomaccessfile);
            randomaccessfile.close();
            randomaccessfile = null;
        } catch (IOException ioexception) {
            System.out.println((new StringBuilder("POSProbMatrix.saveToFile() error:")).append(ioexception.getMessage()).toString());
        }
    }
    private int numPOS;
    private int posFreq[];
    private int posBigram[][];
    private int corpusSize;
}