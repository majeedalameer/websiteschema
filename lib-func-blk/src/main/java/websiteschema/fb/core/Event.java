/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.fb.core;


/**
 *
 * @author ray
 */
public class Event {

    public FunctionBlock fb;
    public String ei;

    public Event(){
        fb = null;
        ei = null;
    }

    public Event(FunctionBlock fb, String ei){
        this.fb = fb;
        this.ei = ei;
    }

}
