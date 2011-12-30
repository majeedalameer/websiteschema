/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.io.IOException;
import java.util.ArrayList;
import websiteschema.utils.PojoMapper;
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
    List<IFieldAnalyzer> fieldAnalyzers = null;

    //对聚类结果进行基本分析
    public Map<String, String> analysis(Map<String, String> old, ClusterModel cm, List<Sample> samples) {
        if (null == cm || null == samples) {
            return old;
        }
        Map<String, String> ret = null != old ? old : new HashMap<String, String>();

        Cluster[] clusters = cm.getClusters();
        FeatureStatInfo statInfo = cm.getStatInfo();
        if (null != clusters) {
            List<DocVector> space = analyzer.convertSamples(samples, statInfo);

            //查找每个类中的相同节点，识别其中的有效节点、无效节点、标题前缀和后缀。
            findBasicParameter(clusters, space, samples, statInfo);
            //确定每个类的类型
            confirmClusterType(clusters, space, samples, statInfo);
            //对每个类进行分析，挖掘各个字段的配置，例如标题、作者等
            analyzeFields(clusters, samples, statInfo, ret);
        }

        //整理结果并返回
        return createResult(ret);
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
            if (null != fieldAnalyzers && !fieldAnalyzers.isEmpty()) {
                Map<String,String> map = new HashMap<String,String>();
                for (IFieldAnalyzer fieldAnalyzer : fieldAnalyzers) {
                    map.put(fieldAnalyzer.getFieldName(), fieldAnalyzer.getClass().getName());
                }
                ret.put("FieldAnalyzers", PojoMapper.toJson(map));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    private void analyzeFields(Cluster[] clusters, List<Sample> samples, FeatureStatInfo statInfo, Map<String, String> old) {
        if (null != fieldAnalyzers) {
            for (Cluster cluster : clusters) {
                List<String> allSamples = cluster.getSamples();
                List<String> rowKeys = new ArrayList<String>();
                int count = allSamples.size() > 10 ? 10 : allSamples.size();
                for (int i = 0; i < count; i++) {
                    rowKeys.add(allSamples.get(i));
                }
                //每个类在进行分析的时候，只分析十个样本
                List<Sample> clusterSamples = analyzer.getSamples(rowKeys, samples);
                analyzeEachCluster(cluster, statInfo, clusterSamples, old);
            }
        }
    }

    /**
     * 检查List中是否已经存在一个Map，这个Map和map一样。
     * @param old
     * @param map
     * @return
     */
    private boolean alreadyHas(List<Map<String, String>> old, Map<String, String> map) {
        if (null != old) {
            try {
                for (Map<String, String> compareTo : old) {
                    boolean same = true;
                    for (String key : map.keySet()) {
                        String value = map.get(key);
                        if (!compareTo.containsKey(key)) {
                            same = false;
                            break;
                        } else {
                            String v1 = compareTo.get(key);
                            if (!value.equals(v1)) {
                                same = false;
                                break;
                            }
                        }
                    }
                    if (same) {
                        return true;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return false;
    }

    private void mergeAnalysisResult(String fieldName, Map<String, String> res, Map<String, String> old) throws IOException {
        //如果不为空，则将结果保存到Map中
        if (null != res && !res.isEmpty()) {
            String ori = old.get(fieldName);
            List<Map<String, String>> list = null;
            if (null != ori) {
                try {
                    list = PojoMapper.fromJson(ori, List.class);
                } catch (Exception ex) {
                    //可能是旧版本，会导致反序列化失败
                    list = new ArrayList<Map<String, String>>();
                }
            } else {
                list = new ArrayList<Map<String, String>>();
            }
            if (!alreadyHas(list, res)) {
                list.add(res);
                old.put(fieldName, PojoMapper.toJson(list));
            }
        }
    }

    private void analyzeEachCluster(Cluster cluster, FeatureStatInfo statInfo, List<Sample> samples, Map<String, String> old) {
        for (IFieldAnalyzer fieldAnalyzer : fieldAnalyzers) {
            String[] types = fieldAnalyzer.getProperClusterType();
            for (String type : types) {
                if (cluster.getType().equals(type)) {
                    try {
                        Map<String, String> res = fieldAnalyzer.analyze(cluster, statInfo, result, samples);
                        //保存结果res存到old中，key是fieldAnalyzer.getFieldName()
                        mergeAnalysisResult(fieldAnalyzer.getFieldName(), res, old);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
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
        this.fieldAnalyzers = fieldAnalyzers;
    }

    public List<IFieldAnalyzer> getFieldAnalyzers() {
        return fieldAnalyzers;
    }
}
