/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.metadata.utils;

import org.apache.log4j.Logger;
import websiteschema.conf.Configure;

/**
 *
 * @author ray
 */
public class MetadataServerContext {

    static private MetadataServerContext ins = new MetadataServerContext();

    public static MetadataServerContext getInstance() {
        return ins;
    }
    private Configure conf;
    private Logger l = Logger.getLogger(MetadataServerContext.class);

    MetadataServerContext() {
        try {
            conf = Configure.createConfigure("configure-site.ini");
            if (null == conf) {
                l.error("Can not load configuration file: configure-site.ini");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Configure getConf() {
        return conf;
    }
}
