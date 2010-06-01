/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.swt;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.Device;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.SelectionListener;
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
    final Shell shell = new Shell(display);
    Browser browser = null;
    String url = "";
    Text text = null;

    public TestBrowser(String URL){
        url = URL;
    }

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

    public void open(){
        open(url);
    }

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
        shell.setLayout(new GridLayout(2, true));
        System.out.println("platform : " + SWT.getPlatform());
        try {
            browser = new Browser(shell, SWT.MOZILLA);
        } catch (SWTError e) {
            display.dispose();
            return;
        }
        browser.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));
        
        Button button = new Button(shell, SWT.PUSH);
	button.setText("转到");
        button.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent se) {
                System.out.println("press1 " + text.getText());
                open(text.getText());
            }

            public void widgetDefaultSelected(SelectionEvent se) {
                System.out.println("press2");
            }
        });
        text = new Text(shell, SWT.MULTI | SWT.BORDER);
        text.setText(url);
    }

    public void trigger(nsIDOMNSHTMLElement html) {
        System.out.println("trig a new event");
    }

    public static void main(String[] args) {
        TestBrowser browser = new TestBrowser("http://www.baidu.com/");
        browser.init();
        browser.open();
    }
}
