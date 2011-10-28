/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.element;

import org.junit.Test;
import static websiteschema.element.DocumentUtil.*;
import static org.junit.Assert.*;

/**
 *
 * @author ray
 */
public class DocumentTest {

    @Test
    public void testXPath() {
        assertEquals(buildXPath("//html", "pre"), "//pre:html");
        assertEquals(buildXPath("html", "pre"), "pre:html");
        assertEquals(buildXPath("html[@id='attr']/meta", "pre"), "pre:html[@id='attr']/pre:meta");
        assertEquals(buildXPath("//@id", "pre"), "//@id");
        assertEquals(buildXPath("//*", "pre"), "//*");
        assertEquals(buildXPath("//p/a/*", "pre"), "//pre:p/pre:a/*");
        assertEquals(buildXPath("//@id/p", "pre"), "//@id/pre:p");
        assertEquals(buildXPath("//a/p/@id", "pre"), "//pre:a/pre:p/@id");
        assertEquals(buildXPath("//html/META[@id='abc']", "pre"), "//pre:html/pre:META[@id='abc']");
        assertEquals(buildXPath("/html/META[@id='abc']", "pre"), "/pre:html/pre:META[@id='abc']");
        assertEquals(buildXPath("/html/META[@id='abc']/text()", "pre"), "/pre:html/pre:META[@id='abc']/text()");
        assertEquals(buildXPath("HTML/BODY/DIV[@id='wrapper']/DIV[@id='container']/DIV[@class='area']/DIV[@class='content focusnews']/DL/DD/DIV[@class='leftCont leftContMain']/UL[@class='hotnews' @id='hotnews']/LI[@class='top']/A[@class='a3']/FONT/text()", "pre"),
                "pre:HTML/pre:BODY/pre:DIV[@id='wrapper']/pre:DIV[@id='container']/pre:DIV[@class='area']/pre:DIV[@class='content focusnews']/pre:DL/pre:DD/pre:DIV[@class='leftCont leftContMain']/pre:UL[@class='hotnews' @id='hotnews']/pre:LI[@class='top']/pre:A[@class='a3']/pre:FONT/text()");
    }
}
