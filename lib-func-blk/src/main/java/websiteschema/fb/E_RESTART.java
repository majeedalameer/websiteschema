/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb;

import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.Event;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EI(name = {"STOP:CEASE"})
@EO(name = {"COLD", "WARM", "STOP"})
public final class E_RESTART extends FunctionBlock {

    public final static String CEASE_COMMAND = "stop";

    @Algorithm(name = "CEASE")
    public void cease() {
        Event evt = new Event(null, CEASE_COMMAND);
        context.addEvent(evt);
    }
}
