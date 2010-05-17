/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.swt;

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
public class SimpleProgressListener implements ProgressListener {

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
            String bodyContent = nsHtmlElement.getInnerHTML();
            System.out.println(bodyContent);
        } else {
            System.out.println("can not get nsIWebBrowser");
        }
    }
}
