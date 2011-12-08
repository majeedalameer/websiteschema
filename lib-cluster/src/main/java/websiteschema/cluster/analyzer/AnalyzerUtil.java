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
import websiteschema.model.domain.cluster.Dimension;
import websiteschema.model.domain.cluster.DocVector;
import websiteschema.model.domain.cluster.FeatureInfo;
import websiteschema.model.domain.cluster.FeatureStatInfo;
import websiteschema.model.domain.cluster.Sample;
import websiteschema.model.domain.cluster.Unit;
import websiteschema.utils.EditDistance;

/**
 *
 * @author ray
 */
public class AnalyzerUtil {

    EditDistance ld = new EditDistance();

    public List<DocVector> convertSamples(List<Sample> samples, FeatureStatInfo statInfo) {
        List<DocVector> space = new ArrayList<DocVector>();
        DocVectorConvertor convertor = new DocVectorConvertor();

        for (Sample sample : samples) {
            DocVector v = convertor.convert(sample, statInfo);
            space.add(v);
        }

        return space;
    }

    public Set<String> findCommonNodes(List<DocVector> samples, FeatureStatInfo statInfo) {
        Set<String> ret = new HashSet<String>();
        FeatureInfo features[] = statInfo.getList();
        for (FeatureInfo f : features) {
            ret.add(f.getName());
        }
        for (int i = 0; i < samples.size(); i++) {
            DocVector vi = samples.get(i);
            Dimension[] dims = vi.getDims();
            Set<String> set = new HashSet<String>();
            for (Dimension dim : dims) {
                String name = features[dim.getId()].getName();
                if (ret.contains(name)) {
                    set.add(name);
                }
            }
            ret = set;
        }
        return ret;
    }

    public Set<String> findInvalidNodes(List<Sample> samples, Set<String> commonNodes, double th) {
        Set<String> ret = new HashSet<String>();
        for (String xpath : commonNodes) {
            Map<String, String> mapText = getText(xpath, samples);
            List<String> listText = new ArrayList<String>();
            for (String key : mapText.keySet()) {
                listText.add(mapText.get(key));
            }
            double sim = ld.caculateSimilarity(listText);
            if (sim >= th) {
                ret.add(xpath);
            }
        }
        return ret;
    }

    /**
     * 获取指定xpath在样本中包含的文本，主要用来计算相似度，或查找相同的前缀和后缀。
     * @param xpath
     * @param samples
     * @return
     */
    public Map<String, String> getText(String xpath, List<Sample> samples) {
        Map<String, String> ret = new HashMap<String, String>();
        for (Sample sample : samples) {
            Unit[] units = sample.getUnits(xpath);
            StringBuilder sb = new StringBuilder();
            if (null != units) {
                for (Unit u : units) {
                    String text = u.getText();
                    if (null != text) {
                        sb.append(text.trim());
                    }
                }
            }
            ret.put(sample.getRowKey(), sb.toString());
        }
        return ret;
    }

    public List<Sample> getSamples(List<String> rowKeys, List<Sample> samples) {
        List<Sample> ret = new ArrayList<Sample>();
        Set<String> setRowKeys = new HashSet<String>(rowKeys);
        for (Sample vect : samples) {
            if (setRowKeys.contains(vect.getRowKey())) {
                ret.add(vect);
            }
        }
        return ret;
    }

    public List<DocVector> getVectors(List<String> rowKeys, List<DocVector> samples) {
        List<DocVector> ret = new ArrayList<DocVector>();
        Set<String> setRowKeys = new HashSet<String>(rowKeys);
        for (DocVector vect : samples) {
            if (setRowKeys.contains(vect.getName())) {
                ret.add(vect);
            }
        }
        return ret;
    }
}
