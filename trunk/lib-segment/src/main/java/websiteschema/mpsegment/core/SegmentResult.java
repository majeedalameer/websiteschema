package websiteschema.mpsegment.core;

import websiteschema.mpsegment.dict.POSUtil;

public class SegmentResult {

    public SegmentResult() {
    }

    public SegmentResult(int size) {
        words = new String[size];
        posArray = new int[size];
        concepts = new int[size];
    }

    public void setWords(String words[]) {
        this.words = words;
    }

    public void setPOSArray(int tags[]) {
        this.posArray = tags;
    }

    public void setConcepts(int marks[]) {
        this.concepts = marks;
    }

    public String[] getWords() {
        return words;
    }

    public int[] getPOSArray() {
        return posArray;
    }

    public int[] getConcepts() {
        return concepts;
    }

    public String getWord(int i) {
        return words[i];
    }

    public int getWordIndexInOriginalString(int index) {
        int wordIndexInOriginalString = 0;
        for(int i = 0; i < index; i++) {
            wordIndexInOriginalString += words[i].length();
        }
        return wordIndexInOriginalString;
    }

    public int getPOS(int i) {
        return posArray[i];
    }

    public int getConcept(int i) {
        return concepts[i];
    }

    public int getIndex(String word) {
        int index = -1;
        for (int j = 0; j < words.length; j++) {
            if (!words[j].equals(word)) {
                continue;
            }
            index = j;
            break;
        }

        return index;
    }

    public int length() {
        if (words != null) {
            return words.length;
        } else {
            return 0;
        }
    }

    private int trimLength() {
        if (words != null) {
            int i = words.length;
            if (i > 1 && words[i - 1].length() <= 0) {
                int j;
                for (j = i - 1; j > 1; j--) {
                    if (words[j].length() > 0) {
                        break;
                    }
                }
                i = j + 1;
            }
            return i;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder retString = new StringBuilder();
        int length = trimLength();
        for (int j = 0; j < length; j++) {
            retString.append(words[j]).append("/").append(POSUtil.getPOSString(posArray[j])).append(" ");
        }

        return retString.toString();
    }

    public String toOriginalString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < length(); i ++) {
            stringBuilder.append(getWord(i));
        }
        return stringBuilder.toString();
    }

    public void setWord(int index, String word, int tag) {
        words[index] = word;
        posArray[index] = tag;
        concepts[index] = 0;
    }

    public void setPOS(int index, int pos) {
        posArray[index] = pos;
    }

    public void letWord1EqualWord2(int wordIndex1, int wordIndex2) {
        words[wordIndex1] = words[wordIndex2];
        posArray[wordIndex1] = posArray[wordIndex2];
        concepts[wordIndex1] = concepts[wordIndex2];
    }

    public void cutTail(int end) {
        int len = end;
        String arrayWord[] = new String[len];
        int arrayPOS[] = new int[len];
        int arrayConcept[] = new int[len];
        for (int k = 0; k < len; k++) {
            arrayWord[k] = getWord(k);
            if (posArray != null) {
                arrayPOS[k] = getPOS(k);
            } else {
                arrayPOS[k] = 0;
            }
            arrayConcept[k] = getConcept(k);
        }
        this.words = arrayWord;
        this.posArray = arrayPOS;
        this.concepts = arrayConcept;
    }

    public void append(SegmentResult segmentResult) {
        int length = length();
        int total = length + segmentResult.length();
        String arrayWord[] = new String[total];
        int arrayPOS[] = new int[total];
        int arrayConcept[] = new int[total];
        for (int i = 0; i < length; i++) {
            arrayWord[i] = getWord(i);
            if (posArray != null) {
                arrayPOS[i] = getPOS(i);
            } else {
                arrayPOS[i] = 0;
            }
            arrayConcept[i] = getConcept(i);
        }

        for (int i = length; i < total; i++) {
            arrayWord[i] = segmentResult.getWord(i - length);
            if (segmentResult.getPOSArray() != null) {
                arrayPOS[i] = segmentResult.getPOS(i - length);
            } else {
                arrayPOS[i] = 0;
            }
            arrayConcept[i] = segmentResult.getConcept(i - length);
        }

        words = arrayWord;
        posArray = arrayPOS;
        concepts = arrayConcept;
    }
    private String words[];
    private int posArray[];
    private int concepts[];
}
