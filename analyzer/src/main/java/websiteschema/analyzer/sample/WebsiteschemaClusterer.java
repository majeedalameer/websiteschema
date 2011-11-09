/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.sample;

import java.awt.Component;
import java.util.*;
import javax.swing.JOptionPane;
import websiteschema.cluster.Clusterer;
import websiteschema.cluster.CosineClusterer;
import websiteschema.cluster.analyzer.ClusterAnalyzer;
import websiteschema.model.domain.Websiteschema;
import websiteschema.model.domain.cluster.*;
import websiteschema.persistence.hbase.ClusterModelMapper;
import websiteschema.persistence.hbase.SampleMapper;
import websiteschema.persistence.hbase.WebsiteschemaMapper;
import websiteschema.utils.DateUtil;

/**
 *
 * @author ray
 */
public class WebsiteschemaClusterer implements Runnable {

    String siteId;
    SampleMapper sampleMapper;
    ClusterModelMapper cmMapper;
    WebsiteschemaMapper websiteschemaMapper;
    ClusterModel model;
    ClusterAnalyzer analyzer;
    Component parentComponent;
    AnalysisPanel panel;

    @Override
    public void run() {
        String now = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm");
        String end = siteId + "+" + now;
        List<Sample> samples = sampleMapper.getList(siteId, end);
        if (null != samples && !samples.isEmpty()) {
            Clusterer cc = new CosineClusterer(siteId);
            cc.appendSample(samples);
            cc.statFeature();
            model = cc.clustering();
            model.printClusterInfo();
            if (null != cmMapper) {
                System.out.println("Saving ClusterModel...");
                cmMapper.put(model);
                Websiteschema schema = websiteschemaMapper.get(siteId);
                if (null != schema) {
                    Map<String, String> prop = schema.getProperties();
                    prop = analyzer.analysis(prop, model, samples);
                    schema.setProperties(prop);
                    websiteschemaMapper.put(schema);
                }
            }
            this.panel.setSiteId(siteId);
            JOptionPane.showMessageDialog(parentComponent, "聚类分析完成");
        } else {
            JOptionPane.showMessageDialog(parentComponent, "样本集为空");
        }
        System.gc();
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public void setSampleMapper(SampleMapper mapper) {
        this.sampleMapper = mapper;
    }

    public void setCmMapper(ClusterModelMapper cmMapper) {
        this.cmMapper = cmMapper;
    }

    public void setAnalyzer(ClusterAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void setWebsiteschemaMapper(WebsiteschemaMapper websiteschemaMapper) {
        this.websiteschemaMapper = websiteschemaMapper;
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    public void setPanel(AnalysisPanel panel) {
        this.panel = panel;
    }
}
