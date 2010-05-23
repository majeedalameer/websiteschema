/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb;

import java.util.Set;

/**
 *
 * @author ray
 */
public class Test extends BasicFuncBlock {

    Integer v = 10;
    @Override
    public void process() {
        if (BasicFuncBlock.STATE_STOP != getStatus()) {
            Set<String> eiNames = EI.keySet();
            for (String eventName : eiNames) {
                Boolean run = EI.get(eventName);
                if (run) {
                    if ("INIT".equals(eventName)) {
                        INIT();
                    } else if ("STOP".equals(eventName)) {
                        STOP();
                    }
                    EI.put(eventName, Boolean.FALSE);
                }
            }
        }
    }

    private void INIT() {
        if(DI.containsKey("IN")){
            v = (Integer) DI.get("IN");
        }
        System.out.println("init test " + v);
        EO.put("INIT", Boolean.TRUE);
    }

    private void STOP() {
        setStatus(BasicFuncBlock.STATE_STOP);
        if(DI.containsKey("IN")){
            v = (Integer) DI.get("IN");
        }
        System.out.println("stop test " + v);
        EO.put("STOP", Boolean.TRUE);
    }
}
