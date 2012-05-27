/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.res;

import cnnlp.resource.domain.DomainFactory;
import com.apc.nlp.update.IUpdateModule;

/**
 *
 * @author ray
 */
public class SegmentDictUpdateModule implements IUpdateModule {

    @Override
    public void update() throws Exception {
        DomainFactory.getInstance().buildDictionary();
    }
}
