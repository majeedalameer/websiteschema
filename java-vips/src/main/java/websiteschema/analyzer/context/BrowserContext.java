/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.context;

import com.webrenderer.swing.IMozillaBrowserCanvas;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import websiteschema.analyzer.browser.SimpleBrowser;
import websiteschema.vips.VIPSContext;

/**
 *
 * @author ray
 */
public class BrowserContext extends VIPSContext {

    IMozillaBrowserCanvas browser = null;
    Map<String, Map<String, String>> urlAndMIME = new LinkedHashMap<String, Map<String, String>>();
    private String reference = null;
    private SimpleBrowser simpleBrowser;

    public SimpleBrowser getSimpleBrowser() {
        return simpleBrowser;
    }

    public void setSimpleBrowser(SimpleBrowser simpleBrowser) {
        this.simpleBrowser = simpleBrowser;
    }

    public IMozillaBrowserCanvas getBrowser() {
        return browser;
    }

    public void setBrowser(IMozillaBrowserCanvas browser) {
        this.browser = browser;
    }

    public void setReference(String ref) {
        this.reference = ref;
    }

    public String getReference() {
        return reference;
    }

    public Map<String, Map<String, String>> getURLAndMIME() {
        return this.urlAndMIME;
    }

    public void addResponseHeader(String url, String responseHeader) {
        Map<String, String> map = null;
        if (urlAndMIME.containsKey(url)) {
            map = urlAndMIME.get(url);
        } else {
            map = new HashMap<String, String>();
            urlAndMIME.put(url, map);
        }
        map.put("response", responseHeader);
    }

    public String getResponseHeader(String url) {
        Map<String, String> map = null;
        if (urlAndMIME.containsKey(url)) {
            map = urlAndMIME.get(url);
            return map.get("response");
        }
        return null;
    }

    public String getRequestHeader(String url) {
        Map<String, String> map = null;
        if (urlAndMIME.containsKey(url)) {
            map = urlAndMIME.get(url);
            return map.get("request");
        }
        return null;
    }

    public void addRequestHeader(String url, String requestHeader) {
        Map<String, String> map = null;
        if (urlAndMIME.containsKey(url)) {
            map = urlAndMIME.get(url);
        } else {
            map = new HashMap<String, String>();
            urlAndMIME.put(url, map);
        }
        map.put("request", requestHeader);
    }
}
