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

import javax.swing.event.TreeSelectionEvent;
import websiteschema.utils.Console;
import websiteschema.utils.AWTConsole;
import com.webrenderer.swing.BrowserFactory;
import com.webrenderer.swing.IBrowserCanvas;
import com.webrenderer.swing.IMozillaBrowserCanvas;
import com.webrenderer.swing.RenderingOptimization;
import com.webrenderer.swing.dom.IElement;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionListener;
import websiteschema.analyzer.browser.listener.*;
import websiteschema.context.BrowserContext;
import websiteschema.element.XPathAttributes;
import websiteschema.model.domain.Site;
import websiteschema.persistence.rdbms.SiteMapper;
import websiteschema.vips.VIPSImpl;
import websiteschema.vips.VisionBlock;

/**
 *
 * @author ray
 */
public class SimpleBrowser extends javax.swing.JFrame {

    IMozillaBrowserCanvas browser = null;
    Console console;
    BrowserContext context;
    VIPSImpl vips = null;
    String homePage = BrowserContext.getConfigure().getProperty("Browser", "HomePage");
    String analysisURL = BrowserContext.getConfigure().getProperty("Analyzer", "AnalysisURL");
    final String user = BrowserContext.getConfigure().getProperty("Browser", "LicenseUser");
    final String serial = BrowserContext.getConfigure().getProperty("Browser", "LicenseSerial");
    AnalysisPanel analysisPanel;
    private javax.swing.JScrollPane vipsTreePane;
    private VipsTree vipsTree = null;

    /** Creates new form SimpleAnalyzer */
    public SimpleBrowser() {
        initComponents();
        //初始化Context
        context = new BrowserContext();
        console = new AWTConsole(consoleTextArea);
        MessageDialog msgDialog = new MessageDialog(this, true);
        context.setMsgDialog(msgDialog);
        context.setConsole(console);
        context.setReference(homePage);

        //初始化Webrenderer
        initBrowser();
        //初始化VIPS Tree控件
        initVipsTree();
        //初始化分析栏
        initAnalysisPanel();

        //关闭consolePane
        this.consolePane.setVisible(this.hideConsoleMenu.isSelected());
        //一打开窗口，就最大化
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        //在console上显示浏览器信息
        displayBrowserInfo();
    }

    public void startAnalysis(String siteId) {
        this.browserTab.setSelectedIndex(1);
        SiteMapper siteMapper = BrowserContext.getSpringContext().getBean("siteMapper", SiteMapper.class);
        Site site = siteMapper.getSiteBySiteId(siteId);
        analysisPanel.setSiteId(siteId);
        analysisPanel.startAnalysis(site);
        String url = site.getUrl();
        this.urlTextField.setText(url);
        this.openUrl(url);
    }

    private void initAnalysisPanel() {
        analysisPanel = new AnalysisPanel(context, this);
//        context.setAnalysisPanel(analysisPanel);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(analysisPanel));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(analysisPanel));
    }

