package websiteschema.mpsegment.dict;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import websiteschema.mpsegment.conf.Configure;
import websiteschema.mpsegment.util.BufReader;

public class HeadIndexer {

    public HeadIndexer(BufReader bufreader, POSArray posArray)
            throws IOException {
        load(bufreader, posArray);
    }

    public HeadIndexer(IWord headWord) {
        this.headStr = headWord.getWordName().substring(0, 1);
        this.headWord = headWord;
        wordCount = 1;
        wordOccuredSum = 1;
        maxWordLength = headStr.length();
        wordArray = new BinaryWordArray(new IWord[]{this.headWord});
    }

    public HeadIndexer(IWord headWord, int headLength) {
        this.headStr = headWord.getWordName().substring(0, headLength);
        this.headWord = headWord;
        wordCount = 1;
        wordOccuredSum = 1;
        maxWordLength = headStr.length();
        wordArray = new BinaryWordArray(new IWord[]{this.headWord});
    }

    public int getWordOccuredSum() {
        return wordOccuredSum;
    }

    public int getWordCount() {
        return wordCount;
    }

    public final int getMaxWordLength() {
        return maxWordLength;
    }

    public String getHeadStr() {
        return headStr;
    }

    public void add(IWord word) {
        String wordName = word.getWordName();
        if(get(wordName) == null) {
            if(wordName.startsWith(headStr)) {
                if(wordName.length() > getMaxWordLength()) {
                    maxWordLength = wordName.length();
                }
                wordCount++;
                wordOccuredSum++;
                wordArray.add(word);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((new StringBuilder("首字:")).append(getHeadStr()).append("(不同词数量:").append(getWordCount()).toString());
        stringBuilder.append((new StringBuilder(",最长词长度:")).append(getMaxWordLength()).toString());
        stringBuilder.append((new StringBuilder(",所有词的全部出现次数:")).append(getWordOccuredSum()).append(")\n").toString());
        IWord[] wordItems = wordArray.getWordItems();
        for (int i = 0; i < wordCount; i++) {
            IWord word = wordItems[i];
            stringBuilder.append(word.toString());
        }

        return stringBuilder.toString();
    }

    public String toDBFString() {
        StringBuilder stringBuilder = new StringBuilder();
        IWord[] wordItems = wordArray.getWordItems();
        for (int i = 0; i < wordItems.length; i++) {
            IWord word = wordItems[i];
            stringBuilder.append(word.toDBFString());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public final void load(BufReader bufreader, POSArray posArray)
            throws IOException {
        int headWordByte = bufreader.readIntByte();
        byte headWordBytes[] = new byte[headWordByte];
        bufreader.read(headWordBytes);
        headStr = new String(headWordBytes, Configure.getInstance().getFileEncoding());
        wordCount = bufreader.readInt();
        maxWordLength = 0;
        wordOccuredSum = 0;
        IWord[] wordItems = new WordImpl[wordCount];
        for (int i = 0; i < wordCount; i++) {
            WordImpl word = new WordImpl(bufreader);
            wordOccuredSum += word.getOccuredSum();
            if (maxWordLength < word.getWordLength()) {
                maxWordLength = word.getWordLength();
            }
            wordItems[i] = word;
            if (i == 0) {
                headWord = word;
            }
            posArray.add(word.getPOSArray());
        }
        // TODO: 5711 HeadIndexers have words less than 64, which total number is 51426,
        //       232 HeadIndexers have words more than 64, which total number is 27261
        if (wordCount <= 64) {
            wordArray = new BinaryWordArray(wordItems);
        } else {
            wordArray = new HashWordArray(wordItems);
        }

    }

    public final void save(RandomAccessFile randomaccessfile)
            throws IOException {
        byte bytes[] = headStr.getBytes(Configure.getInstance().getFileEncoding());
        randomaccessfile.write((byte) bytes.length);
        randomaccessfile.write(bytes);
        randomaccessfile.writeInt(getWordCount());
        IWord[] wordItems = wordArray.getWordItems();
        for (int i = 0; i < wordItems.length; i++) {
            wordItems[i].save(randomaccessfile);
        }

    }

    public IWord get(String word) {
        return wordArray.find(word);
    }

    public IWord[] findMultiWord(String wordStr) {
        if (wordStr.length() == 1) {
            if (wordStr.equals(headWord.getWordName())) {
                IWord words[] = new IWord[1];
                words[0] = headWord;
                return words;
            } else {
                return null;
            }
        }
        int maxWordLen = getMaxWordLength();
        if (wordStr.length() < maxWordLen) {
            maxWordLen = wordStr.length();
        }
        LinkedList<IWord> array = new LinkedList<IWord>();
        for (int i = 1; i < maxWordLen; i++) {
            String candidateWord = wordStr.substring(0, i + 1);
            IWord word = wordArray.find(candidateWord);
            if (null == word) {
                continue;
            }
            array.addFirst(word);
            if (array.size() >= 3) {
                break;
            }
        }

        if (array.size() > 0) {
            return array.toArray(new IWord[0]);
        } else {
            return null;
        }
    }

    public IWord findWord(String wordStr) {
        if (wordStr.length() == 1) {
            if (wordStr.equals(headStr)) {
                return headWord;
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
            IWord word = get(candidateWord);
            if (null == word) {
                continue;
            }
            return word;
        }
        return null;
    }
    private String headStr;
    private int maxWordLength;
    private int wordOccuredSum;
    private int wordCount;
    private IWord headWord;
    private IWordArray wordArray;
}
