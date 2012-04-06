/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.fb;

import com.apc.indextask.idx.Idx;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.utils.Escape;

/**
 *
 * @author ray
 */
@EO(name = {"EO", "BAT_OUT"})
@EI(name = {"EI:CONVERT", "BAT:BATCH"})
public class DocToIdxFB extends FunctionBlock {

    @DI(name = "DOC")
    public Doc doc = null;
    @DI(name = "DOCS")
    public List<Doc> docs = null;
    @DI(name = "MAP")
    public Map<String, String> map = null;
    @DI(name = "DEF")
    public Map<String, String> def = null;
    @DI(name = "ENCODE")
    public List<String> encodeFields = null;
    @DO(name = "IDX", relativeEvents = {"EO"})
    public Idx idx = null;
    @DO(name = "CONTENT", relativeEvents = {"BAT_OUT"})
    public String content = null;

    @Algorithm(name = "BATCH")
    public void batch() {
        try {
            StringBuilder sb = new StringBuilder();
            for (Doc d : docs) {
                Idx i = convertIdx(d, def, map, encodeFields);
                sb.append(i.toString()).append("\n");
            }
            content = sb.toString();
            triggerEvent("BAT_OUT");
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
            throw new RuntimeException("Doc不能转换成IDX");
        }
    }

    @Algorithm(name = "CONVERT")
    public void convert() {
        try {
            idx = convertIdx(doc, def, map, encodeFields);
            triggerEvent("EO");
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
            throw new RuntimeException("Doc不能转换成IDX");
        }
    }

    private Idx convertIdx(Doc doc, Map<String, String> def, Map<String, String> map, List<String> encodeFields) {
        Idx ret = new Idx();
        if (null != def) {
            for (String key : def.keySet()) {
                addField(ret, key, def.get(key));
            }
        }
        for (String field : doc.keySet()) {
            String key = mapTo(field, map) != null ? mapTo(field, map) : field;
            Collection<String> values = doc.getValues(field);
            if (null != values) {
                for (String value : values) {
                    if (null != encodeFields && encodeFields.contains(field)) {
                        addField(ret, key, Escape.escape(value, "UTF-8"));
                    } else {
                        addField(ret, key, value);
                    }
                }
            }
        }
        return ret;
    }

    private void addField(Idx idx, String key, String value) {
        idx.addField(key, value);
    }

    private String mapTo(String field, Map<String, String> map) {
        if (null != map) {
            return map.get(field);
        }
        return null;
    }
}
