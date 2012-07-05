/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.browser;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.webrenderer.swing.*;

/**
 *
 * @author ray
 */
public final class TestBrowser {

    IMozillaBrowserCanvas browser;
    JTextField textfield;

    public TestBrowser() {
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(content());
        frame.setSize(750, 450);
        frame.setVisible(true);
    }

    public JPanel content() {
        JPanel panel = new JPanel(new BorderLayout());

        textfield = new JTextField();

        textfield.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                browser.loadURL(textfield.getText());
            }
        });

        //Must be called to stop License dialog
        BrowserFactory.setLicenseData("30dtrial", "2PS4MACGJHK0T6JP5F1101Q8");

        //Core function to create browser
        browser = BrowserFactory.spawnMozilla();

        // If you are behind a proxy server, it may be necessary to set the proxy authentication
        // to enable access for page loading.  Enable the following call(s) with the appropriate
        // values for domain, port..
        //int yourProxyPort = 8080;
        //String yourProxyServer = "proxyserver";
        //browser.setProxyProtocol(new ProxySetting( ProxySetting.PROTOCOL_ALL, yourProxyServer, yourProxyPort));
        //browser.enableProxy();

        // Improves scrolling performance on pages with windowless flash.
        RenderingOptimization renOps = new RenderingOptimization();
        renOps.setWindowlessFlashSmoothScrolling(true);
        browser.setRenderingOptimizations(renOps);

        browser.loadURL("http://www.baidu.com/");

        panel.add(BorderLayout.NORTH, textfield);
        panel.add(BorderLayout.CENTER, browser.getComponent());

        return panel;
    }

    public static void main(String[] args) {
        new TestBrowser();
    }
}
