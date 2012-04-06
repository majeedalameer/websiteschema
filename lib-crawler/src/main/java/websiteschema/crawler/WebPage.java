/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler;

import org.w3c.dom.Document;

/**
 *
 * @author ray
 */
public class WebPage {

    String url;
    String[] htmlSource;
    Document[] docs;

    public Document[] getDocs() {
        return docs;
    }

    public void setDocs(Document[] docs) {
        this.docs = docs;
    }

    public String[] getHtmlSource() {
        return htmlSource;
    }

    public void setHtmlSource(String[] htmlSource) {
        this.htmlSource = htmlSource;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
