/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.context;

import com.webrenderer.swing.IMozillaBrowserCanvas;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.analyzer.browser.MessageDialog;
import websiteschema.element.StyleSheet;
import websiteschema.utils.Configure;
import websiteschema.utils.Console;

/**
 *
 * @author ray
 */
public class BrowserContext {

    Console console;
    MessageDialog msgDialog;
    IMozillaBrowserCanvas browser = null;
    private static final Configure configure = new Configure("configure-site.ini");
    Map<String, StyleSheet> styleSheets = new HashMap<String, StyleSheet>();
//    AnalysisPanel analysisPanel;
    private static final ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
    private String reference = null;

    public static Configure getConfigure() {
        return configure;
    }

    public static ApplicationContext getSpringContext() {
        return ctx;
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

    public void setReference(String ref) {
        this.reference = ref;
    }

    public String getReference() {
        return reference;
    }

    public MessageDialog getMsgDialog() {
        return msgDialog;
    }

    public void setMsgDialog(MessageDialog msgDialog) {
        this.msgDialog = msgDialog;
    }

}
