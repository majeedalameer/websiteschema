/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain.cluster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import websiteschema.model.domain.HBaseBean;
import websiteschema.persistence.hbase.annotation.ColumnFamily;
import websiteschema.persistence.hbase.annotation.RowKey;

/**
 *
 * @author ray
 */
public class ClusterModel implements HBaseBean {

    @RowKey
    String rowKey;
    @ColumnFamily
    Cluster[] clusters = null;
    @ColumnFamily
    FeatureStatInfo statInfo = null;
    @ColumnFamily
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

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public void delete(String cp) {
        if (null != clusters) {
            List<Cluster> list = Arrays.asList(clusters);
            int index = -1;
            for (int i = 0; i < list.size(); i++) {
                Cluster c = list.get(i);
                if (cp.equals(c.getCentralPoint().getName())) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                list.remove(index);
                clusters = list.toArray(new Cluster[0]);
            }
        }
    }
}
