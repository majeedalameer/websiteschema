/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase.core;

import websiteschema.persistence.hbase.annotation.ColumnFamily;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import websiteschema.model.domain.HBaseBean;

/**
 * Only support String, Date, double, float, long, int, boolean.
 * @author ray
 */
public class JavaBeanWrapper {

    private static JavaBeanWrapper ins = new JavaBeanWrapper();

    public static JavaBeanWrapper getInstance() {
        return ins;
    }

    public <T extends HBaseBean> T getBean(Result obj, Class<T> clazz) {
        try {
            T ret = null;
            if (null != obj && !obj.isEmpty()) {
                ret = clazz.newInstance();
                String rowKey = new String(obj.getRow());
                ret.setRowKey(rowKey);
                for (KeyValue kv : obj.raw()) {
                    String family = new String(kv.getFamily());
                    String value = new String(kv.getValue());
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field field : fields) {
                        set(clazz, ret, field, family, value);
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
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public <T extends HBaseBean> T getBean(Map<String, String> obj, Class<T> clazz) {
        try {
            T ret = null;
            if (!obj.isEmpty()) {
                ret = clazz.newInstance();
                for (String family : obj.keySet()) {
                    String value = obj.get(family);
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field field : fields) {
                        set(clazz, ret, field, family, value);
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
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void set(Class clazz, Object obj, Field field, String family, String value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        String fieldName = field.getName().toLowerCase();
        Class arg = field.getType();
        if (field.isAnnotationPresent(ColumnFamily.class)) {
            if (fieldName.equals(family.toLowerCase())) {
                String setterName = "set" + String.valueOf(family.charAt(0)).toUpperCase() + family.substring(1);
                if (String.class.equals(arg)) {
                    Method method = clazz.getMethod(setterName, String.class);
                    method.invoke(obj, value);
                } else if (double.class.equals(arg)) {
                    Method method = clazz.getMethod(setterName, double.class);
                    method.invoke(obj, Double.valueOf(value));
                } else if (float.class.equals(arg)) {
                    Method method = clazz.getMethod(setterName, float.class);
                    method.invoke(obj, Float.valueOf(value));
                } else if (int.class.equals(arg)) {
                    Method method = clazz.getMethod(setterName, int.class);
                    method.invoke(obj, Integer.valueOf(value));
                } else if (long.class.equals(arg)) {
                    Method method = clazz.getMethod(setterName, long.class);
                    method.invoke(obj, Long.valueOf(value));
                } else if (boolean.class.equals(arg)) {
                    Method method = clazz.getMethod(setterName, boolean.class);
                    method.invoke(obj, Boolean.valueOf(value));
                } else {
                    Method method = clazz.getMethod(setterName, arg);
                    if (!arg.isArray()) {
                        Object param = JSONObject.toBean(JSONObject.fromObject(value), arg);
                        method.invoke(obj, param);
                    } else {
                        Object param = JSONArray.toArray(JSONArray.fromObject(value), arg.getComponentType());
                        method.invoke(obj, param);
                    }
                }
            }
        }
    }

    public <T extends HBaseBean> Map<String, String> getMap(T obj, Class<T> clazz) {
        try {
            Map<String, String> ret = new HashMap<String, String>();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ColumnFamily.class)) {
                    String fieldName = field.getName();
                    Class typo = field.getType();
                    String value = "";
                    String getterName = "get" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
                    if (String.class.equals(typo)) {
                        Method method = clazz.getMethod(getterName);
                        value = (String) method.invoke(obj);
                    } else if (long.class.equals(typo)) {
                        Method method = clazz.getMethod(getterName);
                        value = String.valueOf(method.invoke(obj));
                    } else if (int.class.equals(typo)) {
                        Method method = clazz.getMethod(getterName);
                        value = String.valueOf(method.invoke(obj));
                    } else if (double.class.equals(typo)) {
                        Method method = clazz.getMethod(getterName);
                        value = String.valueOf(method.invoke(obj));
                    } else if (float.class.equals(typo)) {
                        Method method = clazz.getMethod(getterName);
                        value = String.valueOf(method.invoke(obj));
                    } else if (boolean.class.equals(typo)) {
                        getterName = getterName.replaceAll("^get", "is");
                        Method method = clazz.getMethod(getterName);
                        value = String.valueOf(method.invoke(obj));
                    } else {
                        Method method = clazz.getMethod(getterName);
                        Class retType = method.getReturnType();
                        if (!retType.isArray()) {
                            value = JSONObject.fromObject(method.invoke(obj)).toString();
                        } else {
                            value = JSONArray.fromObject(method.invoke(obj)).toString();
                        }
                    }
                    if (null != value) {
                        ret.put(fieldName, value);
                    }
                }
            }
            return ret;
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
