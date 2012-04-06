/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.res;

import com.apc.nlp.kb.IDelegateFactory;
import com.apc.nlp.kb.ISecurityDelegate;
import com.apc.nlp.kb.ISecurityDetails;
import com.apc.nlp.kb.IStockCompany;
import com.apc.nlp.kb.IStockDelegate;
import com.apc.nlp.kb.api.impl.DelegateFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author ray
 */
public class CnStockEntities {

    private final static CnStockEntities ins = new CnStockEntities();

    public static CnStockEntities getInstance() {
        return ins;
    }
    private final AtomicReference<StockInfo> stockOrg = new AtomicReference<StockInfo>();

    class StockInfo {

        Map<String, String> stocks = null;
        Map<String, List<String>> stockSentiments = null;
    }

    CnStockEntities() {
        load();
    }

    public boolean contains(String org) {
        return stockOrg.get().stocks.containsKey(org);
    }

    public String getStockId(String org) {
        return stockOrg.get().stocks.get(org);
    }

    public List<String> getStockSentimentWords(String sec) {
        return stockOrg.get().stockSentiments.get(sec);
    }

    public final void load() {
        StockInfo mapStockOrg = loadEntity();
        stockOrg.set(mapStockOrg);
    }

    private StockInfo loadEntity() {
        System.out.println("CnStockEntityUtil is loading org entities.");
        StockInfo sInfo = new StockInfo();
        Map<String, String> stocks = new HashMap<String, String>();
        Map<String, List<String>> sentiments = new HashMap<String, List<String>>();
        IDelegateFactory factory = DelegateFactory.apply();
        IStockDelegate stockDelegate = factory.getStockDelegate();

        Iterator<IStockCompany> companies = stockDelegate.getCompanyIterator();

        while (companies.hasNext()) {
            IStockCompany com = companies.next();
            String name = com.getName();
            String shortName = com.getShortName();
            String[] shortNames = com.getShortNames();
            String oid = com.getOId();
            ISecurityDelegate securityDelegate = factory.getSecurityDelegate();
            ISecurityDetails sec[] = securityDelegate.getSecuritiesByOID(oid);

            String sid = null != sec && sec.length > 0 ? sec[0].getSecurityCode() : null;
            if (null != sid) {
                stocks.put(name.trim(), sid);
                stocks.put(shortName.trim(), sid);
                for (String sname : shortNames) {
                    stocks.put(sname.trim(), sid);
                }
                String[] sens = com.getSentimentWords();
                if (null != sens && sens.length > 0) {
                    sentiments.put(sid, Arrays.asList(sens));
                }
            }
        }

        sInfo.stocks = stocks;
        sInfo.stockSentiments = sentiments;

        return sInfo;
    }
}
