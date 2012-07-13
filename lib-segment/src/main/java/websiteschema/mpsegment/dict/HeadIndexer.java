package websiteschema.mpsegment.dict;

import websiteschema.mpsegment.util.BufReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import websiteschema.mpsegment.conf.Configure;

public class HeadIndexer {

//    public HeadIndexer(RandomAccessFile randomaccessfile, POSArray posArray)
//            throws IOException {
//        iWordItems = null;
//        load(randomaccessfile, posArray);
//    }

    public HeadIndexer(BufReader bufreader, POSArray posArray)
            throws IOException {
        iWordItems = null;
        load(bufreader, posArray);
    }

    public int getCapacity() {
        return iWordItems.length;
    }

    public int getWordOccuredSum() {
        return wordOccuredSum;
    }

    public int getWordCount() {
        return iWordItems.length;
    }

    public final int getMaxWordLength() {
        return maxWordLength;
    }

    public String getHeadWord() {
        return headWordString;
    }

    public IWord[] getWordItems() {
        return iWordItems;
    }

    @Override
    public String toString() {
        StringBuilder stringbuffer = new StringBuilder();
        stringbuffer.append((new StringBuilder("首字:")).append(getHeadWord()).append("(不同词数量:").append(getCapacity()).toString());
        stringbuffer.append((new StringBuilder(",最长词长度:")).append(getMaxWordLength()).toString());
        stringbuffer.append((new StringBuilder(",所有词的全部出现次数:")).append(getWordOccuredSum()).append(")\n").toString());
        for (int i = 0; i < iWordItems.length; i++) {
            IWord iworditem = iWordItems[i];
            stringbuffer.append(iworditem.toString());
        }

        return stringbuffer.toString();
    }

    public String toDBFString() {
        StringBuilder stringbuffer = new StringBuilder();
        for (int i = 0; i < iWordItems.length; i++) {
            IWord iworditem = iWordItems[i];
            stringbuffer.append(iworditem.toDBFString());
            stringbuffer.append("\n");
        }

        return stringbuffer.toString();
    }

    public final void load(BufReader bufreader, POSArray posArray)
            throws IOException {
        int headWordByte = bufreader.readIntByte();
        byte headWordBytes[] = new byte[headWordByte];
        bufreader.read(headWordBytes);
        headWordString = new String(headWordBytes, Configure.getInstance().getFileEncoding());
        int numWordItem = bufreader.readInt();
        maxWordLength = 0;
        wordOccuredSum = 0;
        iWordItems = new WordImpl[numWordItem];
        for (int k = 0; k < numWordItem; k++) {
            WordImpl worditem2 = new WordImpl(bufreader);
            wordOccuredSum += worditem2.getOccuredSum();
            if (maxWordLength < worditem2.getWordLength()) {
                maxWordLength = worditem2.getWordLength();
            }
            iWordItems[k] = worditem2;
            posArray.add(worditem2.getPOSArray());
        }

    }

    public String toText() {
        String ls = System.getProperty("line.separator");
        String space = " ";
        StringBuilder sb = new StringBuilder();
        sb.append("[headIndexer]").append(ls);
        sb.append(headWordString).append(space);
        sb.append(iWordItems.length).append(ls);
        for (int i = 0; i < iWordItems.length; i++) {
            sb.append(((WordImpl) iWordItems[i]).toText());
        }
        sb.append(ls);
        return sb.toString();
    }

    public void save(RandomAccessFile randomaccessfile)
            throws IOException {
        byte abyte0[] = headWordString.getBytes(Configure.getInstance().getFileEncoding());
        randomaccessfile.write((byte) abyte0.length);
        randomaccessfile.write(abyte0);
        randomaccessfile.writeInt(getCapacity());
        for (int i = 0; i < iWordItems.length; i++) {
            iWordItems[i].save(randomaccessfile);
        }

    }

    public IWord searchWord(String s) {
        int i = lookupWordItem(s);
        if (i >= 0) {
            return iWordItems[i];
        } else {
            return null;
        }
    }

