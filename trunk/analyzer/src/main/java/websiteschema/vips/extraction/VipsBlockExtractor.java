/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips.extraction;

import websiteschema.vips.extraction.rule.DivideRule;
import websiteschema.vips.NodeFeature;
import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import websiteschema.context.BrowserContext;
import websiteschema.element.CSSProperties;
import websiteschema.element.Rectangle;
import websiteschema.element.StyleSheet;
import websiteschema.element.XPathAttributes;
import websiteschema.element.factory.RectangleFactory;
import websiteschema.element.factory.StyleSheetFactory;
import websiteschema.element.factory.XPathFactory;
import websiteschema.utils.Configure;
import websiteschema.vips.extraction.rule.DivideRuleFactory;

/**
 *
 * @author ray
 */
public class VipsBlockExtractor implements BlockExtractor {

    Logger l = Logger.getRootLogger();
    private NodeFeature nodeFeature = NodeFeature.getInstance();
    private Configure configure;
    private Map<String, String> rules;
    private Map<String, String> ruleTypes;
    private Set<String> allDividableNode = new HashSet<String>();
    DivideRuleFactory ruleFactory = new DivideRuleFactory();

    public VipsBlockExtractor() {
        ruleFactory.setAllDividableNode(allDividableNode);
    }

    @Override
    public DivideRule dividable(IElement ele, int level) {
        DivideRule matchedRule = null;

        if (null != ele) {

            String ruleCode = "";
            if (NodeFeature.getInstance().isInlineNode(ele)) {
                ruleCode = rules.get("InlineTextNode");
            } else {
                String tagName = ele.getTagName();
                tagName = null != tagName ? tagName.toUpperCase() : "";
                Set<String> set = rules.keySet();
                if (set.contains(tagName)) {
                    ruleCode = rules.get(tagName.toUpperCase());
                } else {
                    ruleCode = rules.get("OTHER");
                }
            }

            matchedRule = execute(ruleCode, ele, level);

//        l.debug("Rule Code: " + ruleCode);
            if (matchedRule.dividable() == BlockExtractor.Dividable) {
                String xpath = XPathFactory.getInstance().create(ele);
                allDividableNode.add(xpath);
                l.trace("Dividable: " + xpath + " -- Element Type: " + ele.getTagName() + " -- Rule: " + matchedRule.getClass());
            } else {
                String xpath = XPathFactory.getInstance().create(ele);
                l.trace("Undividable: " + xpath + " -- Element Type: " + ele.getTagName() + " -- Rule: " + matchedRule.getClass());
            }
        }
        return matchedRule;
    }

    private DivideRule execute(String ruleCode, IElement ele, int level) {
        return execute(ruleCode.split(","), ele, level);
    }

    private DivideRule execute(String[] ruleCode, IElement ele, int level) {
        for (String rule : ruleCode) {
            DivideRule dr = ruleFactory.create(ruleTypes.get(rule));
            if (dr.match(ele, level)) {
                return dr;
            }
        }
        return null;
    }

    public void analysisElement(IElement ele) {
        l.debug("is inline node: " + nodeFeature.isInlineNode(ele));
        l.debug("is valid node: " + nodeFeature.isValidNode(ele));
        l.debug("is text node: " + nodeFeature.isTextNode(ele));
        l.debug("is virtual text node: " + nodeFeature.isVirtualTextNode(ele));
        l.debug("has valid children: " + nodeFeature.hasValidChildren(ele));
        l.debug("how many children the element has: " + nodeFeature.howManyChildren(ele));
        l.debug("Does all of child node are virtual text node?: " + nodeFeature.areChildrenVirtualTextNode(ele));
        l.debug("one of child node is Line-Break node: " + nodeFeature.hasLineBreakChildNode(ele));
        l.debug("one of child node is <HR> tag: " + nodeFeature.isChildNodeHRTag(ele));
        l.debug("has differenc background: " + nodeFeature.hasDifferentBackgroundColorWithChild(ele, getContext(), getReferrer()));
        l.debug("relative size: " + nodeFeature.getRelativeSize(ele, getPageSize()));
        l.debug("has text or virtual text node: " + nodeFeature.hasTextOrVirtualTextNode(ele));
        //hasTextOrVirtualTextNode
    }

    public BrowserContext getContext() {
        return ruleFactory.getContext();
    }

    public void setContext(BrowserContext context) {
        ruleFactory.setContext(context);
    }

    public String getReferrer() {
        return ruleFactory.getReferrer();
    }

    public void setReferrer(String referrer) {
        ruleFactory.setReferrer(referrer);
    }

    public double getPageSize() {
        return ruleFactory.getPageSize();
    }

    public void setPageSize(double pageSize) {
        ruleFactory.setPageSize(pageSize);
    }

    public double getThreshold() {
        return ruleFactory.getThreshold();
    }

    public void setThreshold(double threshold) {
        ruleFactory.setThreshold(threshold);
    }

    public Configure getConfigure() {
        return configure;
    }

    public void setConfigure(Configure configure) {
        this.configure = configure;
        rules = configure.getMapProperty("VIPS", "Rules");
        ruleTypes = configure.getMapProperty("VIPS", "RuleTypes");
    }
}
