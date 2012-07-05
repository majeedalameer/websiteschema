/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apc.websiteschema.fb;

import com.apc.indextask.idx.Idx;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import websiteschema.cluster.analyzer.Doc;

/**
 *
 * @author ray
 */
public class DocToIdxTest {

    @Test
    public void convert() {
        Doc doc = new Doc();
        doc.addField("TITLE", "title here");
        doc.addField("CONTENT", "content here");
        doc.addField("CONTENT", "content2");
        doc.addField("AUTHOR", "张三");
        doc.addField("URL", "http://news.dichan.sina.com.cn/2012/03/05/451315.html?source=rss");
        DocToIdxFB fb = new DocToIdxFB();
        fb.doc = doc;
        fb.map = createMap();
        fb.def = createDef();
        List<String> encodeFields = new ArrayList<String>();
        encodeFields.add("URL");
        fb.encodeFields = encodeFields;
        fb.convert();
        Idx idx = fb.idx;
        System.out.println(idx.toString());
        assert("张三".equals(idx.getTagValue("AUTHOR")));
        assert("NEWS".equals(idx.getDbName()));
        assert("title here".equals(idx.getTitle()));
        assert("content here\r\ncontent2".equals(idx.getContent()));
        assert("http%3A%2F%2Fnews%2Edichan%2Esina%2Ecom%2Ecn%2F2012%2F03%2F05%2F451315%2Ehtml%3Fsource%3Drss".equals(idx.getReference()));
    }

    private Map<String, String> createMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("TITLE", "DRETITLE");
        map.put("CONTENT", "DRECONTENT");
        map.put("URL", "DREREFERENCE");
        return map;
    }

    private Map<String, String> createDef() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("DREDBNAME", "NEWS");
        return map;
    }
}
