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
public class EventFlow {

    /**
     * <K,V>, K is source event, V is destination event.
     */
    private List<Pair<String, String>> eventConnection = new ArrayList<Pair<String, String>>();

    /**
     * find a set of destination event
     * @param fb
     * @param event
     * @return Pair<K,V> K is the name of Function Block, V is the name of Event
     */
    public Set<Pair<String, String>> getDestination(String fb, String event) {
        Set<Pair<String, String>> ret = new HashSet<Pair<String, String>>();
        String key = fb + "." + event;
        for(Pair<String,String> pair : eventConnection){
            if(key.equals(pair.getKey())){
                String [] dest = pair.getValue().split("\\.");
                ret.add(new Pair<String,String>(dest[0], dest[1]));
            }
        }
        return ret;
    }

    public void addEventConnection(String eo, String ei){
        eventConnection.add(new Pair<String,String>(eo, ei));
    }
}
