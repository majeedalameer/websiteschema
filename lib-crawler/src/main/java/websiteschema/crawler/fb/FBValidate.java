/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.element.W3CDOMUtil;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EI(name = {"EI:VALID"})
@EO(name = {"YES", "NO"})
@Description(desc = "检查文档中是否包含必要的字段")
public class FBValidate extends FunctionBlock {

    @DI(name = "MUSTHAVE", desc = "抽取后的内容")
    List<String> listNotEmpty = null;
    @DI(name = "DOC", desc = "抽取后的内容")
    Doc doc = null;
    @DO(name = "REASON", relativeEvents = {"NO"})
    String reason = "";

    @Algorithm(name = "VALID", desc = "检查是否包含必须的字段")
    public void validateEmpty() {
        if (isValid()) {
            triggerEvent("YES");
        } else {
            triggerEvent("NO");
        }
    }

    boolean isValid() {
        boolean valid = true;
        if (null != doc && null != listNotEmpty) {
            Set<String> fields = new HashSet<String>(listNotEmpty);
            Element root = doc.toW3CDocument().getDocumentElement();
            NodeList children = root.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (Node.ELEMENT_NODE == child.getNodeType()) {
                    String name = child.getNodeName();
                    String text = W3CDOMUtil.getInstance().getNodeText(child);
                    if (fields.contains(name)) {
                        if (null == text || "".equals(text)) {
                            reason = name + " has no value";
                            valid = false;
                        } else {
                            fields.remove(name);
                        }
                    }
                }
            }
            if (valid && !fields.isEmpty()) {
                valid = false;
                reason = fields.toString() + " were not existed";
            }
        } else {
            valid = false;
        }
        return valid;
    }
}
