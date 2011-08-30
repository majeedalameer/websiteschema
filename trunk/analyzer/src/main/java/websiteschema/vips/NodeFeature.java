/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips;

import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import java.util.Set;
import org.w3c.dom.Node;
import websiteschema.context.BrowserContext;
import websiteschema.element.CSSProperties;
import websiteschema.element.Rectangle;
import websiteschema.element.StyleSheet;
import websiteschema.element.factory.RectangleFactory;
import websiteschema.element.factory.StyleSheetFactory;
import websiteschema.utils.Configure;

/**
 *
 * @author ray
 */
public class NodeFeature {

    static final Set<String> inlineNodeSet = Configure.getDefaultConfigure().getSetProperty("VIPS", "InlineNodeName");
    static final RectangleFactory rectangleFactory = new RectangleFactory();
    static final StyleSheetFactory styleSheetFactory = new StyleSheetFactory();
    static final NodeFeature instance = new NodeFeature();

    public static NodeFeature getInstance() {
        return instance;
    }

    /**
     * Inline Node: The DOM node with inline text HTML tags, which affect the appearance of text <br/>
     * and can be applied to string of characters without introducing line break.<br/>
     * Such tags include: B, BIG, EM, FONT, I, STRONG, U, etc.<br/>
     * Line-Break Node: The node with tag other than inline text tags.<br/>
     * Such tags := !isInlineNode(ele);
     * @param ele
     * @return
     */
    public boolean isInlineNode(IElement ele) {
        return inlineNodeSet.contains(ele.getTagName());
    }

    /**
     * A node that can be seen through the browser. The node's width and height are not equal to zero.
     * @param ele
     * @return
     */
    public boolean isValidNode(IElement ele) {
        if (ele.isTextNode()) {
            return true;
        } else {
            Rectangle rect = rectangleFactory.create(ele);
            return rect.getHeight() != 0 || rect.getWidth() != 0;
        }
    }

    /**
     * A node has at least one valid child.
     * @param ele
     * @return
     */
    public boolean hasValidChildren(IElement ele) {
        IElementCollection children = ele.getChildElements();
        if (null != children && children.length() > 0) {
            for (int i = 0; i < children.length(); i++) {
                if (isValidNode(children.item(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Does all of child node are virtual text node?
     * @param ele
     * @return
     */
    public boolean areChildrenVirtualTextNode(IElement ele) {
        IElementCollection children = ele.getChildElements();
        if (null != children && children.length() > 0) {
            for (int i = 0; i < children.length(); i++) {
                if (!isVirtualTextNode(children.item(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Return true if one of the child nodes of the DOM node is line-break node.
     * @param ele
     * @return
     */
    public boolean hasLineBreakChildNode(IElement ele) {
        IElementCollection children = ele.getChildElements();
        if (null != children && children.length() > 0) {
            for (int i = 0; i < children.length(); i++) {
                if (!isInlineNode(children.item(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return true if one of the child nodes of the DOM node is text node or virtual text node.
     * @param ele
     * @return
     */
    public boolean hasTextOrVirtualTextNode(IElement ele) {
        IElementCollection children = ele.getChildElements();
        if (null != children && children.length() > 0) {
            for (int i = 0; i < children.length(); i++) {
                if (isTextNode(children.item(i)) || isVirtualTextNode(children.item(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return true if one of the child nodes if the DOM node has HTML tag HR.
     * @param ele
     * @return
     */
    public boolean isChildNodeHRTag(IElement ele) {
        IElementCollection children = ele.getChildElements();
        if (null != children && children.length() > 0) {
            for (int i = 0; i < children.length(); i++) {
                IElement child = children.item(i);
                if (!isTextNode(child) && "HR".equalsIgnoreCase(child.getTagName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return true if the background color of this node is different from one of its children's.
     * @param ele
     * @return
     */
    public boolean hasDifferentBackgroundColorWithChild(IElement ele, BrowserContext context, String referrer) {
        StyleSheet styleSheets = context.getStyleSheet(referrer);
        CSSProperties css = styleSheetFactory.createCSSProperties(styleSheets, ele);
        String backgroundColor = css.get("background-color");
        String background = css.get("background");
        IElementCollection children = ele.getChildElements();
        if (null != children && children.length() > 0) {
            for (int i = 0; i < children.length(); i++) {
                IElement child = children.item(i);
                if (!isTextNode(child) && isValidNode(child)) {
                    CSSProperties childCSS = new StyleSheetFactory().createCSSProperties(styleSheets, child);
                    String backgroundColor1 = childCSS.get("background-color");
                    String background1 = childCSS.get("background");
                    if (!equal(backgroundColor, backgroundColor1) || !equal(background, background1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean equal(String str1, String str2) {
        if (null != str1) {
            // str1 != null, compare(str1, str2)
            return str1.equals(str2);
        } else if (null != str2) {
            // str1 = null, str2 != null;
            return false;
        } else {
            // str1 = null, str2 = null.
            return true;
        }
    }

    /**
     * return the relative size between element and page.
     * @param ele
     * @return
     */
    public double getRelativeSize(IElement ele, double pageSize) {
        Rectangle rect = rectangleFactory.create(ele);
        double size = rect.getHeight() * rect.getWidth();
        return size / pageSize;
    }

    /**
     * How many children the element has?
     * @param ele
     * @return
     */
    public int howManyChildren(IElement ele) {
        IElementCollection children = ele.getChildElements();
        if (null != children && children.length() > 0) {
            return children.length();
        } else {
            return 0;
        }
    }

    /**
     * The DOM node corresponding to free text, which does not have and html tag.
     * @param ele
     * @return
     */
    public boolean isTextNode(IElement ele) {
        return ele.isTextNode();
    }

    /**
     * 1. Inline node with only text node children is a virtual text node.<br/>
     * 2. Inline node with only text node and virtual text node children is a virtual text node.
     * @param ele
     * @return
     */
    public boolean isVirtualTextNode(IElement ele) {
        if (!isInlineNode(ele) && !isTextNode(ele)) {
            return false;
        } else {
//            String tagName = ele.getTagName();
//            Node node = ele.convertToW3CNode();
//            int nodeType = node.getNodeType();
//            System.out.println(tagName + " - " + nodeType);
            IElementCollection children = ele.getChildElements();
            if (null != children && children.length() > 0) {
                for (int i = 0; i < children.length(); i++) {
                    if (!isVirtualTextNode(children.item(i))) {
                        return false;
                    }
                }
                return true;
            } else {
                return isTextNode(ele);
            }
        }
    }
}
