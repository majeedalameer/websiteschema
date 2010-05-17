/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.swt;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Device;
/*
 * 您必须指定xulrunner的位置，方法：-Dorg.eclipse.swt.browser.XULRunnerPath=/usr/local/xulrunner-sdk/bin
 * @author ray
 */

public class SwtBrowser {

    public static void main(String[] args) {
        final Display display = new Display();
        Device.DEBUG = true;
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        shell.setText("Mozilla");
        System.out.println("platform : " + SWT.getPlatform());
        Browser browser = null;
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
        if (browser != null) {
            /* The Browser widget can be used */
            System.out.println(browser.getWebBrowser());
            browser.addProgressListener(new SimpleProgressListener());
            browser.setUrl("http://www.baidu.com/");

        }
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
