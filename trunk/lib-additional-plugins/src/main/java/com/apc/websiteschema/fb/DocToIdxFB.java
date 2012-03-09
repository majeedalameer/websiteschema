/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.fb;

import com.apc.indextask.idx.Idx;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
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
@EO(name = {"EO"})
@EI(name = {"EI:CONVERT"})
public class DocToIdxFB extends FunctionBlock {

    @DI(name = "DOC")
    public Doc doc = null;
    @DI(name = "MAP")
    public Map<String, String> map = null;
    @DI(name = "DEF")
    public Map<String, String> def = null;
    @DI(name = "ENCODE")
    public Set<String> encodeFields = null;
    @DO(name = "IDX", relativeEvents = {"EO"})
    public Idx idx = null;

    @Algorithm(name = "CONVERT")
    public void convert() {
        try {
            idx = new Idx();
            if (null != def) {
                for (String key : def.keySet()) {
                    addField(key, def.get(key));
                }
            }
            for (String field : doc.keySet()) {
                String key = mapTo(field) != null ? mapTo(field) : field;
                Collection<String> values = doc.getValues(field);
                if (null != values) {
                    for (String value : values) {
                        addField(key, value);
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Doc不能转换成IDX");
        }
        triggerEvent("EO");
    }

    private void addField(String key, String value) {
        if (encodeFields.contains(key)) {
            idx.addField(key, Escape.escape(value, "UTF-8"));
        } else {
            idx.addField(key, value);
        }
    }

    private String mapTo(String field) {
        return map.get(field);
    }
}
