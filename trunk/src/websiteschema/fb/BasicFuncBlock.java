/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb;

import java.util.*;
import websiteschema.fb.task.DataFlow;
import websiteschema.fb.task.EventFlow;

/**
 *
 * @author ray
 */
public abstract class BasicFuncBlock {

    public static final int STATE_START = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_STOP = 2;
    private int status = STATE_START;
    Map<String, Boolean> EI = null;
    Map<String, Boolean> EO = null;
    Map<String, Object> DI = null;
    Map<String, Object> DO = null;
    
    public BasicFuncBlock() {
        EI = new HashMap<String, Boolean>();
        EO = new HashMap<String, Boolean>();
        DI = new HashMap<String, Object>();
        DO = new HashMap<String, Object>();
    }
    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(String name, Object data) {
        DI.put(name, data);
    }

    public Object getData(String name) {
        return DO.get(name);
    }

    public void setTrigger(String name) {
        EI.put(name, Boolean.TRUE);
    }

    public Map<String, Boolean> getEO(){
        return EO;
    }

    public abstract void process();
}
