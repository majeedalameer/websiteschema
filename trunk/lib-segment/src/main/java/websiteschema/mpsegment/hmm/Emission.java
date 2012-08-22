/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.hmm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ray
 */
public final class Emission<T> {

    //observe -> states
    private Map<Integer, Map<Integer, Double>> matrix = new HashMap<Integer, Map<Integer, Double>>(5);
    private int total = 0;

    public Emission() {
    }

    public Emission(Map<Integer, Map<Integer, Integer>> emisMatrix) {
        //state -> observes
        Set<Integer> states = emisMatrix.keySet();
        for (Integer state : states) {
            Map<Integer, Integer> mapO = emisMatrix.get(state);
            int sum = 1;
            Set<Integer> observes = mapO.keySet();
            for (Integer o : observes) {
                sum += mapO.get(o);
            }

            for (Integer o : observes) {
                double prob = (double) mapO.get(o) / (double) sum;
                setProb(o, state, prob);
            }
            total += sum;
        }
    }

    public double getProb(int o, int s) {
        Map<Integer, Double> e = matrix.get(o);
        if (null != e) {
            if (e.containsKey(s)) {
                return e.get(s);
            }
        }

        return 1.0 / (double) total;
    }

    public Set<Integer> getStateProbByObserve(int index) {
        Map<Integer, Double> map = matrix.get(index);
        return null != map ? map.keySet() : null;
    }

    public void setProb(int o, int s, double prob) {
        Map<Integer, Double> map = matrix.get(o);
        if (null == map) {
            map = new HashMap<Integer, Double>(1);
            matrix.put(o, map);
        }
        map.put(s, prob);
    }
}