    private void initVipsTree() {
        vipsTreePane = new javax.swing.JScrollPane();
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(vipsTreePane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE));
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(vipsTreePane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE));

    }

    private void setupVipsTree(VisionBlock block) {
        if (null != vipsTree) {
            vipsTree.removeAll();
            vipsTree.setModel(new VipsTreeModel(block));
        } else {
            vipsTree = new VipsTree(block);
            vipsTree.setContext(context);
            vipsTreePane.setViewportView(vipsTree);
            vipsTree.addTreeSelectionListener(new TreeSelectionListener() {

                private VisionBlock lastSelectedVB = null;
                private String style = "border-style: solid; border-width: 5px; border-color: black;";

                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    System.out.println("selected " + e.getPath());
                    VisionBlock vb = (VisionBlock) e.getPath().getLastPathComponent();
//                    drawRectangleInPage(vb.getEle());
                    undraw(lastSelectedVB);
                    draw(vb);
                    lastSelectedVB = vb;
                }

                private void draw(VisionBlock vb) {
                    if (null != vb) {
                        List<VisionBlock> children = vb.getChildren();
                        if (null != children && children.size() > 0) {
                            for (VisionBlock blk : children) {
                                draw(blk);
                            }
                        } else {
                            draw(vb.getEle());
                        }
                    }
                }

                private void undraw(VisionBlock vb) {
                    if (null != vb) {
                        List<VisionBlock> children = vb.getChildren();
                        if (null != children && children.size() > 0) {
                            for (VisionBlock blk : children) {
                                undraw(blk);
                            }
                        } else {
                            undraw(vb.getEle());
                        }
                    }
                }

                private void draw(IElement ele) {
                    if (null != ele) {
                        String oldStyle = ele.getAttribute("style", 0);
                        if (null != oldStyle && !"".equals(oldStyle)) {
                            ele.setAttribute("style", oldStyle + ";" + style, 0);
                        } else {
                            ele.setAttribute("style", style, 0);
                        }
                    }
                }

                private void undraw(IElement ele) {
                    if (null != ele) {
                        String oldStyle = ele.getAttribute("style", 0);
                        if (null != oldStyle && !"".equals(oldStyle)) {
                            String s = oldStyle.replaceAll(style, "");
                            ele.setAttribute("style", s, 0);
                        }
                    }
                }
            });
        }

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

        //初始化BrowerContext
        context.setBrowser(browser);
        vips = new VIPSImpl(context);

        //添加Listener
        browser.addMouseListener(new SimpleMouseListener(context, this));
        browser.addPromptListener(new SimplePromptListener());
        SimpleNetworkListener snl = new SimpleNetworkListener(context);
        snl.setAddressTextField(urlTextField);
        snl.setProgress(browserProgress);
        browser.addNetworkListener(snl);