    public IWord findWord(String s) {

        int j = -1;
        if (s.length() == 1) {
            if (s.equals(iWordItems[0].getWordName())) {
                return iWordItems[0];
            } else {
                return null;
            }
        }
        int j1 = getMaxWordLength();
        if (s.length() < j1) {
            j1 = s.length();
        }
        for (int k1 = 1; k1 < j1; k1++) {
            String s1 = s.substring(0, k1 + 1);
            int i = lookupWordItem(s1);
            if (i < 0) {
                continue;
            }

            j = i;
            if (j + 1 >= iWordItems.length || !iWordItems[j + 1].getWordName().startsWith(s1, 0)) {
                break;
            }
        }

        if (j >= 0) {
            return iWordItems[j];
        } else {
            return null;
        }
    }

    public IWord[] findMultiWord(String wordStr) {
        int firstWordIndex = -1;
        int foundWordCount = 0;
        int secondWordIndex = -1;
        int thirdWordIndex = -1;
        if (wordStr.length() == 1) {
            if (wordStr.equals(iWordItems[0].getWordName())) {
                IWord words[] = new IWord[1];
                words[0] = iWordItems[0];
                return words;
            } else {
                return null;
            }
        }
        int maxWordLen = getMaxWordLength();
        if (wordStr.length() < maxWordLen) {
            maxWordLen = wordStr.length();
        }
        for (int i = 1; i < maxWordLen; i++) {
            String candidateWord = wordStr.substring(0, i + 1);
            int wordIndex = lookupWordItem(candidateWord);
            if (wordIndex < 0) {
                continue;
            }
            if (++foundWordCount == 2) {
                secondWordIndex = firstWordIndex;
            } else if (foundWordCount == 3) {
                thirdWordIndex = firstWordIndex;
            }
            firstWordIndex = wordIndex;
            if (firstWordIndex + 1 >= iWordItems.length || !iWordItems[firstWordIndex + 1].getWordName().startsWith(candidateWord, 0)) {
                break;
            }
        }

        if (firstWordIndex >= 0) {
            IWord words[] = new IWord[foundWordCount];
            words[0] = iWordItems[firstWordIndex];
            if (secondWordIndex > 0) {
                words[1] = iWordItems[secondWordIndex];
            }
            if (thirdWordIndex > 0) {
                words[2] = iWordItems[thirdWordIndex];
            }
            return words;
        } else {
            return null;
        }
    }

    public IWord[] findWordStartWith(String s, int i) {
        int j = 0;
        int i1 = 0;
        int j1 = getMaxWordLength();
        if (s.length() < j1) {
            j1 = s.length();
        }
        j = lookupWordItem2(s);
        if (j < 0) {
            j = -j;
        }
        i = s.length() + i;
        if (j >= 0) {
            for (int k = j; k < iWordItems.length; k++) {
                String s1 = iWordItems[k].getWordName();
                if (!s1.startsWith(s, 0)) {
                    break;
                }
                if (s1.length() <= i) {
                    i1++;
                }
            }

            if (i1 >= 0) {
                IWord aiworditem[] = new IWord[i1];
                int l = j;
                i1 = 0;
                for (; l < iWordItems.length; l++) {
                    String s2 = iWordItems[l].getWordName();
                    if (!s2.startsWith(s, 0)) {
                        break;
                    }
                    if (s2.length() <= i) {
                        aiworditem[i1] = iWordItems[l];
                        i1++;
                    }
                }

                return aiworditem;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private int lookupWordItem(String s) {
        int k = 0;
        for (int l = iWordItems.length - 1; k <= l;) {
            int i = (k + l) / 2;
            int j = iWordItems[i].getWordName().compareTo(s);
            if (j == 0) {
                return i;
            }
            if (j < 0) {
                k = i + 1;
            } else {
                l = i - 1;
            }
        }

        return -1;
    }

    private int lookupWordItem2(String s) {
        int k = 0;
        for (int l = iWordItems.length - 1; k <= l;) {
            int i = (k + l) / 2;
            int j = iWordItems[i].getWordName().compareTo(s);
            if (j == 0) {
                return i;
            }
            if (j < 0) {
                k = i + 1;
            } else {
                l = i - 1;
            }
        }

        return -k;
    }
    private String headWordString;
    private int maxWordLength;
    private int wordOccuredSum;
    private IWord iWordItems[];
}
