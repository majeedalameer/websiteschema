/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.browser.left.sample;

import websiteschema.analyzer.browser.left.AnalysisPanel;
import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import websiteschema.cluster.Clusterer;
import websiteschema.cluster.CosineClusterer;
import websiteschema.cluster.analyzer.ClusterAnalyzer;
import websiteschema.model.domain.Websiteschema;
import websiteschema.model.domain.cluster.*;
import websiteschema.persistence.hbase.ClusterModelMapper;
import websiteschema.persistence.hbase.SampleMapper;
import websiteschema.persistence.hbase.WebsiteschemaMapper;
import websiteschema.utils.DateUtil;
import websiteschema.analyzer.context.BrowserContext;

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
    JTextArea textArea;

    @Override
    public void run() {
        String now = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm");
        String end = siteId + "+" + now;
        textArea.append("正在加载样本...\n");
        List<Sample> samples = sampleMapper.getList(siteId, end);
        if (null != samples && !samples.isEmpty()) {
            Clusterer cc = new CosineClusterer(siteId);
            cc.appendSample(samples);
            textArea.append("开始统计特征值...\n");
            cc.statFeature();
            textArea.append("开始聚类...\n");
            model = cc.clustering();
            StringWriter sw = new StringWriter();
            model.printClusterInfo(new PrintWriter(sw));
            textArea.append(sw.getBuffer().toString());
            if (null != cmMapper) {
                textArea.append("\n聚类完成...\n");
                Websiteschema schema = websiteschemaMapper.get(siteId);
                if (null != schema) {
                    Map<String, String> prop = schema.getProperties();
                    textArea.append("开始分析各种参数...\n");
                    prop = analyzer.analysis(prop, model, samples);
                    schema.setProperties(prop);
                    websiteschemaMapper.put(schema);
                }
                textArea.append("保存分析结果...\n");
                cmMapper.put(model);
            }
            //重新刷新一下AnalysisPanel
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

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }
}
