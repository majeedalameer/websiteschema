/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.util;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import websiteschema.fb.BasicFuncBlock;
import websiteschema.fb.task.DataFlow;
import websiteschema.fb.task.EventFlow;
import websiteschema.fb.task.FBManager;
import websiteschema.fb.task.SubTask;

/**
 *
 * @author ray
 */
public class FBConfigureUtil {

    private static final FBConfigureUtil u = new FBConfigureUtil();

    public static FBConfigureUtil getFBConfigureUtil() {
        return u;
    }

    private Document parseXmlFile(File f) {
        Document dom = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse(f);
        } catch (SAXException ex) {
            Logger.getLogger(FBConfigureUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FBConfigureUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(FBConfigureUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dom;
    }

    public SubTask initTask(String file) {
        Document doc = null;
        SubTask task = new SubTask();
        doc = parseXmlFile(new File(file));
        parseFBConfig(doc, task);
        return task;
    }

    private void parseFBConfig(Node node, SubTask task) {
        if (null == node) {
            return;
        }
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {

                String name = n.getNodeName();
                if ("FB".equals(name)) {
                    NamedNodeMap attrs = n.getAttributes();
                    String fbName = attrs.getNamedItem("Name").getNodeValue();
                    String fbType = attrs.getNamedItem("Type").getNodeValue();
                    BasicFuncBlock fb = createFB(fbType);
                    FBManager manager = task.getFbManager();
                    manager.put(fbName, fb);
                } else if ("Connection".equals(name)) {
                    if (n.getParentNode().getNodeName().startsWith("Event")) {
                        NamedNodeMap attrs = n.getAttributes();
                        String src = attrs.getNamedItem("Source").getNodeValue();
                        String des = attrs.getNamedItem("Destination").getNodeValue();
                        EventFlow ef = task.getEvents();
                        ef.addEventConnection(src, des);
                    } else if (n.getParentNode().getNodeName().startsWith("Data")) {
                        NamedNodeMap attrs = n.getAttributes();
                        String src = attrs.getNamedItem("Source").getNodeValue();
                        String des = attrs.getNamedItem("Destination").getNodeValue();
                        DataFlow df = task.getDatas();
                        df.addDataConnection(src, des);
                    }
                }

                System.out.println(name);
                parseFBConfig(n, task);
            }
        }
    }

    private BasicFuncBlock createFB(String type) {
        BasicFuncBlock fb = null;
        try {
            Class c = Class.forName(type);
            fb = (BasicFuncBlock) c.newInstance();
        } catch (Exception ex) {
            Logger.getLogger(FBConfigureUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fb;
    }
}
