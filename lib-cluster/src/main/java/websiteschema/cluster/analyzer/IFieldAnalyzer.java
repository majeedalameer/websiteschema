/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.Sample;

/**
 * 
 * @author ray
 */
public interface IFieldAnalyzer {

    /**
     * 指明分析的字段类型，例如分析时间字段就返回PUBLISHDATE。
     * @return
     */
    public String getFieldName();

    /**
     * 用analyze方法得到的结果去初始化一个FieldAnalyzer，根据谁分析谁负责抽取的原则设计。
     * @param params - 用analyze方法得到的Map
     */
    public void init(Map<String, String> params);

    /**
     * 在特定的cluster中，针对某一字段进行分析。
     * @param cluster - 用于分析的对象
     * @param resultAnalysis - ClusterAnalyzer的基本分析结果，主要是指一些无效节点、有效节点等
     * @param samples - cluster中的样本
     * @return 分析结果
     */
    public Map<String, String> analyze(Cluster cluster, BasicAnalysisResult analysisResult, List<Sample> samples);

    /**
     * 抽取数据，结果可能是一个集合
     * @param doc
     * @return
     */
    public Set<String> extract(Document doc);
}
