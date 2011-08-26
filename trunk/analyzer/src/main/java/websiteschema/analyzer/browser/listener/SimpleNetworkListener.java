/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.browser.listener;

import com.webrenderer.swing.IBrowserCanvas;
import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.event.NetworkEvent;
import com.webrenderer.swing.event.NetworkListener;
import org.apache.log4j.Logger;
import websiteschema.context.BrowserContext;
import websiteschema.element.Rectangle;
import websiteschema.element.RectangleFactory;

/**
 *
 * @author ray
 */
public class SimpleNetworkListener implements NetworkListener {

    Logger l = Logger.getRootLogger();
    BrowserContext context;
    IBrowserCanvas browser;

    public SimpleNetworkListener(BrowserContext context, IBrowserCanvas browser) {
        this.context = context;
        this.browser = browser;
    }

    @Override
    public void onProgressChange(NetworkEvent ne) {
        l.debug("onProgressChange" + ((float) ne.getCurrentProgress() / (float) ne.getMaximumProgress()));
    }

    @Override
    public void onDocumentLoad(NetworkEvent ne) {
        l.debug("onDocumentLoad " + ne.getURL());
    }

    @Override
    public void onDocumentComplete(NetworkEvent ne) {
        l.debug("onDocumentComplete ");
        if (context.isUseVIPS()) {
            context.getVipsCanvas().setDocument(browser.getDocument());
        }
    }

    @Override
    public void onNetworkStatus(NetworkEvent ne) {
        l.debug("onNetworkStatus " + ne.getStatusText());
    }

    @Override
    public void onNetworkError(NetworkEvent ne) {
        l.debug("onNetworkError " + ne.getFailure());
    }

    @Override
    public void onHTTPResponse(NetworkEvent ne) {
//        l.debug("onHTTPResponse\n" + ne.getResponseHeaders());
    }

    @Override
    public void onHTTPInterceptHeaders(NetworkEvent ne) {
        l.debug("onHTTPInterceptHeaders " + ne.getRequestHeaders());
    }
}
