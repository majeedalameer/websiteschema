/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb;

import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import static websiteschema.fb.core.Event.*;

/**
 *
 * @author ray
 */
@EI(name = {"STOP:CEASE"})
@EO(name = {"COLD", "WARM", "STOP"})
public final class E_RESTART extends FunctionBlock {

    @Algorithm(name = "CEASE")
    public void cease() {
        context.addEvent(CeaseEvent());
    }
}
