/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apc.websiteschema.fb;

import org.junit.Test;

/**
 *
 * @author ray
 */
public class DreAddTest {

    @Test
    public void checkUnsentData() {
        DreAddDataFB fb = new DreAddDataFB();
        fb.tempDir = "/home/ray/workspace/website_schema/trunk/lib-crawler/temp/unsent";
        fb.checkUnsentData();
    }
}
