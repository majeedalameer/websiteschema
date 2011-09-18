/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import websiteschema.model.domain.HBaseBean;

/**
 *
 * @author ray
 */
public class JavaBeanWrapper {

    public <T extends HBaseBean> T getBean(Result obj, Class<T> clazz) {
        try {
            T ret = clazz.newInstance();
            if (!obj.isEmpty()) {
                for (KeyValue kv : obj.raw()) {
                    String rowKey = new String(kv.getRow());
                    String family = new String(kv.getFamily());
                    String value = new String(kv.getValue());
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods) {
                        String methodName = method.getName().toLowerCase();
                        Class[] args = method.getParameterTypes();
                        if (1 == args.length) {
                            //只有一个参数
                            if (methodName.contains("set")) {
                                //是一个Setter方法
                                Class arg = args[0];
                                if (methodName.contains(family.toLowerCase())
                                        && String.class.equals(arg)) {
                                    method.invoke(ret, value);
                                }
                            } else if (methodName.contains("rowkey")) {
                                method.invoke(ret, rowKey);
                            }
                        }
                    }
                }
            }
            return ret;
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public <T extends HBaseBean> T getBean(Map<String, String> obj, Class<T> clazz) {
        try {
            T ret = clazz.newInstance();
            if (!obj.isEmpty()) {
                for (String key : obj.keySet()) {
                    String value = obj.get(key);
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods) {
                        String methodName = method.getName().toLowerCase();
                        Class[] args = method.getParameterTypes();
                        if (1 == args.length) {
                            //只有一个参数
                            if (methodName.contains("set")) {
                                //是一个Setter方法
                                Class arg = args[0];
                                if (methodName.contains(key.toLowerCase())
                                        && String.class.equals(arg)) {
                                    method.invoke(obj, value);
                                }
                            }
                        }
                    }
                }
            }
            return ret;
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public <T extends HBaseBean> Map<String, String> getMap(T obj, Class<T> clazz) {
        try {
            Map<String, String> ret = new HashMap<String, String>();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                String methodName = method.getName().toLowerCase();
                Class arg = method.getReturnType();

                if (methodName.startsWith("get") && methodName.length() > 3) {
                    //是一个Getter方法
                    if (String.class.equals(arg)) {
                        String value = method.invoke(obj).toString();
                        ret.put(methodName.substring(3), value);
                    }
                }
            }
            return ret;
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
