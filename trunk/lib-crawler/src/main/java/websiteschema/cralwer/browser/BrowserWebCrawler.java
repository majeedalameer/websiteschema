/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cralwer.browser;

import com.webrenderer.swing.BrowserFactory;
import com.webrenderer.swing.IMozillaBrowserCanvas;
import com.webrenderer.swing.ProxySetting;
import com.webrenderer.swing.RenderingOptimization;
import com.webrenderer.swing.dom.IDocument;
import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import java.awt.BorderLayout;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.cralwer.Crawler;
import websiteschema.utils.Configure;
import websiteschema.utils.UrlLinkUtil;

/**
 *
 * @author ray
 */
public class BrowserWebCrawler implements Crawler {

    private static final String user = Configure.getDefaultConfigure().getProperty("Browser", "LicenseUser");
    private static final String serial = Configure.getDefaultConfigure().getProperty("Browser", "LicenseSerial");

//    static {
//        System.out.println("Add Shutdown Hook");
//        Runtime.getRuntime().addShutdownHook(new Thread() {
//
//            @Override
//            public void run() {
//                System.out.println("Shutdown Mozilla : " + BrowserFactory.shutdownMozilla());
//            }
//        });
//    }
    IMozillaBrowserCanvas browser = null;
    JFrame frame;
    boolean loadImage = false;
    boolean loadEmbeddedFrame = false;
    String url = null;
    String encoding = null;
    MyNetworkListener listener = new MyNetworkListener(this);
    IDocument document = null;
    Boolean lock = false;

    @Override
    public void finalize() {
        if (null != browser)BrowserFactory.destroyBrowser(browser);
        if (null != frame) frame.dispose();
    }

    public BrowserWebCrawler() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(content());
        frame.setSize(1024, 600);
        frame.setVisible(true);
    }

    private JPanel content() {
        JPanel panel = new JPanel(new BorderLayout());
        BrowserFactory.setLicenseData(user, serial);
        //Core function to create browser
        browser = BrowserFactory.spawnMozilla();
        // Improves scrolling performance on pages with windowless flash.
        RenderingOptimization renOps = new RenderingOptimization();
        renOps.setWindowlessFlashSmoothScrolling(true);
        browser.setRenderingOptimizations(renOps);
        panel.add(BorderLayout.CENTER, browser.getComponent());

        browser.addNetworkListener(listener);
        return panel;
    }

    @Override
    public Document[] crawl(String url) {
        browser.loadURL(url);
        try {
            synchronized (lock) {
                System.out.println("wait");
                lock.wait(30000);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("after wait");
        document = browser.getDocument();
        IDocument frames[] = document.getChildFrames();
        Document[] ret = null;
        if (null != document) {
            int len = null != frames ? frames.length + 1 : 1;
            ret = new Document[len];
            ret[0] = (Document) browser.getW3CDocument();
            for (int i = 1; i < len; i++) {
                IElement body = frames[i - 1].getBody();
                ret[i] = (Document) body.getParentElement().convertToW3CNode();
            }
        }
        if (null != frame) {
            BrowserFactory.destroyBrowser(browser);
            frame.dispose();
        }
        return ret;
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public void setProxy(String server, int port) {
        browser.setProxyProtocol(new ProxySetting(ProxySetting.PROTOCOL_ALL, server, port));
        browser.enableProxy();
    }

    @Override
    public void executeScript(String javascriptBody) {
        browser.executeScript(javascriptBody);
    }

    @Override
    public void setLoadImage(boolean yes) {
        this.loadImage = yes;
    }

    public boolean isLoadImage() {
        return loadImage;
    }

    @Override
    public void setLoadEmbeddedFrame(boolean yes) {
        loadEmbeddedFrame = yes;
    }

    public boolean isLoadEmbeddedFrame() {
        return loadEmbeddedFrame;
    }

    @Override
    public void stopLoad() {
        this.browser.stopLoad();
    }

    @Override
    public String[] getLinks() {
        IElementCollection links = document.getLinks();
        String[] ret = new String[links.length()];

        for (int i = 0; i < links.length(); i++) {
            IElement ele = links.item(i);
            String href = ele.getAttribute("href", 0);
            URI uri = UrlLinkUtil.getInstance().getURL(getUrl(), href);
            ret[i] = uri.getScheme() + ":" + uri.getSchemeSpecificPart();
        }

        return ret;
    }

    public static void print(Node node) {
        if(node.getNodeType() == Node.TEXT_NODE) {
            System.out.println(node.getNodeValue());
        } else {
            if(node.hasChildNodes()) {
                NodeList children = node.getChildNodes();
                for(int i = 0; i < children.getLength(); i++) {
                    Node child = children.item(i);
                    print(child);
                }
            }
        }
    }
}
