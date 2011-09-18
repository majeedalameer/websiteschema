/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.browser.listener;

import com.webrenderer.swing.IMozillaBrowserCanvas;
import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import com.webrenderer.swing.event.MouseEvent;
import com.webrenderer.swing.event.MouseListener;
import javax.swing.JTabbedPane;
import org.apache.log4j.Logger;
import websiteschema.analyzer.browser.SimpleBrowser;
import websiteschema.context.BrowserContext;
import websiteschema.utils.ElementUtil;

/**
 *
 * @author ray
 */
public class AnalyzeEventListener implements MouseListener {

    Logger l = Logger.getRootLogger();
    IMozillaBrowserCanvas browser;
    String analyzerTips = BrowserContext.getConfigure().getProperty("AnalyzerTips");
    SimpleBrowser simpleBrowser;

    public AnalyzeEventListener(IMozillaBrowserCanvas browser) {
        this.browser = browser;
    }

    public void setSimpleBrowser(SimpleBrowser simpleBrowser) {
        this.simpleBrowser = simpleBrowser;
    }

    @Override
    public void onClick(MouseEvent me) {
        l.trace("mouse onclick");
        IElement ele = me.getTargetElement();
        String innerHTML = ele.toString();
        System.out.println(innerHTML);

        String nodeType = ElementUtil.getInstance().getNodeType(ele);
        l.trace("node type: " + nodeType + " node name: " + ele.getTagName());
        String[] attrs = ele.getAttributes();
        boolean containAnalyzerTips = false;
        boolean containSrcAccept = true;
        if (null != attrs) {
            for (String attr : attrs) {
                l.trace(attr);
                if (attr.contains(analyzerTips)) {
                    containAnalyzerTips = true;
                    continue;
                } else if (attr.contains("resources/accept.gif")) {
                    containSrcAccept = true;
                    continue;
                }
                if (containAnalyzerTips && containSrcAccept) {
                    // receive analyze command.
                    IElement tr = ele.getParentElement().getParentElement().getParentElement();
                    IElementCollection children = tr.getChildElements();
                    String id = ElementUtil.getInstance().getText(children.item(1));
                    String siteId = ElementUtil.getInstance().getText(children.item(2));
                    l.info("Starting analysis site: " + siteId);
//                    ele.setAttribute("src", "resources/delete.gif", 0);
                    simpleBrowser.startAnalysis(siteId);
                    break;
                }
            }
        }
    }

    @Override
    public void onDoubleClick(MouseEvent me) {
    }

    @Override
    public void onMouseDown(MouseEvent me) {
    }

    @Override
    public void onMouseUp(MouseEvent me) {
    }
}
