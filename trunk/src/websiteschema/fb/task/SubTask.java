/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.task;

import java.util.*;
import websiteschema.fb.BasicFuncBlock;
import websiteschema.util.Pair;

/**
 * @author ray
 */
public class SubTask implements Runnable {

    String URL = null;
    EventFlow events = new EventFlow();
    DataFlow datas = new DataFlow();
    FBManager fbManager = new FBManager();

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public DataFlow getDatas() {
        return datas;
    }

    public EventFlow getEvents() {
        return events;
    }

    public FBManager getFbManager() {
        return fbManager;
    }

    public void StartTask(){
        Thread t = new Thread(this);
        t.start();
        fbManager.get("LAUNCHER").setTrigger("INIT");
    }

    public void run() {
        while(!fbManager.isAllFBStop()){
            Set<String> setFB = fbManager.keySet();
            for(String fbName : setFB){
                BasicFuncBlock fb = fbManager.get(fbName);
                fb.process();

                Map<String, Boolean> EO = fb.getEO();

                for(String event : EO.keySet()){
                    if(EO.get(event)){
                        Set<Pair<String,String>> dest = events.getDestination(fbName, event);
                        for(Pair<String,String> d : dest){
                            BasicFuncBlock dfb = fbManager.get(d.getKey());
                            dfb.setTrigger(d.getValue());
                        }
                        EO.put(event, Boolean.FALSE);

                        Set<Pair<String,String>> dataDest = datas.getDestination(fbName);
                        for(Pair<String,String> d : dataDest){
                            String [] src = d.getKey().split("\\.");
                            String [] dst = d.getValue().split("\\.");
                            BasicFuncBlock srcFB = fbManager.get(src[0]);
                            BasicFuncBlock dstFB = fbManager.get(dst[0]);
                            Object data = srcFB.getData(src[1]);
                            dstFB.setData(dst[1], data);
                        }
                    }
                }
            }
        }
    }
}
