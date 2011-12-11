/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.analyzer.browser.utils;

/**
 *
 * @author ray
 */
public class TextAreaSearch {

    private javax.swing.JTextArea sourceArea;

    public TextAreaSearch(javax.swing.JTextArea area) {
        sourceArea = area;
    }

    public String getSource() {
        return this.sourceArea.getText();
    }

    public void last(String target) {
        // TODO add your handling code here:
        String source = getSource();
        int pos = sourceArea.getCaretPosition();
        if (null != source) {
            int at = source.lastIndexOf(target, pos > 0 ? pos - 1 : source.length() - 1);
            if (at >= 0) {
                sourceArea.setCaretPosition(at);

            } else {
                at = source.lastIndexOf(target, source.length() - 1);
                if (at >= 0) {
                    sourceArea.setCaretPosition(at);
                }
            }
        }
    }

    public void next(String target) {
        // TODO add your handling code here:
        String source = getSource();
        int pos = sourceArea.getCaretPosition();
        if (null != source) {
            int at = source.indexOf(target, pos + 1);
            if (at >= 0) {
                sourceArea.setCaretPosition(at);
            } else {
                at = source.indexOf(target, 0);
                if (at >= 0) {
                    sourceArea.setCaretPosition(at);
                }
            }
        }
    }
}
