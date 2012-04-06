/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device;

import websiteschema.fb.core.app.Application;

/**
 *
 * @author ray
 */
public class AppExecutor {

    public static void main(String args[]) throws Exception {
        Application app = new Application();
        app.getContext().loadConfigure("fb/weibo_db_extractor.app");
        app.call();
    }
}