//        browser.addWindowListener(new SimpleWindowListener(context));

        //创建分析框
        IMozillaBrowserCanvas configBrowser = BrowserFactory.spawnMozilla();
        RenderingOptimization renOps2 = new RenderingOptimization();
        renOps2.setWindowlessFlashSmoothScrolling(true);
        configBrowser.setRenderingOptimizations(renOps2);
        configBrowser.setCookie(analysisURL, "websiteschema=analyzer;");
        configBrowser.loadURL(analysisURL);
        AnalyzeEventListener ael = new AnalyzeEventListener(configBrowser);
        ael.setSimpleBrowser(this);
        configBrowser.addMouseListener(ael);

        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.add(BorderLayout.CENTER, configBrowser.getComponent());
        this.configFrame.setContentPane(panel2);
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
        browserProgress = new javax.swing.JProgressBar();
        analysisPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        consolePane = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        consoleTextArea = new javax.swing.JTextArea();
        jToolBar2 = new javax.swing.JToolBar();
        clearButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        defaultXPathField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        usePosCheckBox = new javax.swing.JCheckBox();
        useIdCheckBox = new javax.swing.JCheckBox();
        useClassCheckBox = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        otherAttrTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        xpathField = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jCheckBox4 = new javax.swing.JCheckBox();
        browserTab = new javax.swing.JTabbedPane();
        configFrame = new javax.swing.JInternalFrame();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        hideAnalysisMenu = new javax.swing.JCheckBoxMenuItem();
        hideConsoleMenu = new javax.swing.JCheckBoxMenuItem();
        drawBorderMenu = new javax.swing.JMenuItem();

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
        jToolBar1.add(browserProgress);

        analysisPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 185, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 590, Short.MAX_VALUE)
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
            .addGap(0, 590, Short.MAX_VALUE)
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
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
        );

        consolePane.addTab("控制台", jPanel3);

        jLabel1.setText("默认XPath:");

        jButton1.setText("添加到剪切版");

        jLabel2.setText("XPath属性:");

        usePosCheckBox.setText("使用位置信息");

        useIdCheckBox.setSelected(true);
        useIdCheckBox.setText("使用ID");

        useClassCheckBox.setSelected(true);
        useClassCheckBox.setText("使用class");

        jLabel3.setText("其他属性:");

        jLabel4.setText("自定义XPath:");

        jButton2.setText("添加到剪切板");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(2, 2, 2)
                        .addComponent(defaultXPathField, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usePosCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(useIdCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(useClassCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(otherAttrTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(3, 3, 3)
                        .addComponent(xpathField, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(defaultXPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(usePosCheckBox)
                    .addComponent(useIdCheckBox)
                    .addComponent(useClassCheckBox)
                    .addComponent(jLabel3)
                    .addComponent(otherAttrTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(xpathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addContainerGap(97, Short.MAX_VALUE))
        );

        consolePane.addTab("节点信息", jPanel2);

        jLabel5.setText("页面编码:");

        jLabel6.setText("包含Frame: Yes");

        jCheckBox4.setText("关闭Frame");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox4)))
                .addContainerGap(380, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jCheckBox4))
                .addContainerGap(134, Short.MAX_VALUE))
        );

        consolePane.addTab("页面信息", jPanel5);

        javax.swing.GroupLayout configFrameLayout = new javax.swing.GroupLayout(configFrame.getContentPane());
        configFrame.getContentPane().setLayout(configFrameLayout);
        configFrameLayout.setHorizontalGroup(
            configFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 576, Short.MAX_VALUE)
        );
        configFrameLayout.setVerticalGroup(
            configFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 335, Short.MAX_VALUE)
        );

        browserTab.addTab("Configure", configFrame);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 576, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 335, Short.MAX_VALUE)
        );

        browserTab.addTab("Analyzer", jInternalFrame1);

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

        hideConsoleMenu.setText("Console");
        hideConsoleMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideConsoleMenuActionPerformed(evt);
            }
        });
        jMenu3.add(hideConsoleMenu);

        drawBorderMenu.setText("显示所有块的边框");
        drawBorderMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drawBorderMenuActionPerformed(evt);
            }
        });
        jMenu3.add(drawBorderMenu);

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
                    .addComponent(browserTab, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                    .addComponent(consolePane, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(browserTab, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(consolePane, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(analysisPane, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)))
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
        vips = null;
        vips = new VIPSImpl(context);
        VisionBlock block = vips.segment(browser.getDocument(), context.getReference());
        setupVipsTree(block);
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

    private void drawBorderMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drawBorderMenuActionPerformed
        // TODO add your handling code here:
        if (null != vips) {
            vips.getSegmenter().drawBorder();
        }
    }//GEN-LAST:event_drawBorderMenuActionPerformed

    private void openUrl(String url) {
        if (url.startsWith("ftp://")) {
            System.out.println("FTP URL: " + url);
        } else if (!url.startsWith("http://")) {
            url = "http://" + url;
        }
        context.setReference(url);
        browser.loadURL(url);
    }

    public XPathAttributes getXPathAttr() {
        XPathAttributes attr = new XPathAttributes();

        attr.setUsingPosition(this.usePosCheckBox.isSelected());
        attr.setUsingId(this.useIdCheckBox.isSelected());
        attr.setUsingClass(this.useClassCheckBox.isSelected());
        attr.setSpecifyAttr(this.otherAttrTextField.getText());

        return attr;
    }

    public void setXPathAttr(XPathAttributes attr) {
        this.usePosCheckBox.setSelected(attr.isUsingPosition());
        this.useIdCheckBox.setSelected(attr.isUsingId());
        this.useClassCheckBox.setSelected(attr.isUsingClass());
        this.otherAttrTextField.setText(attr.getSpecifyAttr());
    }

    public void displaySelectedElement(String xpath1, String xpath2) {
        this.defaultXPathField.setText(xpath1);
        this.xpathField.setText(xpath2);
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane analysisPane;
    private javax.swing.JButton backButton;
    private javax.swing.JProgressBar browserProgress;
    private javax.swing.JTabbedPane browserTab;
    private javax.swing.JButton clearButton;
    private javax.swing.JInternalFrame configFrame;
    private javax.swing.JTabbedPane consolePane;
    private javax.swing.JTextArea consoleTextArea;
    private javax.swing.JTextField defaultXPathField;
    private javax.swing.JMenuItem drawBorderMenu;
    private javax.swing.JButton forwardButton;
    private javax.swing.JButton goButton;
    private javax.swing.JCheckBoxMenuItem hideAnalysisMenu;
    private javax.swing.JCheckBoxMenuItem hideConsoleMenu;
    private javax.swing.JButton homeButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTextField otherAttrTextField;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JTextField urlTextField;
    private javax.swing.JCheckBox useClassCheckBox;
    private javax.swing.JCheckBox useIdCheckBox;
    private javax.swing.JCheckBox usePosCheckBox;
    private javax.swing.JButton vipsButton;
    private javax.swing.JTextField xpathField;
    // End of variables declaration//GEN-END:variables
}
