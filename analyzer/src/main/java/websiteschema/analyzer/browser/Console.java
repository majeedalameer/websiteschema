/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.analyzer.browser;

import javax.swing.JTextArea;

/**
 *
 * @author ray
 */
public class Console {

    JTextArea textArea;

    public Console(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void log(String info) {
        textArea.append(info);
        textArea.append("\n");
    }

    public void print(String info) {
        textArea.append(info);
    }

    public void println() {
        textArea.append("\n");
    }

    public void println(String info) {
        log(info);
    }

}
