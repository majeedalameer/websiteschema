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
public class Launcher extends BasicFuncBlock {

    Integer v = 0;

    public Launcher() {
        super();
    }

    @Override
    public void process() {
        if (BasicFuncBlock.STATE_STOP != getStatus()) {
            setStatus(BasicFuncBlock.STATE_RUNNING);
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
            if (BasicFuncBlock.STATE_STOP != getStatus()) {
                setStatus(BasicFuncBlock.STATE_START);
            }
        }
    }

    private void INIT() {
        System.out.println("init launcher " + ++v);
        DO.put("OUT", v);
        EO.put("INIT", Boolean.TRUE);
    }

    private void STOP() {
        setStatus(BasicFuncBlock.STATE_STOP);
        System.out.println("stop launcher " + ++v);
        DO.put("OUT", v);
        EO.put("STOP", Boolean.TRUE);
    }
}
