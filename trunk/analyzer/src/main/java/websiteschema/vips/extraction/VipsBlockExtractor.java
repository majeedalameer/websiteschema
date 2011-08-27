/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips.extraction;

import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import java.util.Set;
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
public class VipsBlockExtractor implements BlockExtractor {

    static final Set<String> inlineNodeSet = Configure.getDefaultConfigure().getSetProperty("VIPS", "InlineNodeName");
    static final RectangleFactory rectangleFactory = new RectangleFactory();
    BrowserContext context;
    String referrer;
    double threshold = 0.1;
    double pageSize = 0.0;

    @Override
    public int dividable(IElement ele, int level) {
        int ret = BlockExtractor.UnDividable;

        if (!isTextNode(ele) && !hasValidChildren(ele)) {
            //Rule 1: if the DOM node is not a text node and it has no valid children,
            //        then this node cannot be divided and will be cut.
            return BlockExtractor.Cut;
        }
        if (1 == howManyChildren(ele)) {
            IElementCollection children = ele.getChildElements();
            IElement onlyChild = children.item(0);
            if (!isTextNode(onlyChild)) {
                //Rule2: If the DOM node has only child and the child is not a text node, then divide the node.
                return BlockExtractor.Dividable;
            }
        }
        //Rule3: If the DOM node is the root node of the sub-DOM tree (corresponding to the block),
        //       and there is only one sub DOM tree corresponding to this block, divide this node.

        if (areChildrenVirtualTextNode(ele)) {
            //Rule4: If all of the child nodes of the DOM node are text nodes or virtual text nodes, do not divide the node.
            return BlockExtractor.UnDividable;
        }

        if (hasLineBreakChildNode(ele)) {
            //Rule5: If one of the child nodes of the DOM node is line-break node, then divide this DOM node.
            return BlockExtractor.Dividable;
        }

        if (isChildNodeHRTag(ele)) {
            //Rule6: If one of the child nodes of the DOM node has HTML tag <HR>, then divide this DOM node.
            return BlockExtractor.Dividable;
        }

        if (hasDifferentBackgroundColorWithChild(ele)) {
            //Rule7: If the background color of this node is different from one of its children's, divided this node.
            return BlockExtractor.Dividable;
        }

        double relativeSize = getRelativeSize(ele);
        if (relativeSize < threshold) {
            if (hasTextOrVirtualTextNode(ele)) {
                //Rule8: If the node has at least one text node child or at least one virtual text node child,
                //       and the node's relative size is smaller than a threshold, the the node can not be divided.
                return BlockExtractor.UnDividable;
            }
        }

        return ret;
    }

    public void analysisElement(IElement ele) {
        System.out.println("is inline node: " + isInlineNode(ele));
        System.out.println("is valid node: " + isValidNode(ele));
        System.out.println("is text node: " + isTextNode(ele));
        System.out.println("is virtual text node: " + isVirtualTextNode(ele));
        System.out.println("has valid children: " + hasValidChildren(ele));
        System.out.println("how many children the element has: " + howManyChildren(ele));
        System.out.println("Does all of child node are virtual text node?: " + areChildrenVirtualTextNode(ele));
        System.out.println("one of child node is Line-Break node: " + hasLineBreakChildNode(ele));
        System.out.println("one of child node is <HR> tag: " + isChildNodeHRTag(ele));
        System.out.println("has differenc background: " + hasDifferentBackgroundColorWithChild(ele));
        System.out.println("relative size: " + getRelativeSize(ele));
        System.out.println("has text or virtual text node: " + hasTextOrVirtualTextNode(ele));
        //hasTextOrVirtualTextNode
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
    private boolean isInlineNode(IElement ele) {
        return inlineNodeSet.contains(ele.getTagName());
    }

    /**
     * A node that can be seen through the browser. The node's width and height are not equal to zero.
     * @param ele
     * @return
     */
    private boolean isValidNode(IElement ele) {
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
    private boolean hasValidChildren(IElement ele) {
        IElementCollection children = ele.getChildElements();
        if (null != children && children.length() > 0) {
            for (int i = 0; i < children.length(); i++) {
                if (hasValidChildren(children.item(i))) {
                    return true;
                }
            }
            return false;
        } else {
            return isValidNode(ele);
        }
    }

    /**
     * Does all of child node are virtual text node?
     * @param ele
     * @return
     */
    private boolean areChildrenVirtualTextNode(IElement ele) {
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
    private boolean hasLineBreakChildNode(IElement ele) {
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
    private boolean hasTextOrVirtualTextNode(IElement ele) {
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
    private boolean isChildNodeHRTag(IElement ele) {
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
    private boolean hasDifferentBackgroundColorWithChild(IElement ele) {
        StyleSheet styleSheets = context.getStyleSheet(referrer);
        CSSProperties css = new StyleSheetFactory().createCSSProperties(styleSheets, ele);
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
    private double getRelativeSize(IElement ele) {
        Rectangle rect = rectangleFactory.create(ele);
        double size = rect.getHeight() * rect.getWidth();
        return size / pageSize;
    }

    /**
     * How many children the element has?
     * @param ele
     * @return
     */
    private int howManyChildren(IElement ele) {
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
    private boolean isTextNode(IElement ele) {
        return ele.isTextNode();
    }

    /**
     * 1. Inline node with only text node children is a virtual text node.<br/>
     * 2. Inline node with only text node and virtual text node children is a virtual text node.
     * @param ele
     * @return
     */
    private boolean isVirtualTextNode(IElement ele) {
        if (!isInlineNode(ele) && !isTextNode(ele)) {
            return false;
        } else {
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

    public BrowserContext getContext() {
        return context;
    }

    public void setContext(BrowserContext context) {
        this.context = context;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public double getPageSize() {
        return pageSize;
    }

    public void setPageSize(double pageSize) {
        this.pageSize = pageSize;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
