/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.fb;

import com.apc.indextask.idx.Idx;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.element.DocumentUtil;

/**
 *
 * @author ray
 */
public class StockEntityTest {

    @Test
    public void testComOrg() {
        FBStockEntity fb = new FBStockEntity();
        fb.doc = new Doc(DocumentUtil.getDocument("stock_entity.xml"));
        fb.tagName = "COM_ORG";
        List<String> lst = new ArrayList<String>();
        lst.add("TITLE");
        lst.add("THREADS/CONTENT");
        fb.targetTags = lst;

        fb.addTag();

        System.out.println(DocumentUtil.getXMLString(fb.doc.toW3CDocument()));
    }

    @Test
    public void testChl() {
        FBStockEntity fb = new FBStockEntity();
        fb.doc = new Doc(DocumentUtil.getDocument("stock_entity.xml"));
        fb.tagName = "ORG_BBS";
        List<String> lst = new ArrayList<String>();
        lst.add("CHANNEL");
        fb.targetTags = lst;

        fb.addTag();

        System.out.println(DocumentUtil.getXMLString(fb.doc.toW3CDocument()));
    }
}
