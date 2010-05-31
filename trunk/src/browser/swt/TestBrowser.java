/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.swt;

import org.eclipse.swt.*;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.graphics.Device;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMHTMLDocument;
import org.mozilla.interfaces.nsIDOMHTMLElement;
import org.mozilla.interfaces.nsIDOMNSHTMLElement;
import org.mozilla.interfaces.nsIWebBrowser;

/**
 *
 * @author ray
 */
public class TestBrowser {

    final Display display = new Display();
    Shell shell = null;
    Browser browser = null;
    ProgressListener pListener = new ProgressListener() {

        public void changed(ProgressEvent pe) {
            System.out.println("status changed");
        }

        public void completed(ProgressEvent pe) {
            System.out.println("HTML load completed.");
            System.out.println(pe.widget.getClass().getName());
            System.out.println(pe.display.getClass().getName());
            nsIWebBrowser webBrowser = (nsIWebBrowser) ((Browser) pe.widget).getWebBrowser();
            if (null != webBrowser) {
                nsIDOMDocument document = webBrowser.getContentDOMWindow().getDocument();
                nsIDOMHTMLDocument htmlDocument = (nsIDOMHTMLDocument) document.queryInterface(nsIDOMHTMLDocument.NS_IDOMHTMLDOCUMENT_IID);
                nsIDOMHTMLElement element = htmlDocument.getBody();
                nsIDOMNSHTMLElement nsHtmlElement = (nsIDOMNSHTMLElement) element.queryInterface(nsIDOMNSHTMLElement.NS_IDOMNSHTMLELEMENT_IID);
//                String bodyContent = nsHtmlElement.getInnerHTML();
//                System.out.println(bodyContent);
                trigger(nsHtmlElement);
            } else {
                System.out.println("can not get nsIWebBrowser");
            }
        }
    };

    public void open(String url) {
        if (browser != null) {
            /* The Browser widget can be used */
            System.out.println(browser.getWebBrowser());
            browser.addProgressListener(pListener);
            browser.setUrl(url);

        }
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    public void init() {
        Device.DEBUG = true;
        shell = new Shell(display);
        shell.setLayout(new FillLayout());
        shell.setText("Mozilla");
        System.out.println("platform : " + SWT.getPlatform());
        try {
            browser = new Browser(shell, SWT.MOZILLA);
        } catch (SWTError e) {
            /* The Browser widget throws an SWTError if it fails to
             * instantiate properly. Application code should catch
             * this SWTError and disable any feature requiring the
             * Browser widget.
             * Platform requirements for the SWT Browser widget are available
             * from the SWT FAQ website.
             */

            display.dispose();
            return;
        }

    }

    public void trigger(nsIDOMNSHTMLElement html) {
        System.out.println("trig a new event");
    }

    public static void main(String[] args) {
        TestBrowser browser = new TestBrowser();
        browser.init();
        browser.open("http://www.cs.com.cn/xwzx/05/201005/t20100531_2454386.htm");
    }
}
