/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.common.merge;

import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author mgd
 */
@EI(name = {"E1", "E2", "E3"})
@EO(name = {"CEASE"})
public class Event3Merger extends FunctionBlock {

    @Algorithm(name = "MERGE_3E")
    public void merge3e() {
        triggerEvent("CEASE");
    }
}
