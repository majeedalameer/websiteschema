/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.weka;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author ray
 */
public class WekaClassifier {

    public boolean category() {
        String model = null;
        Classifier c = WekaUtil.getWekaUtil().getClassifier(model);
        Instances dataSet = WekaUtil.getWekaUtil().getDataSet(model);
        FeatureWrapFactory fwf = new FeatureWrapFactory();
        fwf.setDataSet(dataSet);

        try {
            Instance feature = fwf.wrap(new double[1]);
            if (null != feature) {
                double res = c.classifyInstance(feature);
                int belong = (int) res;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;
    }
}
