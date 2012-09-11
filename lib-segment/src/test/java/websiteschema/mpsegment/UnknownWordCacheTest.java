/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.mpsegment;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.core.UnknownWordCache;

/**
 * @author ray
 */
public class UnknownWordCacheTest {

    @Test
    public void should_be_Fixed_Size_And_Unknown_Words_is_FIFO() {
        UnknownWordCache cache = new UnknownWordCache(2);
        cache.getNewWordItem("A");
        cache.getNewWordItem("B");
        cache.getNewWordItem("C");
        assert (!cache.contains("A"));
        assert (cache.contains("B"));
        assert (cache.contains("C"));
    }

}
