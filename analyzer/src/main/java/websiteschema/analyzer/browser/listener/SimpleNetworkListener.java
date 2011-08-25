/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.analyzer.browser.listener;

import com.webrenderer.swing.event.NetworkEvent;
import com.webrenderer.swing.event.NetworkListener;
import org.apache.log4j.Logger;

/**
 *
 * @author ray
 */
public class SimpleNetworkListener implements NetworkListener {

    Logger l = Logger.getRootLogger();

    @Override
    public void onProgressChange(NetworkEvent ne) {
        l.debug("onProgressChange");
    }

    @Override
    public void onDocumentLoad(NetworkEvent ne) {
        l.debug("onDocumentLoad");
    }

    @Override
    public void onDocumentComplete(NetworkEvent ne) {
        l.debug("onDocumentComplete");
    }

    @Override
    public void onNetworkStatus(NetworkEvent ne) {
        l.debug("onNetworkStatus");
    }

    @Override
    public void onNetworkError(NetworkEvent ne) {
        l.debug("onNetworkError");
    }

    @Override
    public void onHTTPResponse(NetworkEvent ne) {
        l.debug("onHTTPResponse");
    }

    @Override
    public void onHTTPInterceptHeaders(NetworkEvent ne) {
        l.debug("onHTTPInterceptHeaders");
    }

}
