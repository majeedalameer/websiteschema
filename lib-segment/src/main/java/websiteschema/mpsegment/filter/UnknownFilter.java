package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.conf.Configure;
import websiteschema.mpsegment.dict.POSUtil;
import websiteschema.mpsegment.dict.ChNameDictionary;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.util.ChNumberConverter;

public final class UnknownFilter
        implements ISegmentFilter {

    public UnknownFilter() {

        supportQuerySyntax = false;
        maxQueryLength = 512;
        useChNameDict = true;
        chineseNameIdentify = true;
        useForeignNameDict = false;
        segmentMin = true;
        ShiJian = true;
        DiMing = true;
        xingMingSeparate = true;
        segmentMin = Configure.getInstance().isSegmentMin();
        if (segmentMin) {
            ShiJian = false;
            DiMing = false;
            xingMingSeparate = true;
        }
        supportQuerySyntax = Configure.getInstance().isSupportQuerySyntax();
        chineseNameIdentify = Configure.getInstance().isChineseNameIdentify();
        xingMingSeparate = Configure.getInstance().isXingMingSeparate();
        glueChar = Configure.getInstance().getGlueChar();
        maxQueryLength = Configure.getInstance().getMaxQueryLength();
        if (useChNameDict) {
            if (chNameDict == null) {
                chNameDict = new ChNameDictionary();
                chNameDict.loadNameDict(Configure.getInstance().getChNameDict());
            }
            if (useForeignNameDict && foreignName == null) {
                foreignName = new ForeignName();
                foreignName.loadNameWord();
            }
        }
    }

    public void setQuerySyntax(boolean flag) {
        supportQuerySyntax = flag;
    }

    @Override
    public void filter(SegmentResult segmentResult) {
        this.segmentResult = segmentResult;
        wordPosIndexes = new int[segmentResult.length()];

        if (DiMing) {
            processNS();
        }
        processM();
        if (useChNameDict && chineseNameIdentify) {
            processChineseName();
        }
        if (supportQuerySyntax && segmentResult.length() <= maxQueryLength) {
            processQuerySyntax();
        }
        consolidateWordAtomsByPos();
    }

    private boolean isNumberSeparator(int i1) {
        boolean flag = false;
        int length = segmentResult.length();
        String s1 = segmentResult.getWord(i1);
        if (s1.length() == 1) {
            if (s1.equals(".") || s1.equals("．")) {
                if (i1 + 1 < length && (segmentResult.getPOS(i1 + 1) == posM || segmentResult.getPOS(i1 + 1) == posUN) && isNumerical(segmentResult.getWord(i1 + 1)) == 1) {
                    flag = true;
                }
            } else if (s1.equals("点") || s1.equals("/") || s1.equals("／")) {
                if (i1 + 1 < length && (segmentResult.getPOS(i1 + 1) == posM || segmentResult.getPOS(i1 + 1) == posUN)) {
                    flag = true;
                }
            } else if (i1 + 1 < length) {
                if (s1.equals("%") || s1.equals("％") || s1.equals("‰")) {
                    flag = true;
                }
            } else {
                flag = false;
                if (s1.equals("%") || s1.equals("％")) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    private boolean isChineseNumber(int i1) {
        boolean flag = false;
        if (segmentResult.getPOS(i1) == posM) {
            if (segmentResult.getWord(i1).length() == 1) {
                if (!segmentResult.getWord(i1).equals("多") && !segmentResult.getWord(i1).equals("余")) {
                    flag = true;
                }
            } else if (segmentResult.getWord(i1).indexOf("分之") >= 0) {
                flag = true;
            } else if (isNumerical(segmentResult.getWord(i1)) == 1 || ChNumberConverter.isChineseDigitalStr(segmentResult.getWord(i1))) {
                flag = true;
            } else if (segmentResult.getWord(i1).equals("几十") || segmentResult.getWord(i1).equals("几百") || segmentResult.getWord(i1).equals("几千") || segmentResult.getWord(i1).equals("几亿") || segmentResult.getWord(i1).equals("几万") || segmentResult.getWord(i1).equals("千万") || segmentResult.getWord(i1).equals("百万") || segmentResult.getWord(i1).equals("上百") || segmentResult.getWord(i1).equals("上千") || segmentResult.getWord(i1).equals("数十") || segmentResult.getWord(i1).equals("数百") || segmentResult.getWord(i1).equals("数千") || segmentResult.getWord(i1).equals("好几十") || segmentResult.getWord(i1).equals("好几百") || segmentResult.getWord(i1).equals("好几千") || segmentResult.getWord(i1).equals("一个")) {
                flag = true;
            }
        }
        return flag;
    }

    private void processM() {
        int length = segmentResult.length();

        for (int wordI = 0; wordI < length; wordI++) {
            if (segmentResult.getPOS(wordI) == posUN) {
                int j1 = isNumerical(segmentResult.getWord(wordI));
                if (j1 == 1) {
                    segmentResult.setPOS(wordI, posM);
                }
            }
            if (segmentResult.getPOS(wordI) == posNR && wuLiuWan.indexOf(segmentResult.getWord(wordI)) >= 0) {
                if (wordI > 0) {
                    if (segmentResult.getPOS(wordI - 1) == posM || wuLiuWan.indexOf(segmentResult.getWord(wordI - 1)) >= 0) {
                        segmentResult.setPOS(wordI, posM);
                    } else if (wordI + 1 < length && (segmentResult.getPOS(wordI + 1) == posM || wuLiuWan.indexOf(segmentResult.getWord(wordI + 1)) >= 0)) {
                        segmentResult.setPOS(wordI, posM);
                    }
                } else if (length > 1 && (segmentResult.getPOS(wordI + 1) == posM || wuLiuWan.indexOf(segmentResult.getWord(wordI + 1)) >= 0)) {
                    segmentResult.setPOS(wordI, posM);
                }
            }
        }

        numBegin = -1;
        numEnd = -1;
        boolean isChineseNum = false;
        for (int k2 = 0; k2 < length; k2++) {
            if (wordPosIndexes[k2] <= 0) {
                if (segmentResult.getPOS(k2) == posUN) {
                    if (segmentResult.getWord(k2).equals("-") || segmentResult.getWord(k2).equals("－")) {
                        segmentResult.setPOS(k2, posM);
                    } else {
                        int l1 = isNumerical(segmentResult.getWord(k2));
                        if (l1 == 1) {
                            segmentResult.setPOS(k2, posM);
                        }
                    }
                }
                if (isChineseNumber(k2)) {
                    if (numBegin < 0) {
                        numBegin = k2;
                        isChineseNum = true;
                    } else {
                        numEnd = k2;
                    }
                } else if (isChineseNum) {
                    if (!isNumberSeparator(k2)) {
                        isChineseNum = false;
                        if (numEnd - numBegin >= 1) {
                            modifyWordPosIndexes(numBegin, numEnd, posM);
                        }
                        numBegin = -1;
                        numEnd = -1;
                    } else {
                        numEnd = k2;
                        if (k2 + 1 == length && numEnd - numBegin >= 1) {
                            modifyWordPosIndexes(numBegin, numEnd, posM);
                        }
                    }
                }
                if (ShiJian && segmentResult.getWord(k2).length() == 1 && k2 > 0 && (segmentResult.getWord(k2).equals("年") || segmentResult.getWord(k2).equals("月") || segmentResult.getWord(k2).equals("日") || segmentResult.getWord(k2).equals("时") || segmentResult.getWord(k2).equals("分") || segmentResult.getWord(k2).equals("秒"))) {
                    if (segmentResult.getWord(k2).equals("年")) {
                        if (segmentResult.getPOS(k2 - 1) == posM && (segmentResult.getWord(k2 - 1).length() > 2 || wordPosIndexes[k2 - 1] > 0) && (wordPosIndexes[k2 - 1] <= 0 || segmentResult.getWord(k2 - 1).length() != 1 || k2 - 3 > 0 && (k2 - 3 < 0 || wordPosIndexes[k2 - 3] > 0))) {
                            modifyWordPosIndexes(k2 - 1, k2, posT);
                        }
                    } else if (segmentResult.getPOS(k2 - 1) == posM) {
                        modifyWordPosIndexes(k2 - 1, k2, posT);
                    }
                }
            }
        }

        if (numEnd > 0 && numEnd == length - 1 && numBegin >= 0 && numEnd - numBegin >= 1) {
            modifyWordPosIndexes(numBegin, numEnd, posM);
        }

    }

    private void processNS() {
        int length = segmentResult.length();
        for (int wordI = 0; wordI < length; wordI++) {
            if (wordPosIndexes[wordI] <= 0) {
                int pos = segmentResult.getPOS(wordI);
                if (pos == posNS && wordI + 1 < length) {
                    String word = segmentResult.getWord(wordI);
                    if (segmentResult.getWord(wordI + 1).length() == 1 && adminLevels.indexOf(segmentResult.getWord(wordI + 1)) > 0 && word.lastIndexOf(segmentResult.getWord(wordI + 1)) != length - 1) {
                        modifyWordPosIndexes(wordI, wordI + 1, posNS);
                    }
                }
            }
        }

    }

    private int isNumerical(String s1) {
        byte byte0 = 1;
        if (s1.length() <= 0) {
            byte0 = 2;
        } else {
            for (int i1 = 0; i1 < s1.length(); i1++) {
                if (Character.isDigit(s1.charAt(i1))) {
                    continue;
                }
                byte0 = 2;
                break;
            }

        }
        return byte0;
    }

    public static boolean isCharaterOrDigit(char c1) {
        return c1 >= 'a' && c1 <= 'z' || c1 >= 'A' && c1 <= 'Z' || c1 >= '0' && c1 <= '9';
    }

    private boolean isAlphaNumericWithUnderScore(String s1) {
        boolean flag = true;
        for (int i1 = 0; i1 < s1.length(); i1++) {
            char c1 = s1.charAt(i1);
            if (isCharaterOrDigit(c1) || c1 == '_') {
                continue;
            }
            flag = false;
            break;
        }

        return flag;
    }

    private boolean isAlphaNumericWithUnderScore_Slash_Colon(String s1) {
        boolean flag = true;
        for (int i1 = 0; i1 < s1.length(); i1++) {
            char c1 = s1.charAt(i1);
            if (isCharaterOrDigit(c1) || c1 == '_' || c1 == '/' || c1 == ':') {
                continue;
            }
            flag = false;
            break;
        }

        return flag;
    }

    private boolean isLetterOrDigitWithUnderscore(String s1) {
        boolean flag = true;
        for (int i1 = 0; i1 < s1.length(); i1++) {
            char c1 = s1.charAt(i1);
            if (Character.isLetterOrDigit(c1) || c1 == '_') {
                continue;
            }
            flag = false;
            break;
        }

        return flag;
    }

    private boolean isPosP_C_U_W_UN(int i1) {
        return i1 == POSUtil.POS_P || i1 == POSUtil.POS_C || i1 == POSUtil.POS_U || i1 == POSUtil.POS_W || i1 == POSUtil.POS_UNKOWN;
    }

    private boolean isChineseJieCi(String s1) {
        return s1.equals("向") || s1.equals("和") || s1.equals("丁") || s1.equals("自") || s1.equals("若") || s1.equals("于") || s1.equals("同") || s1.equals("为") || s1.equals("以") || s1.equals("连") || s1.equals("从") || s1.equals("得") || s1.equals("则");
    }

    private void processQuerySyntax() {
        int length = segmentResult.length();
        int wordI = 0;

        for (; wordI < length; wordI++) {
            if (wordPosIndexes[wordI] <= 0) {
                int pos = segmentResult.getPOS(wordI);
                if (pos == posUN || pos == posW) {
                    String s1 = segmentResult.getWord(wordI);
                    if (s1.length() == 1 && !isLeftOrRightBraceOrColonOrSlash(s1)) {
                        if (s1.equals(leftBrace)) {
                            if (wordI + 2 < length && rightBrace.equals(segmentResult.getWord(wordI + 2))) {
                                int k1 = isNumerical(segmentResult.getWord(wordI + 1));
                                if (k1 == 1) {
                                    int i2;
                                    if (wordI >= 1) {
                                        i2 = wordI - 1;
                                        if (segmentResult.getWord(i2).equals(tilda)) {
                                            i2--;
                                        }
                                    } else {
                                        i2 = wordI;
                                    }
                                    int l2 = wordI + 2;
                                    modifyWordPosIndexesWithPosUN(i2, l2);
                                }
                            }
                        } else if (s1.equals(questionMark) || s1.equals(star)) {
                            if (wordI >= 1) {
                                int k3 = wordI;
                                int j4 = wordI;
                                if (wordI + 1 < length) {
                                    if (isLetterOrDigitWithUnderscore(segmentResult.getWord(wordI + 1))) {
                                        j4 = wordI + 1;
                                    }
                                    if (isLetterOrDigitWithUnderscore(segmentResult.getWord(wordI - 1))) {
                                        k3 = wordI - 1;
                                    }
                                    modifyWordPosIndexesWithPosUN(k3, j4);
                                } else if (isLetterOrDigitWithUnderscore(segmentResult.getWord(wordI - 1))) {
                                    modifyWordPosIndexesWithPosUN(wordI - 1, wordI);
                                }
                            } else if (wordI + 1 < length && isLetterOrDigitWithUnderscore(segmentResult.getWord(wordI + 1))) {
                                modifyWordPosIndexesWithPosUN(wordI, wordI + 1);
                            }
                        } else if (s1.equals(colon)) {
                            if (wordI > 0 && wordI + 1 < length && isAlphaNumericWithUnderScore(segmentResult.getWord(wordI + 1))) {
                                modifyWordPosIndexesWithPosUN(wordI - 1, wordI + 1);
                            }
                        } else if (s1.equals(underScore)) {
                            int j2;
                            if (wordI > 1 && isAlphaNumericWithUnderScore(segmentResult.getWord(wordI - 1))) {
                                j2 = wordI - 1;
                            } else {
                                j2 = wordI;
                            }
                            int i3 = wordI;
                            for (int l3 = wordI + 1; l3 < length; l3++) {
                                if (!isAlphaNumericWithUnderScore(segmentResult.getWord(l3))) {
                                    break;
                                }
                                i3 = l3;
                            }

                            if (i3 > j2) {
                                modifyWordPosIndexesWithPosUN(j2, i3);
                            }
                        } else if (s1.equals(slash)) {
                            int k2;
                            if (wordI > 1 && isAlphaNumericWithUnderScore_Slash_Colon(segmentResult.getWord(wordI - 1))) {
                                k2 = wordI - 1;
                            } else {
                                k2 = wordI;
                            }
                            int j3 = wordI;
                            for (int i4 = wordI + 1; i4 < length; i4++) {
                                if (!isAlphaNumericWithUnderScore_Slash_Colon(segmentResult.getWord(i4))) {
                                    break;
                                }
                                j3 = i4;
                            }

                            if (j3 > k2) {
                                modifyWordPosIndexesWithPosUN(k2, j3);
                            }
                        } else if (s1.equals(tilda) && wordI + 1 < length && isLetterOrDigitWithUnderscore(segmentResult.getWord(wordI + 1))) {
                            modifyWordPosIndexesWithPosUN(wordI, wordI + 1);
                        }
                    }
                }
            }
        }

    }

    private boolean isLeftOrRightBraceOrColonOrSlash(String s1) {  //   ?[]/
        boolean flag = false;
        if (glueChars.indexOf(s1) >= 0 && glueChar.indexOf(s1) < 0) {
            flag = true;
        }
        return flag;
    }

    private void processChineseName() {
        int length = segmentResult.length();
        int j1 = 0;
        int k1 = -1;
        int l1 = -1;
        boolean flag = false;

        for (; j1 < length; j1++) {
            if (wordPosIndexes[j1] > 0) {
                continue;
            }
            if (flag && (l1 - k1 >= 5 || j1 + 1 == length)) {
                if (j1 + 1 == length) {
                    l1 = j1;
                }
                int i2 = processNRWordItems(k1, l1);
                if (i2 >= 2) {
                    j1 = k1 + i2;
                } else {
                    j1 = k1 + 1;
                }
                flag = false;
                k1 = -1;
                l1 = -1;
                if (j1 + 1 > length) {
                    break;
                }
            }
            if (segmentResult.getPOS(j1) == 27) {
                if (k1 < 0) {
                    k1 = j1;
                    flag = true;
                } else {
                    l1 = j1;
                }
            } else if (flag) {
                if (isPosP_C_U_W_UN(segmentResult.getPOS(j1)) && !isChineseJieCi(segmentResult.getWord(j1)) || segmentResult.getWord(j1).length() > 2) {
                    flag = false;
                    if (l1 - k1 >= 1) {
                        int j2 = processNRWordItems(k1, l1);
                        if (j2 >= 2) {
                            j1 = (k1 + j2) - 1;
                        } else {
                            j1 = k1;
                        }
                    }
                    k1 = -1;
                    l1 = -1;
                } else {
                    l1 = j1;
                    int k2;
                    if (j1 + 1 == length && l1 - k1 >= 1) {
                        k2 = processNRWordItems(k1, l1);
                    }
                }
            } else if (segmentResult.getWord(j1).length() == 1 && !isPosP_C_U_W_UN(segmentResult.getPOS(j1)) || isChineseJieCi(segmentResult.getWord(j1))) {
                if (k1 < 0) {
                    k1 = j1;
                    flag = true;
                } else {
                    l1 = j1;
                }
            }
        }

    }

    private int processNRWordItems(int i1, int j1) {
        int numNRWordItem = getNumNRWordItem(i1, j1);
        if (numNRWordItem > 0 && i1 > 0 && segmentResult.getPOS(i1 - 1) == posM) {
            numNRWordItem = 0;
        }
        if (numNRWordItem > 0) {
            if (xingMingSeparate) {
                if (numNRWordItem >= 3) {
                    modifyWordPosIndexes(i1 + 1, i1 + 2, posNR);
                }
            } else if (numNRWordItem >= 2) {
                modifyWordPosIndexes(i1, (i1 + numNRWordItem) - 1, posNR);
            }
        } else if (useForeignNameDict) {
            numNRWordItem = processForeignName(i1, j1);
        }
        return numNRWordItem;
    }

    private int processForeignName(int i1, int j1) {

        int l1 = -1;
        //System.out.println(" / ");
        for (int i2 = i1; i2 <= j1; i2++) {
            if (!foreignName.isForiegnName(segmentResult.getWord(i2))) {
                break;
            }
            l1 = i2;
            //System.out.print((new StringBuilder(String.valueOf(wordAtoms.getWords(i2)))).append(" ").toString());
        }

        if (l1 > i1 + 1) {
            //  System.out.println("=======");
        }
        return l1;
    }

    private void setPosUN(int i1, int j1) {
        int k1 = segmentResult.length();
        if (i1 < 0 || j1 >= k1 || i1 >= j1) {
            return;
        }
        String s1 = segmentResult.getWord(i1);
        for (int l1 = i1 + 1; l1 <= j1; l1++) {
            s1 = (new StringBuilder(String.valueOf(s1))).append(segmentResult.getWord(l1)).toString();
        }

        segmentResult.setWord(i1, s1, posUN);
        for (int i2 = i1 + 1; i2 < k1 - (j1 - i1); i2++) {
            segmentResult.letWord1EqualWord2(i2, i2 + (j1 - i1));
        }

        for (int j2 = k1 - (j1 - i1); j2 < k1; j2++) {
            segmentResult.setWord(j2, "", posUN);
        }

    }

    /*   private void a(int i1, int j1, int k1) {
    int l1 = wordAtoms.length();
    if (i1 < 0 || j1 >= l1 || i1 >= j1) {
    return;
    }
    String s1 = wordAtoms.getWords(i1);
    for (int i2 = i1 + 1; i2 <= j1; i2++) {
    s1 = (new StringBuilder(String.valueOf(s1))).append(wordAtoms.getWords(i2)).toString();
    }

    wordAtoms.set1Word(i1, s1, k1);
    for (int j2 = i1 + 1; j2 < l1 - (j1 - i1); j2++) {
    wordAtoms.set1From2(j2, j2 + (j1 - i1));
    }

    for (int k2 = l1 - (j1 - i1); k2 < l1; k2++) {
    wordAtoms.set1Word(k2, "", posUN);
    }

    }


    private final int d(int i1) {
    if (i1 > 200) {
    return i1 - 200;
    } else {
    return i1;
    }
    }
     */
    private void modifyWordPosIndexesWithPosUN(int i1, int j1) {
        modifyWordPosIndexes(i1, j1, posUN);
    }

    private void modifyWordPosIndexes(int wordI1, int wordI2, int pos) {
        int length = segmentResult.length();
        int posIndex = pos;
        if (wordI1 < 0 || wordI2 >= length || wordI1 >= wordI2) {
            return;
        }
        if (wordPosIndexes[wordI1] > 0) {
            int posI1 = wordPosIndexes[wordI1];
            for (wordI1--; wordI1 >= 0 && wordPosIndexes[wordI1] == posI1; wordI1--);
            wordI1++;
        }
        if (wordI1 >= 1 && wordPosIndexes[wordI1 - 1] > 0 && wordPosIndexes[wordI1 - 1] < 200) {
            posIndex += 200;
        }
        for (int k2 = wordI1; k2 <= wordI2; k2++) {
            wordPosIndexes[k2] = posIndex;
        }

    }

    private void consolidateWordAtomsByPos() {
        int length = segmentResult.length();
        int i1 = 0;
        int j1 = 0;

        while (i1 < length) {
            if (wordPosIndexes[i1] == 0) {
                if (i1 != j1) {
                    segmentResult.letWord1EqualWord2(j1, i1);
                }
                i1++;
                j1++;
            } else {
                int k1 = wordPosIndexes[i1];
                numBegin = i1;
                String s1 = segmentResult.getWord(numBegin);
                for (i1++; i1 < length && wordPosIndexes[i1] == k1; i1++) {
                    s1 = (new StringBuilder(String.valueOf(s1))).append(segmentResult.getWord(i1)).toString();
                }

                if (k1 >= 200) {
                    k1 -= 200;
                }
                segmentResult.setWord(j1, s1, k1);
                j1++;
            }
        }
        for (int i2 = j1; i2 < length; i2++) {
            segmentResult.setWord(i2, "", posUN);
        }
        segmentResult.cutTail(j1);
    }

    private boolean isSpecialMingChar(int i1) {
        String s1 = segmentResult.getWord(i1);
        return isSpecialMingChar(s1);
    }

    private boolean isSpecialMingChar(String s1) {
        return s1.equals("向") || s1.equals("自") || s1.equals("乃") || s1.equals("以") || s1.equals("从") || s1.equals("和") || s1.equals("得") || s1.equals("为") || s1.equals("则") || s1.equals("如");
    }

    private boolean processSpecialMingChar(int i1) {
        String s1 = segmentResult.getWord(i1);
        if (s1.equals("以") || s1.equals("从")) {
            double d1 = chNameDict.computeLgMing23(segmentResult.getWord(i1), segmentResult.getWord(i1 + 1));
            if (d1 < 0.92000000000000004D) {
                return true;
            }
        } else if (s1.equals("得") || s1.equals("为") || s1.equals("向") || s1.equals("自")) {
            double d2 = chNameDict.computeLgMing23(segmentResult.getWord(i1), segmentResult.getWord(i1 + 1));
            if (d2 <= 0.93000000000000005D) {
                return true;
            }
        } else if (s1.equals("则")) {
            double d3 = chNameDict.computeLgMing23(segmentResult.getWord(i1), segmentResult.getWord(i1 + 1));
            if (d3 <= 0.80000000000000004D) {
                return true;
            }
        } else if (s1.equals("如")) {
            double d4 = chNameDict.computeLgMing23(segmentResult.getWord(i1), segmentResult.getWord(i1 + 1));
            if (d4 <= 1.0D) {
                return true;
            }
        }
        return false;
    }

    private int getNumNRWordItem(int begin, int end) {
        int gap = (end - begin) + 1;
        int l1 = -1;
        if (segmentResult.getWord(begin).length() > 2 || segmentResult.getWord(begin + 1).length() > 2) {
            return l1;
        }
        if (segmentResult.getWord(begin + 1).length() == 1) {
            if (gap >= 3) {
                double d1 = chNameDict.computeLgLP2(segmentResult.getWord(begin), segmentResult.getWord(begin + 1));
                if (d1 <= 0.0D && segmentResult.getWord(begin).length() == 2) {
                    return getNumNR(begin);
                }
                double d4 = chNameDict.computeLgLP3(segmentResult.getWord(begin), segmentResult.getWord(begin + 1), segmentResult.getWord(begin + 2));
                if (segmentResult.getWord(begin + 2).length() > 1) {
                    d4 = 0.0D;
                }
                if (d1 > 0.95999999999999996D) {
                    d1 *= getRightBoundaryWordLP(segmentResult.getWord(begin + 2));
                }
                if (d4 > d1 && d4 > factor1) {
                    l1 = 3;
                    if (isSpecialMingChar(begin + 2)) {
                        double d5 = chNameDict.computeLgMing23(segmentResult.getWord(begin + 1), segmentResult.getWord(begin + 2));
                        l1 = 2;
                        if (d5 > 1.1339999999999999D || d5 > 0.90000000000000002D && d4 > 1.6000000000000001D && d4 / d1 > 2D) {
                            l1 = 3;
                        }
                    } else if (processSpecialMingChar(begin + 1)) {
                        l1 = -1;
                    }
                } else if (d1 > d4 && d1 > factor2) {
                    l1 = 2;
                    if (isSpecialMingChar(begin + 1)) {
                        l1 = -1;
                    }
                }
            } else {
                double d2 = chNameDict.computeLgLP2(segmentResult.getWord(begin), segmentResult.getWord(begin + 1));
                if (d2 > factor2) {
                    l1 = 2;
                    if (isSpecialMingChar(begin + 1)) {
                        l1 = -1;
                    }
                } else if (d2 <= 0.0D && segmentResult.getWord(begin).length() == 2) {
                    l1 = getNumNR(begin);
                }
            }
        } else if (segmentResult.getWord(begin + 1).length() == 2) {
            double d3 = chNameDict.computeLgLP3(segmentResult.getWord(begin), segmentResult.getWord(begin + 1).substring(0, 1), segmentResult.getWord(begin + 1).substring(1, 2));
            if (d3 > factor3) {
                l1 = 2;
            }
        }
        return l1;
    }

    private double getRightBoundaryWordLP(String s1) {
        double d1 = 1.0D + chNameDict.getRightBoundaryWordLP(s1);
        return d1;
    }

    private int getNumNR(int i1) {
        byte byte0 = -1;
        String s1 = segmentResult.getWord(i1).substring(0, 1);
        String s2 = segmentResult.getWord(i1).substring(1, 2);
        String s3 = segmentResult.getWord(i1 + 1);
        double d2 = chNameDict.computeLgLP3_2(segmentResult.getWord(i1), segmentResult.getWord(i1 + 1));
        double d1 = chNameDict.computeLgLP2(s1, s2);
        if (d1 > 0.95999999999999996D) {
            d1 *= getRightBoundaryWordLP(s3);
        }
        if (d2 > d1 && d2 > factor1) {
            byte0 = 2;
            if (isSpecialMingChar(i1 + 1)) {
                double d3 = chNameDict.computeLgMing23(s2, s3);
                byte0 = -1;
                if (d3 > 1.1339999999999999D || d3 > 0.90000000000000002D && d2 > 1.6000000000000001D && d2 / d1 > 2D) {
                    byte0 = 2;
                }
            }
        }
        return byte0;
    }
    private static int posM = 5;
    private static int posUN = 44;
    private static int posW = 26;
    private static int posT = 2;
    private static int posNS = 28;
    private static int posNR = 27;
    private static String glueChars = "*?~/_[]:";
    private static String leftBrace = "[";
    private static String rightBrace = "]";
    private static String questionMark = "?";
    private static String star = "*";
    private static String tilda = "~";
    private static String underScore = "_";
    private static String slash = "/";
    private static String colon = ":";
    private String glueChar = "~_:";
    private int numBegin;
    private int numEnd;
    private boolean supportQuerySyntax;
    private int maxQueryLength;
    private boolean useChNameDict;
    private boolean chineseNameIdentify;
    private boolean useForeignNameDict;
    private boolean segmentMin;
    private boolean ShiJian;
    private boolean DiMing;
    private boolean xingMingSeparate;
    private static double factor1 = 1.1180000000000001D;
    private static double factor2 = 0.65000000000000002D;
    private static double factor3 = 1.6299999999999999D;
    private static String wuLiuWan = "伍陆万";
    private static String adminLevels = " 省市县区乡镇村旗州";
    private ChNameDictionary chNameDict = null;
    private static ForeignName foreignName = null;
    private int wordPosIndexes[];
    private SegmentResult segmentResult;
}
