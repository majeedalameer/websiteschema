/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

/**
 *
 * @author ray
 */
public class UrlLinkUtil {

    private final static UrlLinkUtil ins = new UrlLinkUtil();

    public static UrlLinkUtil getInstance() {
        return ins;
    }

    public URI getURL(String pageUrl, String href) {
        try {
            if (null != href && null != pageUrl) {
                if (href.startsWith("http://") || href.startsWith("ftp://")) {
                    return new URI(href);
                } else if (href.indexOf("://") > 0) {
                    // UnsupportProtocol.
                    return new URI(href);
                }
                URI uri = new URI(pageUrl);
                return uri.resolve(href);
            }
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String convertUriToRowKey(URI uri, String siteId) {
        String ret = null;

        if (null != uri) {
            String schema = uri.getScheme();
            String host = uri.getHost();
            String query = uri.getQuery();
            String path = uri.getPath();
            String date = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm");

            host = (new StringBuilder(host)).reverse().toString();

            if (null != query) {
                ret = siteId + "+" + date + "+" + schema + "://" + host + "/" + path + "?" + query;
            } else {
                ret = siteId + "+" + date + "+" + schema + "://" + host + "/" + path;
            }
        }

        return ret;
    }

    public boolean match(String url, String[] mustHave, String[] dontHave) {
        if (null != url) {
            if (null != mustHave) {
                for (String must : mustHave) {
                    if (!"".equals(must)) {
                        if (!url.contains(must) && !url.matches(must)) {
                            return false;
                        }
                    }
                }
            }

            if (null != dontHave) {
                for (String dont : dontHave) {
                    if (!"".equals(dont)) {
                        if (url.contains(dont) || url.matches(dont)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
//    public static void main(String args[]) throws URISyntaxException {
//        System.out.println(UrlLinkUtil.getInstance().getURL("http://utility.baidu.com/traf/click.php?id=215&url=http://www.baidu.com", "test"));
//        System.out.println(UrlLinkUtil.getInstance().getURL("http://utility.baidu.com/traf/click.php?id=215&url=http://www.baidu.com", ""));
//    }
}