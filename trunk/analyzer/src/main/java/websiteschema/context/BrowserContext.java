/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.context;

import com.webrenderer.swing.IMozillaBrowserCanvas;
import java.util.HashMap;
import java.util.Map;
import websiteschema.analyzer.browser.VipsFrame;
import websiteschema.element.StyleSheet;
import websiteschema.utils.Configure;
import websiteschema.utils.Console;
import websiteschema.vips.VipsCanvas;

/**
 *
 * @author ray
 */
public class BrowserContext {

    VipsFrame vipsFrame;
    VipsCanvas vipsCanvas;
    boolean useVIPS = true;
    Console console;
    IMozillaBrowserCanvas browser = null;
    Configure configure = Configure.getDefaultConfigure();
    Map<String, StyleSheet> styleSheets = new HashMap<String, StyleSheet>();

    public Configure getConfigure() {
        return configure;
    }

    public void setConfigure(Configure configure) {
        this.configure = configure;
    }

    public VipsFrame getVipsFrame() {
        return vipsFrame;
    }

    public void setVipsFrame(VipsFrame vips) {
        this.vipsFrame = vips;
    }

    public VipsCanvas getVipsCanvas() {
        return vipsCanvas;
    }

    public void setVipsCanvas(VipsCanvas vipsCanvas) {
        this.vipsCanvas = vipsCanvas;
    }

    public boolean isUseVIPS() {
        return useVIPS;
    }

    public void setUseVIPS(boolean useVIPS) {
        this.useVIPS = useVIPS;
    }

    public Console getConsole() {
        return console;
    }

    public void setConsole(Console console) {
        this.console = console;
    }

    public IMozillaBrowserCanvas getBrowser() {
        return browser;
    }

    public void setBrowser(IMozillaBrowserCanvas browser) {
        this.browser = browser;
    }

    public void setStyleSheet(String url, StyleSheet styleSheet) {
        this.styleSheets.put(url, styleSheet);
    }

    public StyleSheet getStyleSheet(String url) {
        return this.styleSheets.get(url);
    }
}
