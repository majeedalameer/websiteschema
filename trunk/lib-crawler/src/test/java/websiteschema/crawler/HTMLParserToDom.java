/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler;

import com.gargoylesoftware.htmlunit.TextPage;
import java.util.Stack;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Administrator
 */
public class HTMLParserToDom {

    public Document[] HTMLConvert(NodeList nodelist) throws ParserConfigurationException, ParserException {
        if (0 == nodelist.size()) {
            return null;
        }
        //TextPage text = (TextPage) page;
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element html = null;
        Element body = null;
        
        NodeIterator nodeiterator = nodelist.elements();
        while(nodeiterator.hasMoreNodes()){
            Node node = nodeiterator.nextNode();
            if( node instanceof Tag){
               System.err.println(node.getText()); 
            }
            if( node.getChildren()!=null ){
                nodeiterator = node.getChildren().elements();
            }               
        }
          
        Node iter_node = nodelist.elementAt(0);
        NodeList nodelist1 = iter_node.getChildren();
//        System.out.println(iter_node.getChildren().size());
//        for (int j = 0; j < iter_node.getChildren().size(); j++) {
//            if (iter_node.getChildren().elementAt(j) instanceof Tag) {
//               //System.err.println(((Tag) iter_node.getChildren().elementAt(j)).getTagName());
//            } else {
//                System.err.println(iter_node.getChildren().elementAt(j).getClass().getName());
//            }
//            //System.out.println(" **** " + iter_node.getChildren().elementAt(j).toHtml());
//        }

        //nodelist.elementAt(0).getChildren().elementAt(0).getFirstChild().toPlainTextString();
        html = doc.createElement("HTML");
        for (int i = 0; i < nodelist1.size(); i++) {
            body = doc.createElement("BODY");
            body.setTextContent(nodelist1.elementAt(i).toPlainTextString());
            html.appendChild(body);
        }
        doc.appendChild(html);
        //body.setTextContent(text.getContent());
        return new Document[]{doc};
    }
}
