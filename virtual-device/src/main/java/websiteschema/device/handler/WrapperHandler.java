/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device.handler;

import java.io.InputStream;
import websiteschema.model.domain.Wrapper;

/**
 * 负责缓存Wrapper
 * @author ray
 */
public class WrapperHandler {

    private static WrapperHandler ins = new WrapperHandler();

    public static WrapperHandler getInstance() {
        return ins;
    }

    public Wrapper getWrapper(long wrapperId) {
        return null;
    }

}
