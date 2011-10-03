/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain.cluster;

/**
 *
 * @author ray
 */
public class FeatureInfo {

    private String name;
    private int totalTimes;
    private int timesPerSample;
    private int relatedSample;
    private int weight;
    private double similarity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public int getTimesPerSample() {
        return timesPerSample;
    }

    public void setTimesPerSample(int timesPerSample) {
        this.timesPerSample = timesPerSample;
    }

    public int getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(int totalTimes) {
        this.totalTimes = totalTimes;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getRelatedSample() {
        return relatedSample;
    }

    public void setRelatedSample(int relatedSample) {
        this.relatedSample = relatedSample;
    }
}
