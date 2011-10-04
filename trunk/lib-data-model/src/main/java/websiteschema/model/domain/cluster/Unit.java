/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain.cluster;

/**
 *
 * @author ray
 */
public class Unit {

    public String xpath;
    public String text;

    public Unit() {
        xpath = null;
        text = null;
    }

    public Unit(String xpath, String text) {
        this.xpath = xpath;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }
}
