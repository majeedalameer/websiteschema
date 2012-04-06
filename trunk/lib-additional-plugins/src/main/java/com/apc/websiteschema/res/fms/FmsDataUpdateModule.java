/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.res.fms;

import com.apc.nlp.update.IUpdateModule;

/**
 *
 * @author ray
 */
public class FmsDataUpdateModule implements IUpdateModule {

    @Override
    public void update() throws Exception {
        FmsData.getInstance().load();
    }
}
