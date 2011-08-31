/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SimpleAnalyzer.java
 *
 * Created on Aug 23, 2011, 11:59:13 PM
 */
package websiteschema.analyzer.browser;

import websiteschema.utils.Console;
import websiteschema.utils.AWTConsole;
import com.webrenderer.swing.BrowserFactory;
import com.webrenderer.swing.IBrowserCanvas;
import com.webrenderer.swing.IMozillaBrowserCanvas;
import com.webrenderer.swing.RenderingOptimization;
import com.webrenderer.swing.dom.IDocument;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import websiteschema.analyzer.browser.listener.SimpleMouseListener;
import websiteschema.analyzer.browser.listener.SimpleNetworkListener;
import websiteschema.analyzer.browser.listener.SimplePromptListener;
import websiteschema.analyzer.browser.listener.SimpleWindowListener;
import websiteschema.context.BrowserContext;
import websiteschema.element.Rectangle;
import websiteschema.element.StyleSheet;
import websiteschema.element.factory.RectangleFactory;
import websiteschema.element.factory.StyleSheetFactory;
import websiteschema.utils.Configure;
import websiteschema.vips.VIPSImpl;
import websiteschema.vips.VipsCanvas;
import websiteschema.vips.VisionBasedPageSegmenter;
import websiteschema.vips.VisionBlock;
import websiteschema.vips.extraction.BlockExtractor;
import websiteschema.vips.extraction.BlockExtractorFactory;

/**
 *
 * @author ray
 */
public class SimpleBrowser extends javax.swing.JFrame {

    IMozillaBrowserCanvas browser = null;
    Console console;
    VipsFrame vipsFrame;
    VipsCanvas vipsCanvas;
    BrowserContext context;
    VIPSImpl vips = null;
    boolean doVIPS = false;
    String homePage = "http://localhost:8080/";
    final String user = Configure.getDefaultConfigure().getProperty("Browser", "LicenseUser");
    final String serial = Configure.getDefaultConfigure().getProperty("Browser", "LicenseSerial");

    /** Creates new form SimpleAnalyzer */
    public SimpleBrowser() {
        initComponents();

        
        //一打开窗口，就最大化
//        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        console = new AWTConsole(consoleTextArea);


        //初始化Webrenderer
        initBrowser();


        displayBrowserInfo();
    }

