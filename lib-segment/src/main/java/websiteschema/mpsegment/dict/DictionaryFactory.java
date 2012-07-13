package websiteschema.mpsegment.dict;

import org.apache.log4j.Logger;
import websiteschema.mpsegment.conf.Configure;
import websiteschema.mpsegment.dict.domain.DomainDictFactory;
import websiteschema.mpsegment.core.MPSegment;
import websiteschema.mpsegment.util.FileUtil;

public class DictionaryFactory {

    private final static Logger l = Logger.getLogger("segment");
    private final static DictionaryFactory ins = new DictionaryFactory();

    public static DictionaryFactory getInstance() {
        return ins;
    }
    private HashDictionary coreDict;
    private DomainDictFactory domainFactory;
    private boolean loadDomainDictionary = Configure.getInstance().isLoadDomainDictionary();
    private boolean loadUserDictionary = Configure.getInstance().isLoadUserDictionary();

    public HashDictionary getCoreDictionary() {
        return coreDict;
    }

//    public DomainDictionary getDomainDictionary() {
//        return domainFactory.getDomainDictionary();
//    }

    public void loadDictionary() {
        String segment_dict = Configure.getInstance().getSegmentDict();
        String segment_dict_fre = (new StringBuilder(String.valueOf(FileUtil.removeExtension(segment_dict)))).append(".fre").toString();
        long l1 = System.currentTimeMillis();
        POSAndFreq.loadPOSDb(segment_dict_fre);
        coreDict = new HashDictionary(Configure.getInstance().getSegmentDict());

        l1 = System.currentTimeMillis() - l1;
        l.debug((new StringBuilder()).append("[Segment] ").append("loading dictionary time used(\u8017\u8D39\u65F6\u95F4)\uFF1A").append(l1).toString());
    }

    public void loadDomainDictionary() {
        if (loadDomainDictionary || loadUserDictionary) {
            domainFactory = DomainDictFactory.getInstance();
            domainFactory.buildDictionary();
        }
    }

    public void loadUserDictionary() {
        if (loadUserDictionary) {
            long l1 = System.currentTimeMillis();
            String userDictFile = Configure.getInstance().getUserDictionaryFile();
            UserDictionaryLoader userDict = new UserDictionaryLoader(DomainDictFactory.getInstance().getDomainDictionary(), coreDict);
            userDict.loadUserDictionary(userDictFile);
            userDict.buildDisambiguationRule(new MPSegment());
            userDict.clear();
            l1 = System.currentTimeMillis() - l1;
            l.debug((new StringBuilder()).append("[Segment] ").append("loading user dictionary time used(\u8017\u8D39\u65F6\u95F4)\uFF1A").append(l1).toString());
        }
    }
}
