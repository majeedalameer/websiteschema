/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.fb.task;

import java.util.*;
import websiteschema.util.Pair;

/**
 *
 * @author ray
 */
public class DataFlow {

    /**
     * <K,V>, K is data event, V is destination data.
     */
    private List<Pair<String, String>> dataConnection = new ArrayList<Pair<String, String>>();

    /**
     * find a set of destination event
     * @param fb
     * @param dataOutput
     * @return Pair<K,V> K is the source, V is the destination.
     */
    public Set<Pair<String, String>> getDestination(String fb) {
        Set<Pair<String, String>> ret = new HashSet<Pair<String, String>>();
        String key = fb + ".";
        for(Pair<String,String> pair : dataConnection){
            if(pair.getKey().startsWith(key)){
                ret.add(pair);
            }
        }
        return ret;
    }

    public void addDataConnection(String eo, String ei){
        dataConnection.add(new Pair<String,String>(eo, ei));
    }
}
