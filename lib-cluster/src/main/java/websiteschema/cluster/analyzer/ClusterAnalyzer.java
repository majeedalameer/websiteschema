/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.cluster.analyzer;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.model.domain.cluster.Sample;

/**
 *
 * @author ray
 */
public interface ClusterAnalyzer {

    public Map<String, String> analysis(Map<String, String> old, ClusterModel cm, List<Sample> samples);

}
