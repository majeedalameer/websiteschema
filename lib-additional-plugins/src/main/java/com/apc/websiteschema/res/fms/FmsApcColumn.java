/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.res.fms;

import java.util.Map;
import websiteschema.common.wrapper.BeanWrapper;

/**
 *
 * @author ray
 */
public class FmsApcColumn {

    String id;
    String name;
    String parentId;
    String depth;

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public static FmsApcColumn apply(Map<String, String> map) {
        FmsApcColumn obj = BeanWrapper.getBean(map, FmsApcColumn.class, false);
        return obj;
    }
}
