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
import websiteschema.model.domain.cluster.DocVector;
import websiteschema.model.domain.cluster.FeatureStatInfo;
import websiteschema.model.domain.cluster.Sample;
import static websiteschema.utils.PojoMapper.*;

/**
 *
 * @author ray
 */
public class ClusterAnalyzerImpl implements ClusterAnalyzer {

    BasicAnalysisResult result = null;
    AnalyzerUtil analyzer = AnalyzerUtil.getInstance();
    IClusterTypeRecognizer clusterTypeRecognizer = new SimpleClusterTypeRecognizer();
    IBasicClusterAnalyzer simpleAnalyzer = new SimpleBasicClusterAnalyzer();

    //对聚类结果进行基本分析
    public Map<String, String> analysis(Map<String, String> old, ClusterModel cm, List<Sample> samples) {
        if (null == cm || null == samples) {
            return old;
        }

        Cluster[] clusters = cm.getClusters();
        FeatureStatInfo statInfo = cm.getStatInfo();
        if (null != clusters) {
            List<DocVector> space = analyzer.convertSamples(samples, statInfo);

            //查找每个类中的相同节点，识别其中的有效节点、无效节点、标题前缀和后缀。
            findBasicParameter(clusters, space, samples, statInfo);
            //确定每个类的类型
            confirmClusterType(clusters, space, samples, statInfo);
        }

        //整理结果并返回
        return createResult(old);
    }

    private void findBasicParameter(Cluster[] clusters, List<DocVector> space, List<Sample> samples, FeatureStatInfo statInfo) {
        result = simpleAnalyzer.analysis(clusters, space, samples, statInfo);
    }

    private Map<String, String> createResult(Map<String, String> old) {
        Map<String, String> ret = null != old ? old : new HashMap<String, String>();
        try {
            ret.put("ValidNodes", toJson(result.getValidNodes()));
            ret.put("InvalidNodes", toJson(result.getInvalidNodes()));
            ret.put("TitlePrefix", toJson(result.getTitlePrefix()));
            ret.put("TitleSuffix", toJson(result.getTitleSuffix()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    private void analyzeEachCluster(Cluster cluster) {
    }

    private void confirmClusterType(Cluster[] clusters, List<DocVector> space, List<Sample> samples, FeatureStatInfo statInfo) {
        this.clusterTypeRecognizer.setBasicAnalysisResult(result);
        this.clusterTypeRecognizer.recognizeClusterType(clusters, space, samples, statInfo);
    }

    @Override
    public BasicAnalysisResult getBasicAnalysisResult() {
        return this.result;
    }

    @Override
    public void setFieldAnalyzers(List<IFieldAnalyzer> fieldAnalyzers) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
