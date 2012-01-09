/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.weka;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author ray
 */
public class FeatureWrapFactory {

    private Instances dataSet = null;

    public void setDataSet(Instances dataSet) {
        this.dataSet = dataSet;
    }

    public Instance wrap(double[] vector) {
        Instance ret = null;//new DenseInstance();

        int size = vector.length;
        ret = new DenseInstance(size + 1);
        ret.setDataset(dataSet);
        for (int i = 0; i < size; i++) {
            ret.setValue(i, vector[i]);
        }

        return ret;
    }

}
