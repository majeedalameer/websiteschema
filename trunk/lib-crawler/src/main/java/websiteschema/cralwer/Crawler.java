/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cralwer;

import org.w3c.dom.Document;

/**
 *
 * @author ray
 */
public interface Crawler {

    public Document[] crawl(String url);

    public void stopLoad();

    public String getUrl();

    public String[] getLinks();

    public void setEncoding(String encoding);

    public void setProxy(String server, int port);

    public void executeScript(String javascriptBody);

    public void setLoadImage(boolean yes);

    public void setLoadEmbeddedFrame(boolean yes);

    public void setAllowPopupWindow(boolean yes);

}
