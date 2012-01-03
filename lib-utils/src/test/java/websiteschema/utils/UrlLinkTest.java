/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author ray
 */
public class UrlLinkTest {

    @Test
    public void test() {
        String url = "http://www.chenmingpaper.com/xxlr.asp?tab=&menuid=241&menujb=3";
        String href = "FLMEN.ASP?MENULB=049新闻资讯&MENUJB=2";
        Map<String, String> charsetMap = new HashMap<String, String>();
        URI uri = UrlLinkUtil.getInstance().getURL(url, href, "GBK", charsetMap, "GBK");
        System.out.println(uri.toString());
        assert (uri.toString().equals("http://www.chenmingpaper.com/FLMEN.ASP?MENULB=049%D0%C2%CE%C5%D7%CA%D1%B6&MENUJB=2"));
    }

    @Test
    public void test2() {
        String url = "http://mp3.sogou.com/tag.so?query=%u4F24%u611F&w=02200000";
        try {
            URI uri = new URI(url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
