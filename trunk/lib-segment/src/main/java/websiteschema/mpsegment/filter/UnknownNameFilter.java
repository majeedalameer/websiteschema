/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.dict.POSUtil;
import websiteschema.mpsegment.conf.Configure;
import websiteschema.mpsegment.util.WordUtil;
import websiteschema.mpsegment.dict.ChNameDictionary;
import static websiteschema.mpsegment.util.WordUtil.*;

/**
 *
 * @author ray
 */
public class UnknownNameFilter extends AbstractSegmentFilter {

    private boolean useChNameDict = true;
    private boolean recognizeChineseName = Configure.getInstance().isChineseNameIdentify();
    private boolean xingMingSeparate = Configure.getInstance().isSegmentMin();
    private boolean useForeignNameDict = false;
    private static ChNameDictionary chNameDict = null;
    private static double factor1 = 1.1180000000000001D;
    private static double factor2 = 0.65000000000000002D;
    private static double factor3 = 1.6299999999999999D;
    private static int posNR = POSUtil.POS_NR;
    private static int posM = POSUtil.POS_M;
    private static ForeignName foreignName = null;

    public UnknownNameFilter() {
        initialize();
    }

    private synchronized void initialize() {
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

    @Override
    public void doFilter() {
        if (useChNameDict && recognizeChineseName) {
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
    }

    private int processNRWordItems(int i1, int j1) {
        int numNRWordItem = getNumNRWordItem(i1, j1);
        if (numNRWordItem > 0 && i1 > 0 && segmentResult.getPOS(i1 - 1) == posM) {
            numNRWordItem = 0;
        }
        if (numNRWordItem > 0) {
            if (xingMingSeparate) {
                if (numNRWordItem >= 3) {
                    mergeWordsWithPOS(i1 + 1, i1 + 2, posNR);
                }
            } else if (numNRWordItem >= 2) {
                mergeWordsWithPOS(i1, (i1 + numNRWordItem) - 1, posNR);
            }
        } else if (useForeignNameDict) {
            numNRWordItem = processForeignName(i1, j1);
        }
        return numNRWordItem;
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

    private double getRightBoundaryWordLP(String s1) {
        double d1 = 1.0D + chNameDict.getRightBoundaryWordLP(s1);
        return d1;
    }

    private boolean isSpecialMingChar(int wordIndex) {
        String word = segmentResult.getWord(wordIndex);
        return WordUtil.isSpecialMingChar(word);
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
}
