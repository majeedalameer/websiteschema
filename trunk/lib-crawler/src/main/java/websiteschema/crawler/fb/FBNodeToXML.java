/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.util.ArrayList;
import java.util.List;
;
import javax.xml.xpath.*;
import org.jaxen.XPathSyntaxException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import websiteschema.element.DocumentUtil;
import websiteschema.fb.annotation.*;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author mupeng
 */


@EI(name = {"EI:EXT"})
@EO(name = {"EO"})
@Description(desc = "抽取页面中一个信息单元的XML")
public class FBNodeToXML extends FunctionBlock {

    @DI(name = "DOC", desc = "要抽取的文档")
    public Document doc;
    @DI(name = "UPATH", desc = "Unit XPath")
    public String unitXPath;
    @DI(name = "URL", desc = "文档来自的URL")
    public String url;
    @DO(name = "OUT", relativeEvents = {"EO"})
    public List<String> table;
    XPathExpression[] xPathExps = null;
    static final XPath X_PATH = XPathFactory.newInstance().newXPath();

    @Algorithm(name = "EXT")
    public void extractUnits() {
        try {
            table = assembleUnits(unitXPath);
            triggerEvent("EO");
        } catch (XPathSyntaxException e) {
            l.debug(e.getMultilineMessage());
            l.debug("[exception] " + url + " upath:" + unitXPath);
        } catch (XPathExpressionException e) {
            l.debug("[exception] " + url, e);
        } catch (Exception ex) {
            l.debug("[exception] " + url, ex);
        }
    }

    private List<Node> getUnits(Document doc, String unitXPath) throws Exception {
        return DocumentUtil.getByXPath(doc, unitXPath);
    }

    // 组装信息单元
    private List<String> assembleUnits(String unitConf) throws Exception {
        List<Node> units = getUnits(doc, unitConf);
        if (null != units) {
            List<String> ret = new ArrayList<String>();
            for (int i = 0; i < units.size(); ++i) {
                Node iterNode = units.get(i);
                String row = extractRow(iterNode);
                if (null != row) {
                    ret.add(row);
                }
            }
            return ret;
        }
        return null;
    }

    private String extractRow(Node iterNode) throws XPathExpressionException {
        StringBuffer sbuf = new StringBuffer();
        try {
            String ret = DocumentUtil.getHtmlStringByNode(iterNode);
            if (ret.length() > 0) {
                String[] tmp = ret.split("\n");
                for (int i = 1; i < tmp.length - 1; i++) {
                    sbuf.append(tmp[i]);
                    sbuf.append("\n");
                }
            }
        } catch (RuntimeException e) {
            l.error("运行错误：", e);
        }

        return sbuf.toString();
    }
}
