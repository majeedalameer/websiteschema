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
public class Cluster {

    String rowKey;
    List<String> samples;
    DocVector centralPoint;
    double threshold;
    String siteId;

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public DocVector getCentralPoint() {
        return centralPoint;
    }

    public void setCentralPoint(DocVector centralPoint) {
        this.centralPoint = centralPoint;
    }

    public List<String> getSamples() {
        return samples;
    }

    public void setSamples(List<String> samples) {
        this.samples = samples;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
