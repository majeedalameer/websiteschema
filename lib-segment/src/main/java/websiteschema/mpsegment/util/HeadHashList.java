package websiteschema.mpsegment.util;

import websiteschema.mpsegment.dict.IWord;
import java.util.ArrayList;
import java.util.HashMap;

public class HeadHashList {

    public HeadHashList() {
        headIndexHashMap = new HashMap<String, Integer>();
    }

    public void buildHashList(IWord aiworditem[]) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new Integer(-1));
        String s1 = "\002";
        for (int i = 0; i < aiworditem.length; i++) {
            String s = aiworditem[i].getWordName().substring(0, 1);
            if (!s.equals(s1)) {
                arrayList.add(new Integer(i));
                headIndexHashMap.put(s, arrayList.size() - 1);
                s1 = s;
            }
        }

        arrayList.add(new Integer(aiworditem.length));
        headIndexArray = new int[arrayList.size()];
        int j = arrayList.size();
        for (int k = 0; k < j; k++) {
            headIndexArray[k] = ((Integer) arrayList.get(k)).intValue();
        }

    }

    public void buildHashList(String as[]) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new Integer(-1));
        String s1 = "\002";
        for (int i = 0; i < as.length; i++) {
            String s = as[i].substring(0, 1);
            if (!s.equals(s1)) {
                arrayList.add(new Integer(i));
                headIndexHashMap.put(s, arrayList.size() - 1);
                s1 = s;
            }
        }

        arrayList.add(new Integer(as.length));
        headIndexArray = new int[arrayList.size()];
        int j = arrayList.size();
        for (int k = 0; k < j; k++) {
            headIndexArray[k] = ((Integer) arrayList.get(k)).intValue();
        }

    }

    public int getFirstPos(String s) {
        int i = headIndexHashMap.get(s);
        if (i > 0) {
            return headIndexArray[i];
        } else {
            return -1;
        }
    }

    public int getLastPos(String s) {
        int i = headIndexHashMap.get(s);
        if (i > 0) {
            return headIndexArray[i + 1];
        } else {
            return -1;
        }
    }
    HashMap<String, Integer> headIndexHashMap;
    int headIndexArray[];
}