/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.conf;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private String namespace = null;
    private Map<String, Map<String, String>> properties = new HashMap<String, Map<String, String>>();

    private Configure() {
        try {
            loadProperty("configure-default.ini");
        } catch (IOException ex) {
            System.out.println("Can not load configuration configure-default.ini");
        }
    }

    public Configure(String file) {
        this(file, null);
    }

    public Configure(String file, Map<String, String> prop) {
        properties = root.cloneToNewMap();
        try {
            loadProperty(file, prop);
        } catch (IOException ex) {
            System.out.println("Can not load configuration " + file);
        }
    }

    public Configure(InputStream config) {
        this(config, null);
    }

    public Configure(InputStream config, Map<String, String> prop) {
        properties = root.cloneToNewMap();
        try {
            loadProperty(config, prop);
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
        loadProperty(file, null);
    }

    private void loadProperty(String file, Map<String, String> prop) throws IOException {
        InputStream is = null;
        File f = new File(file);
        try {
            if (f.exists()) {
                is = new FileInputStream(f);
            } else {
                is = Configure.class.getClassLoader().getResourceAsStream(file);
            }
            loadProperty(is, prop);
        } finally {
            if (null != is) {
                is.close();
            }
        }
    }

    private void loadProperty(InputStream is, Map<String, String> prop) throws IOException {
        if (null != is) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int count = 0;
                String line = readLine(br);
                while (null != line) {
                    if (!"".equals(line)) {
                        if (null != prop) {
                            parseLine(line, ++count, prop);
                        } else {
                            parseLine(line, ++count, null);
                        }
                    }
                    line = readLine(br);
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
    private String readLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (!isComments(line) && !isField(line)) {
            if (isMultiLine(line)) {
                String multiLine = readMultiLine(line, br);
                if (null != multiLine) {
                    return multiLine;
                }
            }
        }
        return line;
    }

    private String readMultiLine(String firstLine, BufferedReader br) throws IOException {
        String first = firstLine.trim();
        if (first.endsWith("'''")) {
            return first;
        }
        br.mark(100000);
        StringBuilder sb = new StringBuilder();
        sb.append(firstLine).append("\n");
        String line = br.readLine();
        boolean end = false;
        while (null != line) {
            if (line.trim().endsWith("'''")) {
                sb.append(line.trim());
                end = true;
                break;
            } else {
                sb.append(line).append("\n");
            }
            line = br.readLine();
        }
        if (end) {
            return sb.toString();
        } else {
            br.reset();
        }
        return null;
    }

    private boolean isMultiLine(String line) {
        if (null != line) {
            int i = line.indexOf('=');
            if (i > 0) {
                String value = line.substring(i + 1).trim();
                return value.startsWith("'''");
            }
        }
        return false;
    }

    private boolean isComments(String line) {
        if (null != line) {
            return line.startsWith("#") || line.startsWith(";");
        } else {
            return false;
        }
    }

    private boolean isField(String line) {
        if (null != line) {
            return line.startsWith("[") && line.endsWith("]");
        } else {
            return false;
        }
    }
    private final static Pattern pat = Pattern.compile("(.*?)\\$\\{(.*?)\\}(.*)");

    private void parseLine(String line, int currentRowNumber, Map<String, String> prop) {
        if (!isComments(line)) {
            if (isField(line)) {
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
                    value = value.trim();
                    if (value.startsWith("'''") && value.endsWith("'''")) {
                        try {
                            value = value.substring(3, value.length() - 3);
                        } catch (Exception ex) {
                        }
                    }
                    // 从预先设定的prop中寻找是否需要替换。
                    // 如果在prop有：abc = xyz
                    // 如果value = ${abc}
                    // 则value会被替换成xyz
                    if (null != prop) {
                        Matcher m = pat.matcher(value.trim());
                        if (m.matches()) {
                            String k = m.group(2);
                            if (prop.containsKey(k)) {
                                String prefix = m.group(1);
                                String suffix = m.group(3);
                                value = prefix + prop.get(k) + suffix;
                            }
                        }
                    }
                    putProperties(currentField, key.trim(), value);
                }
            }
        }
    }

    public void putProperties(String key, String value) {
        putProperties(DefaultField, key, value);
    }

    public void putProperties(String field, String key, String value) {
        putProperties(getNamespace(), field, key, value);
    }

    public void putProperties(String ns, String field, String key, String value) {
        if (null == field) {
            throw new RuntimeException("Field is null.");
        }
        String f = field.toLowerCase();
        if (null != ns) {
            f = ns + "." + f;
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
            return this.namespace;
        }
    }

    /**
     * 获取所有域的集合
     * @return
     */
    public Set<String> getAllFields() {
        return properties.keySet();
    }

    /**
     * 获取指定域下的所有配置
     * @param field
     * @return
     */
    public Map<String, String> getAllPropertiesInField(String field) {
        return properties.get(field);
    }

    /**
     * 获取默认域下的指定配置
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return getProperty(DefaultField, key, null);
    }

    /**
     * 获取特定域下的指定配置
     * @param field
     * @param key
     * @return
     */
    public String getProperty(String field, String key) {
        return getProperty(field, key, null);
    }

    /**
     * 获取特定域下的指定配置，如果配置无效就返回用户设定的默认值
     * @param field
     * @param key
     * @param def
     * @return
     */
    public String getProperty(String field, String key, String def) {
        return getProperty(getNamespace(), field, key, def);
    }

    /**
     * 获取特定域下的指定配置，如果配置无效就返回用户设定的默认值，<br/>
     * 同时指定域的命名空间。
     * @param ns
     * @param field
     * @param key
     * @param def
     * @return
     */
    public String getProperty(String ns, String field, String key, String def) {
        if (null == key || "".equals(key)) {
            return def;
        }
        Map<String, String> map = null;
        String f = field.toLowerCase();
        boolean nsNotNull = false;
        if (null != ns && !"".equals(ns)) {
            map = properties.get(ns + "." + f);
            nsNotNull = true;
        } else {
            map = properties.get(f);
        }
        if (null != map) {
            String ret = map.get(key.toLowerCase());
            if (null != ret) {
                return ret;
            }
        }
        if (nsNotNull) {
            map = properties.get(f);
            if (null != map) {
                String ret = map.get(key.toLowerCase());
                if (null != ret) {
                    return ret;
                }
            }
        }

        return def;
    }

    public List<String> getListProperty(String key) {
        return getListProperty(DefaultField, key);
    }

    public List<String> getListProperty(String field, String key) {
        return getListProperty(getNamespace(), field, key);
    }

    public List<String> getListProperty(String ns, String field, String key) {
        String value = getProperty(ns, field, key, null);
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
        return getSetProperty(getNamespace(), field, key);
    }

    public Set<String> getSetProperty(String ns, String field, String key) {
        String value = getProperty(ns, field, key, null);
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
        return getMapProperty(getNamespace(), field, key);
    }

    public Map<String, String> getMapProperty(String ns, String field, String key) {
        String value = getProperty(ns, field, key, null);
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
        return getIntProperty(getNamespace(), field, key, def);
    }

    public int getIntProperty(String ns, String field, String key, int def) {
        if (null == key || "".equals(key)) {
            return def;
        }
        String value = getProperty(ns, field, key, null);
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
        return getDoubleProperty(getNamespace(), field, key, def);
    }

    public double getDoubleProperty(String ns, String field, String key, double def) {
        if (null == key || "".equals(key)) {
            return def;
        }
        String value = getProperty(ns, field, key, null);
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
        return getBooleanProperty(getNamespace(), field, key, def);
    }

    public boolean getBooleanProperty(String ns, String field, String key, boolean def) {
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
        return getBean(getNamespace(), field, key, clazz);
    }

    public <T> T getBean(String ns, String field, String key, Class<T> clazz) {
        String jsonText = getProperty(ns, field, key, null);
        if (null != jsonText) {
            try {
                return (T) JSONObject.toBean(JSONObject.fromObject(jsonText), clazz);
            } catch (JSONException ex) {
                if (int.class.equals(clazz) || Integer.class.equals(clazz)) {
                    T ret = (T) Integer.valueOf(getIntProperty(field, key));
                    return ret;
                } else if (long.class.equals(clazz) || Long.class.equals(clazz)) {
                    T ret = (T) Long.valueOf(getProperty(field, key));
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
