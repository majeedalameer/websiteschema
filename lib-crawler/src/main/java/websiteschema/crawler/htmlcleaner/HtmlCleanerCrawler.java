/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.htmlcleaner;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.log4j.Logger;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.JDomSerializer;
import org.htmlcleaner.TagNode;
import org.jdom.output.DOMOutputter;
import org.w3c.dom.Document;
import websiteschema.crawler.Crawler;
import websiteschema.crawler.WebPage;
import websiteschema.model.domain.cralwer.CrawlerSettings;

/**
 *
 * @author mupeng
 */
public class HtmlCleanerCrawler implements Crawler {

    boolean loadImage = false;
    boolean loadEmbeddedFrame = false;
    boolean allowPopupWindow = false;
    String url = null;
    String encoding = null;
    CrawlerSettings crawlerSettings;
    int sec = 1000;
    int delay = 60 * sec;
    int httpStatus = 0;
    Map<String, String> header = new HashMap<String, String>(2);
    Logger l = Logger.getLogger(HtmlCleanerCrawler.class);

    @Override
    public Document[] crawl(String url_str) {
        WebPage page = crawlWebPage(url_str);
        if (null != page) {
            return page.getDocs();
        }
        return null;
    }

    public WebPage crawlWebPage0(String url_str) {
        WebPage ret = null;
        try {
            org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();


            HttpGet httpget = new HttpGet(url_str);

            // Execute the request
            HttpResponse response = httpclient.execute(httpget);

            // Examine the response status
            System.out.println(response.getStatusLine());


            // Get hold of the response entity
            HttpEntity entity = response.getEntity();

            // If the response does not enclose an entity, there is no need
            // to worry about connection release
            if (entity != null) {
                InputStream instream = entity.getContent();
                try {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                    // do something useful with the response
                    System.out.println(reader.readLine());

                } catch (IOException ex) {

                    // In case of an IOException the connection will be released
                    // back to the connection manager automatically
                    throw ex;

                } catch (RuntimeException ex) {

                    // In case of an unexpected exception you may want to abort
                    // the HTTP request in order to shut down the underlying
                    // connection and release it back to the connection manager.
                    httpget.abort();
                    throw ex;

                } finally {

                    // Closing the input stream will trigger connection release
                    instream.close();

                }

                // When HttpClient instance is no longer needed,
                // shut down the connection manager to ensure
                // immediate deallocation of all system resources
                httpclient.getConnectionManager().shutdown();
            }
        } catch (Exception e) {
        }

        return ret;
    }

    @Override
    public WebPage crawlWebPage(String url_str) {
        WebPage ret = new WebPage(this);

        GetMethod get = new GetMethod(url_str);
        try {
            Thread.sleep(10);
            url = url_str;
            HttpClient hc = new HttpClient();
            Collection<Header> headers = new ArrayList<Header>();
            headers.add(new Header("Accept", "*/*"));
            headers.add(new Header("Accept-Language", "zh-cn"));
            headers.add(new Header("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)"));
            headers.add(new Header("UA-CPU", "x86"));
            headers.add(new Header("Accept-Encoding", "gzip, deflate"));

            hc.getParams().setParameter("http.default-headers", headers);
            hc.getHttpConnectionManager().getParams().setConnectionTimeout(delay);
            hc.getHttpConnectionManager().getParams().setSoTimeout(delay);

            get.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);

            hc.executeMethod(get);

            String html = "";
            Header encode = get.getResponseHeader("Content-Encoding");
            if (encode.getValue().contains("gzip")) {
                html = unZip(get.getResponseBodyAsStream(), get.getResponseCharSet());
            } else {
                html = get.getResponseBodyAsString();
            }


            HtmlCleaner cleaner = new HtmlCleaner();
            CleanerProperties props = cleaner.getProperties();
            TagNode node = cleaner.clean(html);

            JDomSerializer jdomSerializer = new JDomSerializer(props, true);

            DOMOutputter outputter = new DOMOutputter();
            org.w3c.dom.Document document = outputter.output(jdomSerializer.createJDom(node));
            httpStatus = get.getStatusCode();

            ret.setDocs(new Document[]{document});
            ret.setHtmlSource(new String[]{html});
            ret.setUrl(url);
        } catch (Exception ex) {
            l.error(url_str, ex);
            return null;
        } finally {
            if (get != null) {
                get.releaseConnection();
            }
        }

        return ret;
    }

    @Override
    public void stopLoad() {
        l.debug("getLinks: Not supported yet.");
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String[] getLinks() {
        l.debug("getLinks: Not supported yet.");
        return null;
    }

    @Override
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public void setProxy(String server, int port) {
        l.debug("setProxy: Not supported yet.");
    }

    @Override
    public void executeScript(String javascriptBody) {
        l.debug("executeScript: Not supported yet.");
    }

    @Override
    public void setLoadImage(boolean yes) {
        this.loadImage = yes;
    }

    @Override
    public void setLoadEmbeddedFrame(boolean yes) {
        loadEmbeddedFrame = yes;
    }

    @Override
    public void setAllowPopupWindow(boolean yes) {
        l.debug("setAllowPopupWindow: Not supported yet.");
    }

    @Override
    public void setCrawlerSettings(CrawlerSettings setting) {
        this.crawlerSettings = setting;
        if (null == encoding || "".equals(encoding)) {
            encoding = crawlerSettings.getEncoding();
        }
    }

    @Override
    public void setTimeout(int timeout) {
        this.delay = timeout;
    }

    @Override
    public void addHeader(String key, String value) {
        header.put(key, value);
    }

    @Override
    public void setCookie(String cookies) {
        header.put("Cookie", cookies);
    }

    private String unZip(InputStream in, String charSet) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPInputStream gis = null;
        try {
            gis = new GZIPInputStream(in);
            byte[] _byte = new byte[1024];
            int len = 0;
            while ((len = gis.read(_byte)) != -1) {
                baos.write(_byte, 0, len);
            }
            String unzipString = new String(baos.toByteArray(), charSet);
            return unzipString;
        } finally {
            if (null != gis) {
                try {
                    gis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
