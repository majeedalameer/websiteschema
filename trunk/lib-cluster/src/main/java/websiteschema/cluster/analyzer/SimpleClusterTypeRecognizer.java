/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.util.ArrayList;
import java.util.List;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.Dimension;
import websiteschema.model.domain.cluster.DocVector;
import websiteschema.model.domain.cluster.FeatureInfo;
import websiteschema.model.domain.cluster.FeatureStatInfo;
import websiteschema.model.domain.cluster.Sample;

/**
 *
 * @author ray
 */
public class SimpleClusterTypeRecognizer implements IClusterTypeRecognizer {

    BasicAnalysisResult result = null;
    AnalyzerUtil analyzer = new AnalyzerUtil();

    public void recognizeClusterType(Cluster[] clusters, List<DocVector> space, List<Sample> samples, FeatureStatInfo statInfo) {
        for (Cluster cluster : clusters) {
            if (cluster.getSamples().size() > 0) {
                List<String> allSamples = cluster.getSamples();
                List<String> rowKeys = new ArrayList<String>();
                int count = allSamples.size() > 10 ? 10 : allSamples.size();
                for (int i = 0; i < count; i++) {
                    rowKeys.add(allSamples.get(i));
                }
                List<DocVector> vectors = analyzer.getVectors(rowKeys, space);
//                List<Sample> clusterSamples = analyzer.getSamples(rowKeys, samples);
//                Set<String> commonNodes = analyzer.findCommonNodes(vectors, statInfo);
                double textWeight = 0.0;
                int textCount = 0;
                double anchorWeight = 0.0;
                int anchorCount = 0;
//                DocVector v = vectors.get(0);
                for (DocVector v : vectors) {
                    Dimension dims[] = v.getDims();
                    for (Dimension dim : dims) {
                        int dimId = dim.getId();
                        int value = dim.getValue();
                        FeatureInfo feature = statInfo.getList()[dimId];
                        String xpath = feature.getName();
                        if (!result.getInvalidNodes().contains(xpath)) {
                            xpath = xpath.toLowerCase();
                            if (feature.getWeight() > 0) {
                                if (xpath.endsWith("/a")) {
                                    anchorWeight += Math.log(feature.getWeight());
                                    anchorCount++;
                                } else {
                                    textWeight += Math.log(feature.getWeight());
                                    textCount++;
                                }
                            }
                        }
                    }
                }
                double ratio = textWeight / (textWeight + anchorWeight);
                double countRatio = (double) textCount / (double) (textCount + anchorCount);
                System.out.println("cluster: " + cluster.getCustomName() + " text ratio: " + ratio + "text count ratio: " + countRatio);
                String type = "LINKS";
                if ((textWeight + anchorWeight) < 0.01D) {
                    type = "INVALID";
                } else if (ratio > 0.5) {
                    type = "DOCUMENT";
                }
                cluster.setType(type);
            } else {
                String type = "INVALID";
                cluster.setType(type);
            }
        }
    }

    public void setBasicAnalysisResult(BasicAnalysisResult analysisResult) {
        this.result = analysisResult;
    }
}
