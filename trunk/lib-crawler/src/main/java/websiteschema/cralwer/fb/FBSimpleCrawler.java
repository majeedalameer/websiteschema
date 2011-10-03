/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cralwer.fb;

import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EO(name = {"SUCCESS", "FAILURE"})
@EI(name = {"INIT:INIT"})
public class FBSimpleCrawler extends FunctionBlock {
}
