/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import websiteschema.fb.annotation.*;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.model.domain.Websiteschema;
import websiteschema.persistence.hbase.WebsiteschemaMapper;

/**
 *
 * @author ray
 */
@EO(name = {"SUC", "FAL"})
@EI(name = {"INIT:INIT"})
public class FBWebsiteschema extends FunctionBlock {

    @DI(name = "SITE")
    public String siteId = "";
    @DO(name = "OUT", relativeEvents = {"SUC"})
    public Websiteschema out = null;

    @Algorithm(name = "INIT")
    public void create() {
        WebsiteschemaMapper mapper = getContext().getSpringBeanFactory().getBean("websiteschemaMapper", WebsiteschemaMapper.class);
//        try {
//            test();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        WebsiteschemaMapper mapper = new WebsiteschemaMapper();
        out = mapper.get(siteId);
        if (null != out) {
            triggerEvent("SUC");
        } else {
            triggerEvent("FAL");
        }
    }

    private void test() throws Exception {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        //ignore all comments inside the xml file
        docBuilderFactory.setIgnoringComments(true);

        //allow includes in the xml file
        docBuilderFactory.setNamespaceAware(true);
        try {
            docBuilderFactory.setXIncludeAware(true);
        } catch (UnsupportedOperationException e) {
            l.error("Failed to set setXIncludeAware(true) for parser "
                    + docBuilderFactory
                    + ":" + e,
                    e);
        }
        DocumentBuilder builder = docBuilderFactory.newDocumentBuilder();
        Document doc = null;
//        URL url = FBWebsiteschema.class.getClassLoader().getResource("hbase-default.xml");
        URL url = Thread.currentThread().getContextClassLoader().getResource("hbase-default.xml");
        doc = builder.parse(url.toString());

        InputStream is = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = br.readLine();
        while(null != line) {
            System.out.println(line);
            line = br.readLine();
        }

        System.out.println("FBWebsiteschema: " + url);
        System.out.println(websiteschema.element.DocumentUtil.getXMLString(doc));
    }
}
