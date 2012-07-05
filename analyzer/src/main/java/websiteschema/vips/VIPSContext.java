/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips;

import java.util.HashMap;
import java.util.Map;
import websiteschema.element.StyleSheet;
import websiteschema.conf.Configure;
import websiteschema.utils.Console;

/**
 *
 * @author ray
 */
public class VIPSContext {

    Console console;
    private static final Configure configure = new Configure("configure-site.ini");
    Map<String, StyleSheet> styleSheets = new HashMap<String, StyleSheet>();

    public static Configure getConfigure() {
        return configure;
    }

    public Console getConsole() {
        return console;
    }

    public void setConsole(Console console) {
        this.console = console;
    }

    public void setStyleSheet(String url, StyleSheet styleSheet) {
        this.styleSheets.put(url, styleSheet);
    }

    public StyleSheet getStyleSheet(String url) {
        return this.styleSheets.get(url);
    }
}
