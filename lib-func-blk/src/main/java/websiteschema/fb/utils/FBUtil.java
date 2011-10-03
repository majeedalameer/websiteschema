/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.DataLink;
import websiteschema.fb.core.EventLink;
import websiteschema.fb.core.FBInfo;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.fb.core.RuntimeContext;

/**
 *
 * @author ray
 */
public class FBUtil {

    Logger l = Logger.getLogger(FBUtil.class);
    static final FBUtil ins = new FBUtil();

    public static FBUtil getInstance() {
        return ins;
    }

    public FunctionBlock loadFunctionBlock(String fbName, RuntimeContext context) {
        String fbType = context.getConfig().getProperty(fbName, "FBType");
        try {
            Class clazz = Class.forName(fbType);
            FBInfo info = new FBInfo(clazz);
            context.addFunctionBlockInfo(clazz, info);
            FunctionBlock fb = (FunctionBlock) clazz.newInstance();
            fb.setName(fbName);
            fb.setContext(context);

            loadDefaultParameters(fb, clazz, info, context);
            loadEventLinks(fb, clazz, info, context);
            loadDataLinks(fb, clazz, info, context);
            return fb;
        } catch (ClassNotFoundException ex) {
            l.error(ex);
        } catch (InstantiationException ex) {
            l.error(ex);
        } catch (IllegalAccessException ex) {
            l.error(ex);
        }
        return null;
    }

    private void loadDefaultParameters(FunctionBlock fb, Class clazz, FBInfo info, RuntimeContext context) throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(DI.class)) {
                DI _di = field.getAnnotation(DI.class);
                Class type = field.getType();
                String paramName = "DI." + _di.name();
                Object defaultData = context.getConfig().getBean(fb.getName(), paramName, type);
                if (null != defaultData) {
                    field.set(fb, defaultData);
                }
            }
        }
    }

    private void loadEventLinks(FunctionBlock fb, Class clazz, FBInfo info, RuntimeContext context) {
        if (clazz.isAnnotationPresent(EO.class)) {
            EO _eo = (EO) clazz.getAnnotation(EO.class);
            String[] eventOutputs = _eo.name();
            for (String eo : eventOutputs) {
                String key = "EO." + eo;
                Map<String, String> links = context.getConfig().getMapProperty(fb.getName(), key);
                if (null != links) {
                    for (String dest : links.keySet()) {
                        String ei = links.get(dest);
                        if (null != ei) {
                            EventLink elink = new EventLink();
                            elink.src = fb.getName();
                            elink.dest = dest;
                            elink.eventOutput = eo;
                            elink.eventInput = ei;
                            context.addEventLink(elink);
                        }
                    }
                }
            }
        }
    }

    private void loadDataLinks(FunctionBlock fb, Class clazz, FBInfo info, RuntimeContext context) {
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(DO.class)) {
                DO _do = field.getAnnotation(DO.class);
                String key = "DO." + _do.name();
                Map<String, String> links = context.getConfig().getMapProperty(fb.getName(), key);
                if (null != links) {
                    for (String dest : links.keySet()) {
                        String di = links.get(dest);
                        if (null != di) {
                            DataLink dlink = new DataLink();
                            dlink.src = fb.getName();
                            dlink.dataOutput = _do.name();
                            dlink.dest = dest;
                            dlink.dataInput = di;
                            context.addDataLink(dlink);
                        }
                    }
                }
            }
        }
    }
}
