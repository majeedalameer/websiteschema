/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.element.factory;

import com.webrenderer.swing.dom.IElement;
import websiteschema.element.XPathAttributes;

/**
 * 根据指定的设置（XPathAttributes），生成指定元素的xpath。
 * @author ray
 */
public class XPathFactory {

    public String create(IElement ele, XPathAttributes attr) {
        return getXPath(ele, attr);
    }

    private String getXPath(IElement ele, XPathAttributes attr) {
        IElement pEle = ele.getParentElement();
        String xpath = getElementXPath(ele, attr);
        if (pEle != null) {
            xpath = getXPath(pEle, attr) + "/" + xpath;
        }

        return xpath;
    }

    private String getElementXPath(IElement ele, XPathAttributes attr) {
        String xpath = "";
        String tagName = ele.getTagName();
        if (null != tagName) {
            xpath = "" + tagName;
            String attrKeyValues = "";
            //如果要用位置来表示XPATH上的节点，则就没有必要使用Id和Class了。
            if (attr.isUsingPosition()) {
                //按照元素的顺序生成XPath
                int pos = (int) ele.getSourceIndex();
                attrKeyValues = String.valueOf(pos);
            } else {
                if (attr.isUsingClass()) {
                    //将class属性增加到XPath中
                    String className = ele.getClassName();
                    if(null != className && !"".equals(className)) {
                        attrKeyValues += " @class='" + className + "'";
                    }
                }
                if (attr.isUsingId()) {
                    //将id属性增加到XPath中
                    String id = ele.getId();
                    if(null != id && !"".equals(id)) {
                        attrKeyValues += " @id='" + id + "'";
                    }
                }
                String specifyAttr = attr.getSpecifyAttr();
                if(null != specifyAttr && !"".equals(specifyAttr)) {
                    //将指定的属性增加到XPath中
                    String attrValue = ele.getAttribute(specifyAttr, 1);
                    if(null != attrValue && !"".equals(attrValue)) {
                        attrKeyValues += " @"+specifyAttr+"='" + attrValue + "'";
                    }
                }
            }
            if(!"".equals(attrKeyValues)) {
                xpath += "[" + attrKeyValues.trim() + "]";
            }
        }
        return xpath;
    }
}
