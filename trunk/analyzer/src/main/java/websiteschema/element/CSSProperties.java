/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.element;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author ray
 */
public class CSSProperties {

    Map<String, String> styles = new LinkedHashMap<String, String>();

    public void put(String key, String value) {
        styles.put(key, value);
    }

    public String get(String key) {
        return styles.get(key);
    }

    @Override
    public String toString() {
        return styles.toString();
    }
}
