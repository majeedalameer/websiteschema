/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import websiteschema.cluster.DocVectorConvertor;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.model.domain.cluster.Dimension;
import websiteschema.model.domain.cluster.DocVector;
import websiteschema.model.domain.cluster.FeatureInfo;
import websiteschema.model.domain.cluster.FeatureStatInfo;
import websiteschema.model.domain.cluster.Sample;
import static websiteschema.utils.PojoMapper.*;

/**
 * 用来分析新闻类型的网站
 * @author ray
 */
public class BaseClusterAnalyzer implements ClusterAnalyzer {

    Set<String> validNodes = new HashSet<String>();
    Set<String> invalidNodes = new HashSet<String>();
    AnalyzerUtil analyzer = new AnalyzerUtil();

    public Map<String, String> analysis(Map<String, String> old, ClusterModel cm, List<Sample> samples) {
        if (null == cm || null == samples) {
            return old;
        }
        Map<String, String> ret = null != old ? old : new HashMap<String, String>();

        Cluster[] clusters = cm.getClusters();
        FeatureStatInfo statInfo = cm.getStatInfo();
        if (null != clusters) {
            List<DocVector> space = analyzer.convertSamples(samples, statInfo);

            for (Cluster cluster : clusters) {
                if (cluster.getSamples().size() > 1) {
                    List<String> rowKeys = cluster.getSamples();
                    List<DocVector> vectors = analyzer.getVectors(rowKeys, space);
                    List<Sample> clusterSamples = analyzer.getSamples(rowKeys, samples);
                    Set<String> commonNodes = analyzer.findCommonNodes(vectors, statInfo);
                    Set<String> invalidNodeSet = analyzer.findInvalidNodes(clusterSamples, commonNodes, 0.6);
                    invalidNodes.addAll(invalidNodeSet);
                    validNodes.addAll(commonNodes);
                    for (String xpath : invalidNodes) {
                        validNodes.remove(xpath);
                    }
                } else {
                }
            }
            try {
                ret.put("ValidNodes", toJson(validNodes));
                ret.put("InvalidNodes", toJson(invalidNodes));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return ret;
    }

    private void findTitlePrefix() {
        String titlePath = "html/head/title";
        
    }
}
