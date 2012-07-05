/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.res;

import com.apc.nlp.update.IUpdateModule;

/**
 *
 * @author ray
 */
public class CnStockEntitiesUpdateModule implements IUpdateModule {

    @Override
    public void update() throws Exception {
        CnStockEntities.getInstance().load();
    }
}
