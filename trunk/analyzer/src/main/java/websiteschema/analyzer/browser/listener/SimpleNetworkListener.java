/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.browser.listener;

import com.webrenderer.swing.IBrowserCanvas;
import com.webrenderer.swing.event.NetworkEvent;
import com.webrenderer.swing.event.NetworkListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import websiteschema.context.BrowserContext;
import websiteschema.vips.VIPSImpl;

/**
 *
 * @author ray
 */
public class SimpleNetworkListener implements NetworkListener {

    Logger l = Logger.getRootLogger();
    BrowserContext context;
    IBrowserCanvas browser;
    javax.swing.JTextField addressTextField;
    int sec = 1000;
    long delay = 30 * sec;
    boolean started = false;
    Timer timer = null;

    public SimpleNetworkListener(BrowserContext context) {
        this.context = context;
        this.browser = context.getBrowser();
    }

    public JTextField getAddressTextField() {
        return addressTextField;
    }

    public void setAddressTextField(JTextField addressTextField) {
        this.addressTextField = addressTextField;
    }

    @Override
    public void onProgressChange(NetworkEvent ne) {
        l.debug("onProgressChange" + ((float) ne.getCurrentProgress() / (float) ne.getMaximumProgress()));
    }

    @Override
    public void onDocumentLoad(NetworkEvent ne) {
        l.debug("onDocumentLoad ");
        started = false;
        timer = new Timer();
        timer.schedule(new MyTimerTask(), delay);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            if (couldProcess()) {
                process();
            }
        }
    }

    public synchronized boolean couldProcess() {
        if (!started) {
            started = true;
            return started;
        } else {
            return false;
        }
    }

    private void process() {
//        VIPSImpl vips = new VIPSImpl(context);
//        vips.segment(context.getBrowser().getDocument());
    }

    @Override
    public void onDocumentComplete(NetworkEvent ne) {
        l.debug("onDocumentComplete " + ne.getURL());
        addressTextField.setText(ne.getURL());

        timer.cancel();
        timer = null;
        if (couldProcess()) {
            process();
        }
    }

    @Override
    public void onNetworkStatus(NetworkEvent ne) {
        l.debug("onNetworkStatus " + ne.getStatus());
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
        l.debug("onHTTPInterceptHeaders " + ne.getURL());
        l.trace("Send Request Header:\n" + ne.getMutableRequestHeaders());
    }
}
