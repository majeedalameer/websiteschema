/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb;

import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EI(name = {"COMP:COMP"})
@EO(name = {"GT", "LT", "EQ"})
public class IF_GREAT extends FunctionBlock {

    @DI(name = "R")
    public int r = 0;
    @DI(name = "L")
    public int l = 0;

    @Algorithm(name = "COMP")
    public void compare() {
        if (l > r) {
            triggerEvent("GT");
        } else if (l == r) {
            triggerEvent("EQ");
        } else {
            triggerEvent("LT");
        }
    }
}
