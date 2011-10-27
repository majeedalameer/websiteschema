/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 *
 * @author ray
 */
public class Configure {

    private static final String DefaultField = "default";
    private static final Configure root = new Configure();
    private String currentField = DefaultField;
    private String namespace = "";
    private Map<String, Map<String, String>> properties = new HashMap<String, Map<String, String>>();

    private Configure() {
        try {
            loadProperty("configure-default.ini");
        } catch (IOException ex) {
            System.out.println("Can not load configuration configure-default.ini");
        }
    }

    public Configure(String file) {
        properties = root.cloneToNewMap();
        try {
            loadProperty(file);
        } catch (IOException ex) {
            System.out.println("Can not load configuration " + file);
        }
    }

    public Configure(InputStream config) {
        properties = root.cloneToNewMap();
        try {
            loadProperty(config);
        } catch (IOException ex) {
            System.out.println("Can not load configuration");
        }
    }

    public static Configure getDefaultConfigure() {
        return root;
    }

    public static Configure createConfigure(String config) throws UnsupportedEncodingException {
        return new Configure(new ByteArrayInputStream(config.getBytes("UTF-8")));
    }

    private Map<String, Map<String, String>> cloneToNewMap() {
        Map<String, Map<String, String>> clone = new HashMap<String, Map<String, String>>();
        for (Iterator it = properties.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Map<String, String>> e = (Map.Entry<String, Map<String, String>>) it.next();
            clone.put(e.getKey(), (Map<String, String>) ((HashMap) e.getValue()).clone());
        }
        return clone;
    }

    private void loadProperty(String file) throws IOException {
        InputStream is = null;
        File f = new File(file);
        try {
            if (f.exists()) {
                is = new FileInputStream(f);
            } else {
                is = Configure.class.getClassLoader().getResourceAsStream(file);
            }
            loadProperty(is);
        } finally {
            if (null != is) {
                is.close();
            }
        }
    }

