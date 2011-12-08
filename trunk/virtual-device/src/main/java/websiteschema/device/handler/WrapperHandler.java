/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device.handler;

import websiteschema.model.domain.Wrapper;
import websiteschema.persistence.rdbms.WrapperMapper;
import static websiteschema.device.DeviceContext.*;

/**
 * 负责缓存Wrapper
 * @author ray
 */
public class WrapperHandler {

    private static WrapperHandler ins = new WrapperHandler();
    private WrapperMapper wrapperMapper = getSpringContext().getBean("wrapperMapper", WrapperMapper.class);

    public static WrapperHandler getInstance() {
        return ins;
    }

    public Wrapper getWrapper(long wrapperId) {
        return wrapperMapper.getById(wrapperId);
    }

}
