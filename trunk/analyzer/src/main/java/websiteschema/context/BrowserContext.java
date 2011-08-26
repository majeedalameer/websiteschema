/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.context;

import websiteschema.analyzer.browser.VipsFrame;
import websiteschema.utils.Console;
import websiteschema.vips.VipsCanvas;

/**
 *
 * @author ray
 */
public class BrowserContext {

    VipsFrame vips;
    VipsCanvas vipsCanvas;
    boolean useVIPS = true;
    Console console;

    public VipsFrame getVipsFrame() {
        return vips;
    }

    public void setVipsFrame(VipsFrame vips) {
        this.vips = vips;
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

}
