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

    private String name = null;
    private int totalCount = 0;
    //Average frequence of each document
    private int frequence = 0;
    private int documentFrequence = 0;
    private int weight = 0;
    private double similarity = 0.0;

    public int getDocumentFrequence() {
        return documentFrequence;
    }

    public void setDocumentFrequence(int documentFrequence) {
        this.documentFrequence = documentFrequence;
    }

    public int getFrequence() {
        return frequence;
    }

    public void setFrequence(int frequence) {
        this.frequence = frequence;
    }

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

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
