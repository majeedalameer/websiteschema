/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.core;

import websiteschema.fb.core.ecc.ECC;
import websiteschema.fb.core.ecc.ExecutionControl;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.apache.log4j.Logger;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;

/**
 *
 * @author ray
 */
public class FunctionBlock {

    public final static int INIT = 0;
    public final static int STARTED = 1;
    public final static int STOPED = 2;
    public final Logger l = Logger.getLogger(FunctionBlock.class);
    int status;
    String name;
    public RuntimeContext context;
    boolean withECC = false;
    ECC ecc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RuntimeContext getContext() {
        return context;
    }

    public void setContext(RuntimeContext context) {
        this.context = context;
    }

    public final void executeEvent(String evt) {
        ExecutionControl ec = ecc.getExecutionControl(evt);
        String algorithm = ec.getAlgorithm();
        boolean success = execute(algorithm);
        if (success) {
            ecc.setStatus(ec.getSuccess());
        } else {
            ecc.setStatus(ec.getFailure());
        }

        String eo = ec.getEo();
        if (null != eo && !"".equals(eo)) {
            trigger(eo);
        }
    }

    public final boolean execute(String algorithm) {
        Method[] methods = this.getClass().getMethods();
        boolean ret = true;
        for (Method method : methods) {
            if (method.isAnnotationPresent(Algorithm.class)) {
                if (algorithm.equalsIgnoreCase(method.getAnnotation(Algorithm.class).name())) {
                    try {
                        method.invoke(this);
                    } catch (Exception ex) {
                        System.out.println(getName());
                        ex.printStackTrace();
                        l.error(ex);
                        ret = false;
                    }
                }
                break;
            }
        }
        return ret;
    }

    public boolean triggerEvent(String evt) {
        if (!withECC) {
            trigger(evt);
        }
        return true;
    }

    private void trigger(String evt) {
        l.trace(getName() + " trigger event: " + evt);
        sendRelativeData(evt, context);
        List<EventLink> evtLinks = context.getEventLinks(getName(), evt);
        if (null != evtLinks) {
            for (EventLink evtLink : evtLinks) {
                Event event = new Event(context.getFunctionBlockByName(evtLink.dest), evtLink.eventInput);
                context.addEvent(event);
            }
        }
    }

    /**
     * 发送相关的数据至各个功能块
     * @param fb
     * @param evt
     * @param context
     */
    private void sendRelativeData(String evt, RuntimeContext context) {
        List<String> dataOutputs = context.getFunctionBlockInfo(getClass()).eventRelativeData(evt);

        for (String dataOutput : dataOutputs) {
            List<DataLink> dataLinks = context.getDataLinks(getName(), dataOutput);
            for (DataLink dLink : dataLinks) {
                sendData(dLink, context);
            }
        }
    }

    /**
     * 发送数据到指定的功能块
     * @param dataLink
     * @param context
     */
    private void sendData(DataLink dataLink, RuntimeContext context) {
        Object dataOutput = getData(dataLink.src, dataLink.dataOutput, context);
        setData(dataLink.dest, dataLink.dataInput, dataOutput, context);
    }

    /**
     * 根据DO，取出指定的数据。
     * @param name
     * @param dataOutput
     * @param context
     * @return
     */
    private Object getData(String name, String dataOutput, RuntimeContext context) {
        Object obj = context.getFunctionBlockByName(name);
        if (null != obj) {
            Field[] fields = obj.getClass().getFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(DO.class)) {
                    String dataOutputName = field.getAnnotation(DO.class).name();
                    if (dataOutput.equalsIgnoreCase(dataOutputName)) {
                        try {
                            Object ret = field.get(obj);
                            return ret;
                        } catch (IllegalAccessException ex) {
                            l.error(ex);
                        } catch (IllegalArgumentException ex) {
                            l.error(ex);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 将数据写入到指定的功能块
     * @param name
     * @param dataInput
     * @param data
     * @param context
     */
    private void setData(String name, String dataInput, Object data, RuntimeContext context) {
        Object obj = context.getFunctionBlockByName(name);
        if (null != obj) {
            Field[] fields = obj.getClass().getFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(DI.class)) {
                    String dataInputName = field.getAnnotation(DI.class).name();
                    if (dataInput.equalsIgnoreCase(dataInputName)) {
                        try {
                            Class typo = field.getType();
                            if (typo.equals(data.getClass())) {
                                field.set(obj, data);
                            } else if (String.class.equals(typo)) {
                                field.set(obj, data.toString());
                            } else {
                                field.set(obj, data);
                            }
                            return;
                        } catch (IllegalAccessException ex) {
                            l.error(ex);
                        } catch (IllegalArgumentException ex) {
                            l.error(ex);
                        }
                    }
                }
            }
        }
    }
}
