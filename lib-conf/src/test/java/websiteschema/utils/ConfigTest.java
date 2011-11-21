/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.utils;

import websiteschema.conf.Configure;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ray
 */
public class ConfigTest {

    @Test
    public void test() {
        Configure conf = new Configure("configure-site.ini");
        assertEquals(conf.getProperty("AnalyzerTips"),"abc");
    }

}
