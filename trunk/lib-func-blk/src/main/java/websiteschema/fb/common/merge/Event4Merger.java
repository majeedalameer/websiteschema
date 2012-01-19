/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.common.merge;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author st
 */
@EI(name  = {"E1:M","E2:M","E3:M","E4:M"})
@EO(name = {"EO"})
public class Event4Merger extends FunctionBlock{
    @Algorithm(name = "M")
    public void Merge4(){
        triggerEvent("EO");
    }
}
