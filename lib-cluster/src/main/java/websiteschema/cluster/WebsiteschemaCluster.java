/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster;

import java.util.*;
import websiteschema.model.domain.cluster.*;
import websiteschema.persistence.hbase.SampleMapper;

/**
 *
 * @author ray
 */
public class WebsiteschemaCluster implements Runnable {

    String siteId;
    SampleMapper mapper;
    ClusterModel model;

    @Override
    public void run() {
        List<Sample> samples = mapper.getList(siteId);
        if (null != samples && !samples.isEmpty()) {
            Clusterer cc = new CosineClusterer();
            cc.appendSample(samples);
            cc.statFeature();
            model = cc.clustering();
            model.printClusterInfo();
        }
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public void setMapper(SampleMapper mapper) {
        this.mapper = mapper;
    }
}
