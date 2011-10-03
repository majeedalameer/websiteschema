/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain.cluster;

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

    public void addSample(Sample sample) {
        throw new RuntimeException();
    }

    public void append(List<Cluster> array) {
        int pos = this.clusters.length;
        Cluster[] tmp = new Cluster[pos + array.size()];
        System.arraycopy(this.clusters, 0, tmp, 0, pos);
        for (int i = 0; i < array.size(); i++) {
            tmp[pos + i] = array.get(i);
        }
        this.clusters = tmp;
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
}