    private void initVipsTree(VisionBlock block){
        vipsTree = new VipsTree(block);

        vipsTreePane.setViewportView(vipsTree);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(vipsTreePane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(vipsTreePane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
        );

    }

    private void initBrowser() {
        BrowserFactory.setLicenseData(user, serial);

        //Core function to create browser
        browser = BrowserFactory.spawnMozilla();

        RenderingOptimization renOps = new RenderingOptimization();
        renOps.setWindowlessFlashSmoothScrolling(true);
        browser.setRenderingOptimizations(renOps);

        browser.loadURL(homePage);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(BorderLayout.CENTER, browser.getComponent());
        this.jInternalFrame1.setContentPane(panel);

        //添加VIPS测试代码
        context = new BrowserContext();
        context.setUseVIPS(doVIPS);
        if (context.isUseVIPS()) {
            vipsFrame = new VipsFrame();
            vipsCanvas = new VipsCanvas();
            vipsFrame.setVisible(true);
            context.setVipsFrame(vipsFrame);
            context.setVipsCanvas(vipsCanvas);
            vipsFrame.setCanvas(vipsCanvas);
        }
        context.setConsole(console);
        context.setBrowser(browser);
        vips = new VIPSImpl(context);

        //添加Listener
        browser.addMouseListener(new SimpleMouseListener(context));
        browser.addPromptListener(new SimplePromptListener());
        SimpleNetworkListener snl = new SimpleNetworkListener(context);
        snl.setAddressTextField(urlTextField);
        browser.addNetworkListener(snl);
//        browser.addWindowListener(new SimpleWindowListener(context));
    }

    private void displayBrowserInfo() {
        String mozVersion = browser.getMozillaVersion();
        String mozPath = BrowserFactory.getLibraryPath();

        console.log("mozilla path: " + mozPath);
        console.log("xulrunner version: " + mozVersion);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        backButton = new javax.swing.JButton();
        forwardButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        homeButton = new javax.swing.JButton();
        urlTextField = new javax.swing.JTextField();
        stopButton = new javax.swing.JButton();
        goButton = new javax.swing.JButton();
        vipsButton = new javax.swing.JButton();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        analysisPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        consolePane = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        consoleTextArea = new javax.swing.JTextArea();
        jToolBar2 = new javax.swing.JToolBar();
        clearButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        hideAnalysisMenu = new javax.swing.JCheckBoxMenuItem();
        hideConsoleMenu = new javax.swing.JCheckBoxMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simple Browser");

        jToolBar1.setRollover(true);

        backButton.setText("<");
        backButton.setToolTipText("Back");
        backButton.setFocusable(false);
        backButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        backButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(backButton);

        forwardButton.setText(">");
        forwardButton.setToolTipText("Forward");
        forwardButton.setFocusable(false);
        forwardButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        forwardButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        forwardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forwardButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(forwardButton);

        refreshButton.setText("刷");
        refreshButton.setToolTipText("Refresh");
        refreshButton.setFocusable(false);
        refreshButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refreshButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(refreshButton);

        homeButton.setText("H");
        homeButton.setToolTipText("Home page");
        homeButton.setFocusable(false);
        homeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        homeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        homeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(homeButton);

        urlTextField.setText("about:blank");
        urlTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                urlTextFieldActionPerformed(evt);
            }
        });
        jToolBar1.add(urlTextField);

        stopButton.setText("X");
        stopButton.setToolTipText("Stop loading");
        stopButton.setFocusable(false);
        stopButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stopButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(stopButton);

        goButton.setText("Go");
        goButton.setToolTipText("Load page");
        goButton.setFocusable(false);
        goButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        goButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        goButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(goButton);

        vipsButton.setText("VIPS");
        vipsButton.setToolTipText("Start VIPS");
        vipsButton.setFocusable(false);
        vipsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        vipsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        vipsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vipsButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(vipsButton);

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 581, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 354, Short.MAX_VALUE)
        );

        analysisPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 185, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 505, Short.MAX_VALUE)
        );

        analysisPane.addTab("基本分析", jPanel1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 185, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 505, Short.MAX_VALUE)
        );

        analysisPane.addTab("VB树", jPanel4);

        consolePane.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        consoleTextArea.setColumns(20);
        consoleTextArea.setRows(5);
        jScrollPane2.setViewportView(consoleTextArea);

        jToolBar2.setRollover(true);

        clearButton.setText("Clear");
        clearButton.setFocusable(false);
        clearButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        clearButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });
        jToolBar2.add(clearButton);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2))
        );

        consolePane.addTab("console", jPanel3);

        jMenu1.setText("文件");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1.setText("退出");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("编辑");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("视图");

        hideAnalysisMenu.setSelected(true);
        hideAnalysisMenu.setText("分析栏");
        hideAnalysisMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideAnalysisMenuActionPerformed(evt);
            }
        });
        jMenu3.add(hideAnalysisMenu);

        hideConsoleMenu.setSelected(true);
        hideConsoleMenu.setText("Console");
        hideConsoleMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideConsoleMenuActionPerformed(evt);
            }
        });
        jMenu3.add(hideConsoleMenu);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(analysisPane, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(consolePane, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jInternalFrame1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(consolePane, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4))
                    .addComponent(analysisPane, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void goButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goButtonActionPerformed
        // TODO add your handling code here:
        String url = urlTextField.getText();
        openUrl(url);
    }//GEN-LAST:event_goButtonActionPerformed

    private void urlTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlTextFieldActionPerformed
        // TODO add your handling code here:
        String url = urlTextField.getText();
        openUrl(url);
    }//GEN-LAST:event_urlTextFieldActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        if (browser.canGoBack()) {
            browser.goBack();
        }
    }//GEN-LAST:event_backButtonActionPerformed

    private void forwardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forwardButtonActionPerformed
        // TODO add your handling code here:
        if (browser.canGoForward()) {
            browser.goForward();
        }
    }//GEN-LAST:event_forwardButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        // TODO add your handling code here:
        this.consoleTextArea.setText("");
    }//GEN-LAST:event_clearButtonActionPerformed

    private void vipsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vipsButtonActionPerformed
        // TODO add your handling code here:
        VisionBlock block = vips.segment(browser.getDocument());
        vipsTree = new VipsTree(block);

        initVipsTree(block);

    }//GEN-LAST:event_vipsButtonActionPerformed

    private void homeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeButtonActionPerformed
        // TODO add your handling code here:
        openUrl(homePage);
    }//GEN-LAST:event_homeButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        // TODO add your handling code here:
        browser.stopLoad();
    }//GEN-LAST:event_stopButtonActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        // TODO add your handling code here:
        browser.reload(IBrowserCanvas.RELOAD_NORMAL);
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void hideAnalysisMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideAnalysisMenuActionPerformed
        // TODO add your handling code here:
        this.analysisPane.setVisible(this.hideAnalysisMenu.isSelected());
    }//GEN-LAST:event_hideAnalysisMenuActionPerformed

    private void hideConsoleMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideConsoleMenuActionPerformed
        // TODO add your handling code here:
        this.consolePane.setVisible(this.hideConsoleMenu.isSelected());
    }//GEN-LAST:event_hideConsoleMenuActionPerformed

    private void openUrl(String url) {
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }
        browser.loadURL(url);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new SimpleBrowser().setVisible(true);
            }
        });
    }
    private javax.swing.JScrollPane vipsTreePane = new javax.swing.JScrollPane();
    private VipsTree vipsTree;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane analysisPane;
    private javax.swing.JButton backButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JTabbedPane consolePane;
    private javax.swing.JTextArea consoleTextArea;
    private javax.swing.JButton forwardButton;
    private javax.swing.JButton goButton;
    private javax.swing.JCheckBoxMenuItem hideAnalysisMenu;
    private javax.swing.JCheckBoxMenuItem hideConsoleMenu;
    private javax.swing.JButton homeButton;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JTextField urlTextField;
    private javax.swing.JButton vipsButton;
    // End of variables declaration//GEN-END:variables

}
