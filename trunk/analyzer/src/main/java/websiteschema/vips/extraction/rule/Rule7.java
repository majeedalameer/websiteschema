/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.vips.extraction.rule;

import com.webrenderer.swing.dom.IElement;
import websiteschema.analyzer.context.BrowserContext;

/**
 * Rule7: If the background color of this node is different from one of its children's, divided this node.
 * @author ray
 */
public class Rule7 extends AbstractRule {

    BrowserContext context;
    String referrer;

    public Rule7() {
        this(null, null);
    }

    public Rule7(BrowserContext context, String referrer) {
        this.context = context;
        this.referrer = referrer;
    }

    @Override
    public boolean match(IElement ele, int level) {
        if (nodeFeature.hasDifferentBackgroundColorWithChild(ele, context, referrer)) {
            return true;
        } else {
            return false;
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
}
