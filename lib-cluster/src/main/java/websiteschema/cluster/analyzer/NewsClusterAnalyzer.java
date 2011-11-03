/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.model.domain.cluster.FeatureInfo;
import websiteschema.model.domain.cluster.FeatureStatInfo;
import websiteschema.model.domain.cluster.Sample;

/**
 *
 * @author ray
 */
public class NewsClusterAnalyzer implements ClusterAnalyzer {

    public Map<String, String> analysis(Map<String, String> old, ClusterModel cm, List<Sample> samples) {
        if (null == cm || null == samples) {
            return old;
        }
        Map<String, String> ret = null != old ? old : new HashMap<String, String>();

        Cluster[] clusters = cm.getClusters();
        FeatureStatInfo statInfo = cm.getStatInfo();
        if (null != clusters) {
            for (Cluster cluster : clusters) {
                List<String> rowKeys = cluster.getSamples();
                FeatureInfo[] features = statInfo.getList();

            }
        }

        return ret;
    }

    private Sample getSample(String rowKey, List<Sample> samples) {
        return null;
    }
}
