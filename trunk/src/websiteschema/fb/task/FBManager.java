/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.fb.task;

import java.util.*;
import websiteschema.fb.BasicFuncBlock;

/**
 *
 * @author ray
 */
public class FBManager {

    private Map<String, BasicFuncBlock> mapFB = new HashMap<String, BasicFuncBlock>();
    public void put(String name, BasicFuncBlock fb){
        mapFB.put(name, fb);
    }

    public BasicFuncBlock get(String name){
        return mapFB.get(name);
    }

    public boolean isAllFBStop(){
        Set<String> fbNames = mapFB.keySet();
        boolean ret = true;
        for(String fbName : fbNames){
            BasicFuncBlock fb = mapFB.get(fbName);
            if(fb.getStatus() != BasicFuncBlock.STATE_STOP){
                ret = false;
                break;
            }
        }
        return ret;
    }

    public Set<String> keySet(){
        return mapFB.keySet();
    }
}
