package websiteschema.mpsegment.conf;

import java.io.*;
import java.util.Properties;
import org.apache.log4j.Logger;

public final class Configure {

    private static Logger l = Logger.getLogger("segment");
    private static final Configure configure = new Configure();

    private Configure() {
        Properties p = new Properties();
        try {
            l.debug("load maxprob-default.cfg: " + Configure.class.getClassLoader().getResource("maxprob-default.cfg"));
            p.load(Configure.class.getClassLoader().getResourceAsStream("maxprob-default.cfg"));
            if (null != Configure.class.getClassLoader().getResource("maxprob.cfg")) {
                l.debug("load maxprob.cfg: " + Configure.class.getClassLoader().getResource("maxprob.cfg"));
                p.load(Configure.class.getClassLoader().getResourceAsStream("maxprob.cfg"));
            }
        } catch (IOException ex) {
            l.error("error when loading Chinese NLP configure files", ex);
        }
        initialize(p);
    }

    public static Configure getInstance() {
        return configure;
    }

    private String getHomePath() {
        return homePath;
    }

    public static int SectionSize() {
        return 1024;
    }

    public void setHomePath(String path) {
        int i1 = path.length();
        if (i1 >= 1) {
            if (path.charAt(i1 - 1) == '/' || path.charAt(i1 - 1) == '\\') {
                homePath = path;
            } else {
                homePath = (new StringBuilder(String.valueOf(path))).append("/").toString();
            }
        }
    }

    public String getDictFile() {
        return new StringBuilder(homePath).append("./segment.dat").toString();
    }

    public final void initialize(Properties prop) {
        l.debug(new StringBuilder().append("[System] load properties begin...").toString());
        try {
            segment_maximumthreads = Integer.parseInt(prop.getProperty("segment.maximumthreads", "2").trim());
            segment_dict = prop.getProperty("cnnlp.lexical.segment.MPSegment", "segment.dict");
            POSMatrix_fre = prop.getProperty("cnnlp.lexical.segment.POSTagging", "POSMatrix.fre");
            NamePOSMatrix_fre = prop.getProperty("cnnlp.lexical.segment.NameTagging", "NamePOSMatrix.fre");
            ChNameDict = prop.getProperty("cnnlp.lexical.segment.ChNameDict", "ChName.dict");
            word_freq = prop.getProperty("ir.wordfreq", "word.freq");
            loaddomaindictionary = Boolean.valueOf(prop.getProperty("domain.loaddomaindictionary", "true")).booleanValue();
            loadDomainDictFromDB = Boolean.valueOf(prop.getProperty("domain.loaddictionaryfromdb", "false")).booleanValue();
            domaindictionaryurl = prop.getProperty("domain.db.url", "");
            domaindictloader = prop.getProperty("domain.dictloader", "");
            domaindictionaryusername = prop.getProperty("domain.db.username", "");
            domaindictionarypassword = prop.getProperty("domain.db.password", "");
            domaindictionary = prop.getProperty("domain.domaindictionary", "domain.dict");
            domain_dof = prop.getProperty("domain.domainontology", "domain.dof");
            loaduserdictionary = Boolean.valueOf(prop.getProperty("user.loaduserdictionary", "false")).booleanValue();
            userdict_txt = prop.getProperty("user.dictionaryfile", "userdict.txt");
            stopword_txt = prop.getProperty("user.stopwordfile", "stopword.txt");
            stoplist = prop.getProperty("user.stoplist", "");
            segment_min = Boolean.valueOf(prop.getProperty("segment.min", "false")).booleanValue();
            querysyntax = Boolean.valueOf(prop.getProperty("segment.querysyntax", "false")).booleanValue();
            gluechar = prop.getProperty("segment.gluechar", "*?~/_[]:");
            maxquerylength = Integer.parseInt(prop.getProperty("segment.maxquerylength", "512").trim());
            stopwordfilter = Boolean.valueOf(prop.getProperty("segment.stopwordfilter", "false")).booleanValue();
            chinesenumbertodigital = Boolean.valueOf(prop.getProperty("segment.chinesenumbertodigital", "false")).booleanValue();
            chinesenameidentify = Boolean.valueOf(prop.getProperty("segment.chinesenameidentify", "true")).booleanValue();
            xingmingseparate = Boolean.valueOf(prop.getProperty("segment.xingmingseparate", "true")).booleanValue();
            halfshapeall = Boolean.valueOf(prop.getProperty("segment.halfshapeall", "false")).booleanValue();
            uppercaseall = Boolean.valueOf(prop.getProperty("segment.uppercaseall", "false")).booleanValue();
            maxwordlength = Integer.parseInt(prop.getProperty("segment.maxwordlength", "8").trim());
            ExtendPOSInDomainDictionary = Boolean.valueOf(prop.getProperty("domain.extendposindomaindictionary", "false")).booleanValue();
            prop = null;
        } catch (Exception e) {
            l.error((new StringBuilder()).append("[System] Could not find ").append(cfgFile).append(" !").toString());
        }
        l.debug(new StringBuilder().append("[System] load properties finished !").toString());
    }

