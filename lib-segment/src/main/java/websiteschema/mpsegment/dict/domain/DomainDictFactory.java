package websiteschema.mpsegment.dict.domain;

import org.apache.log4j.Logger;
import websiteschema.mpsegment.conf.Configure;
import websiteschema.mpsegment.dict.HashDictionary;
import websiteschema.mpsegment.dict.IWord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DomainDictFactory {

    private final static DomainDictFactory ins = new DomainDictFactory();
    private final static Logger l = Logger.getLogger("segment");
    private volatile DomainDictionary domainDict;

    public static DomainDictFactory getInstance() {
        return ins;
    }

    private DomainDictFactory() {
//        buildDictionary();
    }

    public final void buildDictionary() {
        if (Configure.getInstance().isLoadDomainDictionary()) {
            DomainDictionary dict = new DomainDictionary();
            if (Configure.getInstance().isLoadDomainDictionaryFromDB()) {
                loadDictFromDB(dict);
            }
            domainDict = dict;
        } else {
            DomainDictionary dict = new DomainDictionary();
            domainDict = dict;
        }
    }

    public String[] getAllSynonym(String wordName) {
        List<IWord> words = domainDict.getSynonymSet(wordName);
        if (null != words) {
            String[] ret = new String[words.size()];
            int i = 0;
            for (IWord word : words) {
                ret[i++] = word.getWordName();
            }
            return ret;
        }
        return new String[]{wordName};
    }

    public ArrayList gatAllWords() {
        throw new UnsupportedOperationException();
    }

    public DomainDictionary getDomainDictionary() {
        return domainDict;
    }

    public void loadDictFromDB(DomainDictionary dict) {
        try {
            loadEntities(dict);
            // @2011-05-30 领域词典中的词是否扩展其在普通词典中的词性
            if (Configure.getInstance().isExtendPOSInDomainDictionary()) {
                HashDictionary hashDictionary = new HashDictionary(Configure.getInstance().getSegmentDict());
                Iterator<IWord> iterator = dict.iterator();
                while(iterator.hasNext()) {
                    IWord dWord = iterator.next();
                    if (dWord == null) {
                        continue;
                    }
                    IWord hWord = hashDictionary.lookupWord(dWord.getWordName());
                    if (hWord == null) {
                        continue;
                    }
                    dWord.getPOSArray().add(hWord.getPOSArray());
                }
            }
        } catch (Exception ex) {
            l.error("exception when load dictionary from database " + ex.getMessage());
        } finally {
        }
    }

    private void loadEntities(DomainDictionary dict) {
        String classNames[] = Configure.getInstance().getDomainDictLoader().split("[,; ]+");
        for (String className : classNames) {
            DomainDictLoader loader = createDictLoader(className);
            if (null != loader) {
                loader.load(dict);
            }
        }
    }

    private DomainDictLoader createDictLoader(String className) {
        try {
            Class clazz = Class.forName(className);
            DomainDictLoader loader = (DomainDictLoader) clazz.newInstance();
            return loader;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
