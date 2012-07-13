/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment;

import websiteschema.mpsegment.dict.domain.DomainDictLoader;
import websiteschema.mpsegment.dict.domain.DomainDictionary;

/**
 *
 * @author ray
 */
public class SimpleDomainDictLoader implements DomainDictLoader {

    @Override
    public void load(DomainDictionary dict) {
        dict.addEntity("PC机", null, "N", 5, 10001, dict);
        dict.addEntity("个人电脑", "PC机", "N", 5, 10001, dict);
    }

    private void addEntity(String word, String headWord, String pos, int freq, int domainType, DomainDictionary dict) {
        dict.pushWord(word, headWord, pos, freq, domainType);
    }
}
