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
public class FmsChannel {

    String id;
    String name;
    String apcColumnId;
    String sourceId;
    String jobname;

    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    public String getApcColumnId() {
        return apcColumnId;
    }

    public void setApcColumnId(String apcColumnId) {
        this.apcColumnId = apcColumnId;
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

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getJobname() {
        return jobname;
    }

    public static FmsChannel apply(Map<String, String> map) {
        FmsChannel obj = BeanWrapper.getBean(map, FmsChannel.class, false);
        return obj;
    }
}
