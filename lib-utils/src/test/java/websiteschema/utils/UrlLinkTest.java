/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
        URI uri = UrlLinkUtil.getInstance().getURI(url, href, "GBK", charsetMap, "GBK");
        System.out.println(uri.toString());
        assert (uri.toString().equals("http://www.chenmingpaper.com/FLMEN.ASP?MENULB=049%D0%C2%CE%C5%D7%CA%D1%B6&MENUJB=2"));
    }

    @Test
    public void test2() {
        String url = "http://mp3.sogou.com/tag.so?query=%u4F24%u611F&w=02200000";
        try {
            URL uri = UrlLinkUtil.getInstance().getURL("http://mp3.sogou.com/", url);
        } catch (Exception ex) {
            ex.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void test3() {
        String str = "http://www.chenmingpaper.com/xxlr.asp?tab=&menuid=241&menujb=3";
        try {
            URI uri = UrlLinkUtil.getInstance().getURI("http://www.chenmingpaper.com/", str);
            URL url = UrlLinkUtil.getInstance().getURL("http://www.chenmingpaper.com/", str);
            String rk1 = UrlLinkUtil.getInstance().convertUriToRowKey(uri, "siteId");
            String rk2 = UrlLinkUtil.getInstance().convertUrlToRowKey(url, "siteId");
            System.out.println(rk1);
            System.out.println(rk2);
            assert (rk1.equals(rk2));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
