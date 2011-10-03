/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.core;

import org.apache.log4j.Logger;
import websiteschema.fb.CEASE;
import websiteschema.utils.Configure;

/**
 *
 * @author ray
 */
public class Application implements Runnable {

    Logger l = Logger.getLogger(Application.class);
    RuntimeContext context = new RuntimeContext();
    private boolean running = true;

    public void stop() {
        running = false;
    }

    public RuntimeContext getContext() {
        return context;
    }

    public void run() {

        Configure config = context.getConfig();
        String initEvent = config.getProperty("InitEvent");
        FunctionBlock start = context.getStartFB();
        start.triggerEvent(initEvent);

        while (running) {

            Event evt = context.getEventQueue().poll();
            if (null != evt) {
                FunctionBlock fb = evt.fb;
                String ei = evt.ei;
                if (null == fb && ei.equals(CEASE.CEASE_COMMAND)) {
                    stop();
                } else {
                    if (!fb.withECC) {
                        Class clazz = fb.getClass();
                        FBInfo fbInfo = context.getFunctionBlockInfo(clazz);
                        String algorithm = fbInfo.getEIRelatedAlgorithm(ei);
                        fb.execute(algorithm);
                    }
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    l.error(ex);
                }
            }
        }
    }
}