    public String getFileEncoding() {
        return default_encoding;
    }

    public int getMaximumThreads() {
        return segment_maximumthreads;
    }

    public String getSegmentDict() {
        return new StringBuilder(getHomePath()).append(segment_dict).toString();
    }

    public String getPOSMatrix() {
        return new StringBuilder(getHomePath()).append(POSMatrix_fre).toString();
    }

    public String getNamePOSMatrix() {
        return new StringBuilder(getHomePath()).append(NamePOSMatrix_fre).toString();
    }

    public String getChNameDict() {
        return new StringBuilder(getHomePath()).append(ChNameDict).toString();
    }

    public String getWordFreqFile() {
        return new StringBuilder(getHomePath()).append(word_freq).toString();
    }

    public boolean isLoadDomainDictionary() {
        return loaddomaindictionary;
    }

    public String getDomainDictionaryFile() {
        return new StringBuilder(getHomePath()).append(domaindictionary).toString();
    }

    public String getDomainOntologyFile() {
        return new StringBuilder(getHomePath()).append(domain_dof).toString();
    }

    public boolean isLoadUserDictionary() {
        return loaduserdictionary;
    }

    public String getUserDictionaryFile() {
        return new StringBuilder(getHomePath()).append(userdict_txt).toString();
    }

    public String getStopWordFile() {
        return new StringBuilder(getHomePath()).append(stopword_txt).toString();
    }

    public boolean isSupportQuerySyntax() {
        return querysyntax;
    }

    public String getStopPosList() {
        return stoplist;
    }

    public boolean isSegmentMin() {
        return segment_min;
    }

    public String getGlueChar() {
        return gluechar;
    }

    public int getMaxQueryLength() {
        return maxquerylength;
    }

    public boolean isFilterStopWord() {
        return stopwordfilter;
    }

    public boolean isChineseNumberToDigital() {
        return chinesenumbertodigital;
    }

    public boolean isChineseNameIdentify() {
        return chinesenameidentify;
    }

    public boolean isXingMingSeparate() {
        return xingmingseparate;
    }

    public boolean isHalfShapeAll() {
        return halfshapeall;
    }

    public boolean isUpperCaseAll() {
        return uppercaseall;
    }

    public int getMaxWordLength() {
        return maxwordlength;
    }

    public boolean isLoadDomainDictionaryFromDB() {
        return loadDomainDictFromDB;
    }

    public String getDomainDictLoader() {
        return domaindictloader;
    }

    public String getDomainDatabaseURL() {
        return domaindictionaryurl;
    }

    public String getDomainDatabaseUsername() {
        return domaindictionaryusername;
    }

    public String getDomainDatabasePassword() {
        return domaindictionarypassword;
    }

    public boolean isExtendPOSInDomainDictionary() {
        return ExtendPOSInDomainDictionary;
    }
    private String homePath = "";
    private int segment_maximumthreads = 2;
    private String default_encoding = "gbk";
    private String cfgFile = "./cnnlp.cfg";
    private String segment_dict = "segment.dict";
    private String POSMatrix_fre = "POSMatrix.fre";
    private String NamePOSMatrix_fre = "NamePOSMatrix.fre";
    private String ChNameDict = "ChName.dict";
    private String word_freq = "word.freq";
    private boolean loaddomaindictionary = false;
    private boolean loadDomainDictFromDB = false;
    private String domaindictloader = "";
    private String domaindictionaryurl = "";
    private String domaindictionaryusername = "";
    private String domaindictionarypassword = "";
    private String domaindictionary = "domain.dict";
    private String domain_dof = "domain.dof";
    private boolean loaduserdictionary = false;
    private String userdict_txt = "userdict.txt";
    private boolean segment_min = false;
    private String stopword_txt = "stopword.txt";
    private String stoplist = "";
    private boolean querysyntax = false;
    private String gluechar = "*?~/_[]:";
    private int maxquerylength = 512;
    private boolean stopwordfilter = false;
    private boolean chinesenumbertodigital = false;
    private boolean chinesenameidentify = true;
    private boolean xingmingseparate = true;
    private boolean halfshapeall = false;
    private boolean uppercaseall = false;
    private int maxwordlength = 8;
    //领域词典中的词是否扩展其在普通词典中的词性
    private boolean ExtendPOSInDomainDictionary = false;
}
