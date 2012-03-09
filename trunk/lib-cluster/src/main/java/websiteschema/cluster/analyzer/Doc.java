/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.element.W3CDOMUtil;

/**
 *
 * @author ray
 */
public class Doc {

    private Map<String, Collection<String>> data;
    private Map<String, Collection<Map<String, String>>> extData;

    public Doc() {
        data = new HashMap<String, Collection<String>>();
        extData = new HashMap<String, Collection<Map<String, String>>>();
    }

    public Doc(Document doc) {
        this();
        parseDocument(doc);
    }

    private void parseDocument(Document doc) {
        Element root = doc.getDocumentElement();
        if ("DOCUMENT".equalsIgnoreCase(root.getNodeName())) {
            NodeList children = root.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (Node.ELEMENT_NODE == child.getNodeType()) {
                    String name = child.getNodeName();
                    String text = W3CDOMUtil.getInstance().getNodeText(child);
                    addField(name.toUpperCase(), text);
                }
            }
        }
    }

    public Set<String> keySet() {
        return data.keySet();
    }

    public Collection<String> getValues(String field) {
        if (null != field) {
            return data.get(field.toUpperCase());
        }
        return null;
    }

    public Collection<Map<String, String>> getExtValues(String field) {
        if (null != field) {
            return extData.get(field.toUpperCase());
        }
        return null;
    }

    public String getValue(String field) {
        Collection<String> values = getValues(field);
        if (null != values) {
            return values.iterator().next();
        }
        return null;
    }

    public String getExtValue(String field) {
        Collection<String> values = getValues(field);
        if (null != values) {
            return values.iterator().next();
        }
        return null;
    }

    public void setValues(String field, Collection<String> values) {
        if (null != field && null != values) {
            field = field.toUpperCase();
            data.put(field, values);
        }
    }

    public void setExtValues(String field, Collection<Map<String, String>> values) {
        if (null != field && null != values) {
            field = field.toUpperCase();
            extData.put(field, values);
        }
    }

    public void remove(String field) {
        data.remove(field);
        extData.remove(field);
    }

    public void remove(String field, String value) {
        if (null != field && null != value) {
            field = field.toUpperCase();
            if (data.containsKey(field)) {
                Collection<String> values = data.get(field);
                if (null != values) {
                    values.remove(value);
                }
            }
        }
    }

    public void addField(String field, String value) {
        if (null != field && null != value) {
            field = field.toUpperCase();
            if (data.containsKey(field)) {
                Collection<String> values = data.get(field);
                if (null != values) {
                    if (!values.contains(value)) {
                        values.add(value);
                    }
                } else {
                    values = new ArrayList<String>();
                    values.add(value);
                    data.put(field, values);
                }
            } else {
                Collection<String> values = new ArrayList<String>();
                values.add(value);
                data.put(field, values);
            }
        }
    }

    // not been tested!
    @Deprecated
    public void addValues(String field, Collection<String> values) {
        if (null != field && null != values) {
            field = field.toUpperCase();
            Collection<String> tmpValues = null;
            if (data.containsKey(field)) {
                tmpValues = data.get(field);
                if (null == tmpValues) {
                    tmpValues = new ArrayList<String>();
                }
            } else {
                tmpValues = new ArrayList<String>();
            }
            tmpValues.addAll(values);
            data.put(field, tmpValues);
        }
    }

    public void addExtField(String field, Map<String, String> value) {
        if (null != field && null != value) {
            field = field.toUpperCase();
            if (extData.containsKey(field)) {
                Collection<Map<String, String>> values = extData.get(field);
                if (null != values) {
                    if (!values.contains(value)) {
                        values.add(value);
                    }
                } else {
                    values = new ArrayList<Map<String, String>>();
                    values.add(value);
                    extData.put(field, values);
                }
            } else {
                Collection<Map<String, String>> values = new ArrayList<Map<String, String>>();
                values.add(value);
                extData.put(field, values);
            }
        }
    }

    public Document toW3CDocument() {
        Document ret = create();
        Element root = ret.createElement("DOCUMENT");
        ret.appendChild(root);

        //添加抽取结果到doc中
        for (String field : data.keySet()) {
            Collection<String> list = data.get(field);
            if (null != list) {
                for (String value : list) {
                    Element ele = ret.createElement(field);
                    ele.setTextContent(value);
                    root.appendChild(ele);
                }
            }
        }

        for (String field : extData.keySet()) {
            Collection<Map<String, String>> list = extData.get(field);
            if (null != list && !list.isEmpty()) {
                for (Map<String, String> obj : list) {
                    Element ele = ret.createElement(field);

                    for (String key : obj.keySet()) {
                        String value = obj.get(key);
                        Element child = ret.createElement(key);
                        child.setTextContent(value);
                        ele.appendChild(child);
                    }

                    root.appendChild(ele);
                }
            }
        }

        return ret;
    }

    private Document create() {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.newDocument();
            return doc;
        } catch (Exception ex) {
            return null;
        }
    }
}
