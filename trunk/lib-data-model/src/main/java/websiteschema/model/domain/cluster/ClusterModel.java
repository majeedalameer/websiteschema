/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain.cluster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author ray
 */
public class ClusterModel {

    Cluster[] clusters = null;
    FeatureStatInfo statInfo = null;
    int totalSamples = 0;

    public Cluster getCluster(int index) {
        return clusters[index];
    }

    public Cluster[] getClusters() {
        return clusters;
    }

    public void setClusters(Cluster[] clusters) {
        this.clusters = clusters;
    }

    public FeatureStatInfo getStatInfo() {
        return statInfo;
    }

    public void setStatInfo(FeatureStatInfo statInfo) {
        this.statInfo = statInfo;
    }

    public int getTotalSamples() {
        return totalSamples;
    }

    public void setTotalSamples(int totalSamples) {
        this.totalSamples = totalSamples;
    }

    public void printClusterInfo() {
        if (null != clusters) {
            for (int i = 0; i < clusters.length; i++) {
                System.out.println("Cluster " + i);
                List<String> sameKindInstancs = clusters[i].getSamples();
                for (String sample : sameKindInstancs) {
                    System.out.println(sample);
                }
            }
        }
    }
}
