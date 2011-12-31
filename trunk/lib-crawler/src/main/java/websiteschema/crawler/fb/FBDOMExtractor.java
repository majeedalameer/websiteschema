/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import websiteschema.utils.PojoMapper;
import websiteschema.cluster.analyzer.IFieldAnalyzer;
import java.util.List;
import websiteschema.model.domain.Websiteschema;
import websiteschema.common.base.Function;
import java.util.HashSet;
import websiteschema.element.factory.XPathAttrFactory;
import websiteschema.element.XPathAttributes;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Node;
import websiteschema.fb.annotation.DO;
import java.util.Set;
import java.util.Map;
import org.w3c.dom.Document;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import static websiteschema.utils.PojoMapper.*;
import static websiteschema.common.base.Traversal.*;

/**
 *
 * @author ray
 */
@EO(name = {"EO"})
@EI(name = {"EXTRACT:EXT"})
public class FBDOMExtractor extends FunctionBlock {

    private Map<String, String> prop;
    private XPathAttributes xpathAttr;
    @DI(name = "IN")
    public Document in;
    @DI(name = "SCHEMA")
    public Websiteschema schema;
    @DO(name = "OUT", relativeEvents = {"EO"})
    public Document out;

    @Algorithm(name = "EXT")
    public void extract() {
        try {
            prop = schema.getProperties();
            xpathAttr = schema.getXpathAttr();
            String vnode = prop.get("ValidNodes");
            Set<String> validNodes = null != vnode ? (Set<String>) fromJson(vnode, Set.class) : null;
            String ivnode = prop.get("InvalidNodes");
            Set<String> invalidNodes = null != vnode ? (Set<String>) fromJson(ivnode, Set.class) : null;
            toUppercase(validNodes);
            toUppercase(invalidNodes);
            //抽取正文
            out = extract(validNodes, invalidNodes);
            //抽取其他标签
            String fa = prop.get("FieldAnalyzers");
            Map<String, String> fieldAnalyzers = null != vnode ? (Map<String, String>) fromJson(fa, Map.class) : null;
            extractFields(in, out, fieldAnalyzers);
            this.triggerEvent("EO");
        } catch (Exception ex) {
            ex.printStackTrace();
            l.error(ex);
        }
    }

    public Set<String> toUppercase(Set<String> set) {
        Set<String> tmp = new HashSet<String>();
        for (String str : set) {
            tmp.add(str.toUpperCase());
        }
        set.clear();
        set.addAll(tmp);
        return set;
    }

    private boolean isTextNode(Node node) {
        return Node.TEXT_NODE == node.getNodeType();
    }

    /**
     * 根据配置抽取每一个字段
     * @param in
     * @param fieldAnalyzerNames
     */
    private void extractFields(Document in, Document out, Map<String, String> fieldAnalyzerNames) {
        for (String fieldName : fieldAnalyzerNames.keySet()) {
            String clazzName = fieldAnalyzerNames.get(fieldName);
            //创建字段抽取器
            IFieldAnalyzer analyzer = createFieldAnalyzer(clazzName);
            //读取字段抽取器的配置，这是一个List<Map>的配置
            String configStr = prop.get(fieldName);
            try {
                List<Map<String, String>> listConfig = PojoMapper.fromJson(configStr, List.class);
                //对每一个配置都尝试抽取
                for (Map<String, String> config : listConfig) {
                    analyzer.init(config);
                    //开始抽取
                    Set<String> result = analyzer.extract(in);
                    if (null != result && !result.isEmpty()) {
                        Element root = out.getDocumentElement();
                        //添加抽取结果到doc中
                        for (String res : result) {
                            Element ele = out.createElement(fieldName);
                            ele.setTextContent(res);
                            root.appendChild(ele);
                        }
                        //如果抽取到结果，就结束抽取
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private IFieldAnalyzer createFieldAnalyzer(String clazzName) {
        try {
            Class clazz = Class.forName(clazzName);
            IFieldAnalyzer analyzer = (IFieldAnalyzer) clazz.newInstance();
            return analyzer;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Document extract(final Set<String> validNodes, final Set<String> invalidNodes) {
        if (null != in) {
            final StringBuilder content = new StringBuilder();
            traversal(in.getDocumentElement(), new Function<Node>() {

                @Override
                public void invoke(Node node) {
//                    System.out.println("----" + XPathAttrFactory.getInstance().create(node, xpathAttr));
                    if (isTextNode(node)) {
                        Node father = node.getParentNode();
                        String xpath = XPathAttrFactory.getInstance().create(father, xpathAttr).toUpperCase();
                        if (!invalidNodes.contains(xpath)) {
                            if (validNodes.contains(xpath)) {
                                String nodeName = father.getNodeName();
                                String text = node.getNodeValue();
                                content.append(node.getNodeValue());
                                if ("P".equalsIgnoreCase(nodeName)) {
                                    if (!text.endsWith("\n")) {
                                        content.append("\n");
                                    }
                                }
                            }
                        }
                    } else {
                        String nodeName = node.getNodeName();
                        if ("BR".equalsIgnoreCase(nodeName)) {
                            content.append("\n");
                        }
                    }
                }
            });

            try {
                DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = domFactory.newDocumentBuilder();
                Document doc = builder.newDocument();
                Element eleRoot = doc.createElement("DOCUMENT");
                Element eleContent = doc.createElement("DRECONTENT");
                eleContent.setTextContent(content.toString());
                eleRoot.appendChild(eleContent);
                doc.appendChild(eleRoot);
                return doc;
            } catch (Exception ex) {
                l.error("Can not create Document: ", ex);
            }
        }
        return null;
    }
}