    private void loadProperty(InputStream is) throws IOException {
        if (null != is) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int count = 0;
                String line = br.readLine();
                while (null != line) {
                    if (!"".equals(line)) {
                        parseLine(line, ++count);
                    }
                    line = br.readLine();
                }
            } catch (IOException ex) {
            } finally {
                if (null != br) {
                    br.close();
                }
            }
        }
    }

    /**
     * Read multi-lines configuration.
     * @param br
     * @return
     */
    private String readLine(BufferedReader br) {
        throw new java.lang.UnsupportedOperationException();
    }

    private void parseLine(String line, int currentRowNumber) {
        if (!line.startsWith("#") && !line.startsWith(";")) {
            if (line.startsWith("[") && line.endsWith("]")) {
                currentField = line.substring(1, line.length() - 1).trim().toLowerCase();
            } else if (line.indexOf("=") > 0) {
                int eq = line.indexOf("=");
                String key = line.substring(0, eq).trim().toLowerCase();
                String value = line.substring(eq + 1, line.length());
                if ("namespace".equals(key) && Configure.DefaultField.equals(currentField)) {
                    if (1 == currentRowNumber) {
                        this.namespace = value;
                    } else {
                        throw new RuntimeException("'namespace' must be declared at first line.");
                    }
                } else {
                    putProperties(
                            currentField, key.trim(), value.trim());
                }
            }
        }
    }

    public void putProperties(String key, String value) {
        putProperties(DefaultField, key, value);
    }

    public void putProperties(String field, String key, String value) {
        if (null == field) {
            throw new RuntimeException("Field is null.");
        }
        String f = field.toLowerCase();
        if (!DefaultField.equals(f)) {
            f = getNamespace() + f;
        }
        Map<String, String> map = properties.get(f);
        if (null == map) {
            map = new HashMap<String, String>();
            properties.put(f, map);
        }
        String k = key.toLowerCase();
        map.put(k, value);
    }

    private String getNamespace() {
        if ("".equals(namespace)) {
            return "";
        } else {
            return namespace + ".";
        }
    }

    public String getProperty(String key) {
        return getProperty(DefaultField, key, null);
    }

    public String getProperty(String field, String key) {
        return getProperty(field, key, null);
    }

    public String getProperty(String field, String key, String def) {
        if (null == key || "".equals(key)) {
            return def;
        }
        Map<String, String> map = null;
        String f = field.toLowerCase();
        if (DefaultField.equals(f)) {
            map = properties.get(f);
        } else {
            map = properties.get(getNamespace() + f);
        }
        if (null != map) {
            String ret = map.get(key.toLowerCase());
            if (null != ret) {
                return ret;
            }
        }
        return def;
    }

    public List<String> getListProperty(String key) {
        return getListProperty(DefaultField, key);
    }

    public List<String> getListProperty(String field, String key) {
        String value = getProperty(field, key);
        if (null != value) {
            return JSONArray.fromObject(value);
        } else {
            return null;
        }
    }

    public Set<String> getSetProperty(String key) {
        return getSetProperty(DefaultField, key);
    }

    public Set<String> getSetProperty(String field, String key) {
        String value = getProperty(field, key);
        if (null != value) {
            return new HashSet<String>(getListProperty(field, key));
        } else {
            return null;
        }
    }

    public Map<String, String> getMapProperty(String key) {
        return getMapProperty(DefaultField, key);
    }

    public Map<String, String> getMapProperty(String field, String key) {
        String value = getProperty(field, key);
        if (null != value) {
            JSONObject json = JSONObject.fromObject(value);
            return json;
        } else {
            return null;
        }
    }

    public int getIntProperty(String key) {
        return getIntProperty(DefaultField, key, 0);
    }

    public int getIntProperty(String field, String key) {
        return getIntProperty(field, key, 0);
    }

    public int getIntProperty(String field, String key, int def) {
        if (null == key || "".equals(key)) {
            return def;
        }
        String value = getProperty(field, key);
        if (null != value) {
            return Integer.valueOf(value);
        }
        return def;
    }

    public double getDoubleProperty(String key) {
        return getDoubleProperty(DefaultField, key, 0.0);
    }

    public double getDoubleProperty(String field, String key) {
        return getDoubleProperty(field, key, 0.0);
    }

    public double getDoubleProperty(String field, String key, double def) {
        if (null == key || "".equals(key)) {
            return def;
        }
        String value = getProperty(field, key);
        if (null != value) {
            return Double.valueOf(value);
        }
        return def;
    }

    public boolean getBooleanProperty(String key) {
        return getBooleanProperty(DefaultField, key, false);
    }

    public boolean getBooleanProperty(String field, String key) {
        return getBooleanProperty(field, key, false);
    }

    public boolean getBooleanProperty(String field, String key, boolean def) {
        if (null == key || "".equals(key)) {
            return def;
        }
        String value = getProperty(field, key);
        if (null != value) {
            return Boolean.valueOf(value);
        }
        return def;
    }

    public <T> T getBean(String field, String key, Class<T> clazz) {
        String jsonText = getProperty(field, key);
        if (null != jsonText) {
            try {
                return (T) JSONObject.toBean(JSONObject.fromObject(jsonText), clazz);
            } catch (JSONException ex) {
                if (int.class.equals(clazz) || Integer.class.equals(clazz)) {
                    T ret = (T) Integer.valueOf(getIntProperty(field, key));
                    return ret;
                } else if (double.class.equals(clazz) || Double.class.equals(clazz)) {
                    T ret = (T) Double.valueOf(getDoubleProperty(field, key));
                    return ret;
                } else if (float.class.equals(clazz) || Float.class.equals(clazz)) {
                    T ret = (T) new Float(getDoubleProperty(field, key));
                    return ret;
                } else if (boolean.class.equals(clazz) || Boolean.class.equals(clazz)) {
                    T ret = (T) Boolean.valueOf(getBooleanProperty(field, key));
                    return ret;
                } else if (String.class.equals(clazz)) {
                    T ret = (T) getProperty(field, key, null);
                    return ret;
                }
            }
        }
        return null;
    }
}
