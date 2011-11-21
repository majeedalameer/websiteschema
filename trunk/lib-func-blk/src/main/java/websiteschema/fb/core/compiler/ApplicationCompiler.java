/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.core.compiler;

import websiteschema.conf.Configure;

/**
 *
 * @author ray
 */
public class ApplicationCompiler {

    Configure config = null;

    public void compile(String app) {
        config = new Configure(app);
        String startFB = config.getProperty("StartFB");

        throw new UnsupportedOperationException();
    }
}
