/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.crawler;

import org.w3c.dom.Document;

/**
 *
 * @author ray
 */
public interface Crawler {

    public Document crawl(String url);

    public String getUrl();

    public void setEncoding(String encoding);

    public void executeScript(String javascriptBody);
}
