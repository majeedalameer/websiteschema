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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ray
 */
public class Configure {

    private static final String Default_Field = "default";
    private static final Configure root = new Configure();
    private String currentField = Default_Field;
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
                String line = br.readLine();
                while (null != line) {
                    parseLine(line);
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

    private void parseLine(String line) {
        if (!line.startsWith("#")) {
            if (line.startsWith("[") && line.endsWith("]")) {
                currentField = line.substring(1, line.length() - 1).trim().toLowerCase();
            } else if (line.indexOf("=") > 0) {
                int eq = line.indexOf("=");
                String key = line.substring(0, eq).trim().toLowerCase();
                String value = line.substring(eq + 1, line.length());
                putProperties(
                        currentField, key.trim(), value.trim());
            }
        }
    }

    public void putProperties(String key, String value) {
        putProperties(Default_Field, key, value);
    }

    public void putProperties(String field, String key, String value) {
        String f = field.toLowerCase();
        Map<String, String> map = properties.get(f);
        if (null == map) {
            map = new HashMap<String, String>();
            properties.put(f, map);
        }
        String k = key.toLowerCase();
        map.put(k, value);
    }

    public String getProperty(String key) {
        return getProperty(Default_Field, key, null);
    }

    public String getProperty(String field, String key) {
        return getProperty(field, key, null);
    }

    public String getProperty(String field, String key, String def) {
        if (null == key || "".equals(key)) {
            return def;
        }
        Map<String, String> map = properties.get(field.toLowerCase());
        if (null != map) {
            String ret = map.get(key.toLowerCase());
            if (null != ret) {
                return ret;
            }
        }
        return def;
    }

    public List<String> getListProperty(String key) {
        return getListProperty(Default_Field, key);
    }

    public List<String> getListProperty(String field, String key) {
        String value = getProperty(field, key);
        if (null != value) {
            return Arrays.asList(value.split(","));
        } else {
            return null;
        }
    }

    public Set<String> getSetProperty(String key) {
        return getSetProperty(Default_Field, key);
    }

    public Set<String> getSetProperty(String field, String key) {
        String value = getProperty(field, key);
        if (null != value) {
            return new HashSet<String>(Arrays.asList(value.split(",")));
        } else {
            return null;
        }
    }
}
