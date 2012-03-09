/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apc.websiteschema.fb;

import com.apc.indextask.idx.Idx;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
        doc.addField("AUTHOR", "张三");
        doc.addField("URL", "http://news.dichan.sina.com.cn/2012/03/05/451315.html?source=rss");
        DocToIdxFB fb = new DocToIdxFB();
        fb.doc = doc;
        fb.map = createMap();
        fb.def = createDef();
        Set<String> encodeFields = new HashSet<String>();
        encodeFields.add("DREREFERENCE");
        fb.encodeFields = encodeFields;
        fb.convert();
        Idx idx = fb.idx;
        assert("张三".equals(idx.getTagValue("AUTHOR")));
        assert("NEWS".equals(idx.getDbName()));
        assert("title here".equals(idx.getTitle()));
        assert("content here".equals(idx.getContent()));
        System.out.println(idx.toString());
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
