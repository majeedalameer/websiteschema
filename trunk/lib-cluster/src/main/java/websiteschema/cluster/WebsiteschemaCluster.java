/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster;

import java.util.*;
import websiteschema.model.domain.cluster.*;
import websiteschema.persistence.hbase.ClusterModelMapper;
import websiteschema.persistence.hbase.SampleMapper;

/**
 *
 * @author ray
 */
public class WebsiteschemaCluster implements Runnable {

    String siteId;
    SampleMapper mapper;
    ClusterModelMapper cmMapper;
    ClusterModel model;

    @Override
    public void run() {
        List<Sample> samples = mapper.getList(siteId);
        if (null != samples && !samples.isEmpty()) {
            Clusterer cc = new CosineClusterer(siteId);
            cc.appendSample(samples);
            cc.statFeature();
            model = cc.clustering();
            model.printClusterInfo();
            if (null != cmMapper) {
                System.out.println("Save ClusterModel...");
                cmMapper.put(model);
            }
        }
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public void setSampleMapper(SampleMapper mapper) {
        this.mapper = mapper;
    }

    public void setCmMapper(ClusterModelMapper cmMapper) {
        this.cmMapper = cmMapper;
    }
}
