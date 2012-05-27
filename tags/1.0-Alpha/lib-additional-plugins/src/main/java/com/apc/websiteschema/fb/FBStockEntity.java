/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.fb;

import cnnlp.lexical.segment.SegmentEngine2;
import cnnlp.lexical.segment.SegmentWorker;
import cnnlp.lexical.segment.WordAtoms;
import cnnlp.resource.domain.DomainFactory;
import com.apc.websiteschema.res.CnStockEntities;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EO(name = {"EO", "BAT_OUT"})
@EI(name = {"EI:ADD_ENTITY", "BAT:BATCH"})
public class FBStockEntity extends FunctionBlock {

    @DI(name = "DOC")
    @DO(name = "DOC", relativeEvents = {"EO"})
    public Doc doc = null;
    @DI(name = "DOCS")
    @DO(name = "DOCS", relativeEvents = {"BAT_OUT"})
    public List<Doc> docs = null;
    @DI(name = "TAG")
    public String tagName = "COM_ORG";
    @DI(name = "TARGET")
    public List<String> targetTags;

    @Algorithm(name = "ADD_ENTITY")
    public void addTag() {
        String text = getText(doc, targetTags);
        WordAtoms words = segment(text); //中文分词
        Set<String> orgs = findOrg(words);
        if (null != orgs) {
            for (String org : orgs) {
                addTag(doc, org, tagName);
            }
        }
        triggerEvent("EO");
    }

    @Algorithm(name = "BATCH")
    public void batchAddTag() {
        if (null != docs) {
            for (Doc d : docs) {
                String text = getText(d, targetTags);
                WordAtoms words = segment(text); //中文分词
                Set<String> orgs = findOrg(words);
                if (null != orgs) {
                    for (String org : orgs) {
                        addTag(d, org, tagName);
                    }
                }
            }
        }
        triggerEvent("BAT_OUT");
    }

    private void addTag(Doc doc, String org, String tagName) {
        if (CnStockEntities.getInstance().contains(org)) {
            //如果org属于上市公司
            String securityCode = CnStockEntities.getInstance().getStockId(org);
            doc.addField(tagName, org);
            doc.addField(tagName, securityCode);
        }
    }

    private Set<String> findOrg(WordAtoms words) {
        Set<String> ret = new HashSet<String>();
        if (null != words) {
            for (int i = 0; i < words.length(); i++) {
                String word = words.getWords(i);
                int mark = words.getMarks(i);
                if (10002 == mark) {
                    String head = getSynonym(word);
                    ret.add(head);
                }
            }
        }
        return ret;
    }

    public String getSynonym(String word) {
        String ret = word;
        String[] synonyms = DomainFactory.getInstance().getAllSynonym(word);
        if (null != synonyms && synonyms.length > 0) {
            ret = synonyms[synonyms.length - 1];
            ret = doHalfShape(ret);
        }
        return ret;
    }

    private String doHalfShape(String s1) {
        String s2 = null;
        char ac[] = s1.toCharArray();
        for (int i1 = 0; i1 < ac.length; i1++) {
            if (isWordStartWithAlphabeticalOrDigital(ac[i1])) {
                if (ac[i1] >= '\uFF21' && ac[i1] <= '\uFF41' || ac[i1] >= '\uFF3A' && ac[i1] <= '\uFF5A' || ac[i1] >= '\uFF10' && ac[i1] <= '\uFF19') {
                    ac[i1] = (char) (ac[i1] - 65248);
                }
            }
        }

        s2 = new String(ac);
        return s2;
    }

    private boolean isWordStartWithAlphabeticalOrDigital(char c1) {
        boolean flag = false;
        if (c1 >= 'A' && c1 <= 'Z' || c1 >= 'a' && c1 <= 'z' || c1 >= '0' && c1 <= '9' || c1 >= '\uFF21' && c1 <= '\uFF41' || c1 >= '\uFF3A' && c1 <= '\uFF5A' || c1 >= '\uFF10' && c1 <= '\uFF19') {
            flag = true;
        }
        return flag;
    }

    private WordAtoms segment(String text) {
        SegmentWorker worker = SegmentEngine2.getInstance().getSegmentWorker();
        WordAtoms words = worker.segment2(text);
        SegmentEngine2.getInstance().freeWorker(worker);
        return words;
    }

    private String getText(Doc doc, List<String> targetTags) {
        if (null != targetTags) {
            StringBuilder source = new StringBuilder();
            for (String target : targetTags) {
                Collection<String> values = null;
                if (target.contains("/")) {
                    values = doc.getExtValue(target);
                } else {
                    values = doc.getValues(target);
                }
                if (null != values) {
                    for (String v : values) {
                        source.append(v.replaceAll(" ", "")).append("\n");
                    }
                }
            }
            return source.toString();
        }
        return null;
    }
}
