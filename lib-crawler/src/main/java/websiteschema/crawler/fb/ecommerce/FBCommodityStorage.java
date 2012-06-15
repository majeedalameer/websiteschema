/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb.ecommerce;

import com.ocr.OCR;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;


import websiteschema.fb.annotation.*;
import websiteschema.fb.core.FunctionBlock;

import websiteschema.model.domain.Commodity;
import websiteschema.utils.UrlLinkUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.hadoop.hbase.util.Base64;


import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.w3c.dom.Document;
import websiteschema.crawler.Crawler;
import websiteschema.crawler.SimpleHttpCrawler;
import websiteschema.crawler.fb.FBUnitExtractor;
import websiteschema.element.DocumentUtil;
import websiteschema.model.domain.CommodityRowkey;
import websiteschema.persistence.mapper.CommodityMapper;
import websiteschema.persistence.rdbms.CommodityRowkeyMapper;
import websiteschema.utils.PojoMapper;

/**
 *
 * @author mupeng
 */
@EI(name = {"EI:ADD"})
@EO(name = {"EO"})
@Description(desc = "将URL添加至表Commodity")
public class FBCommodityStorage extends FunctionBlock {

    private Logger l = Logger.getLogger(FBCommodityStorage.class);
    @DI(name = "JOBID", desc = "job id")
    public String jobId;
    @DI(name = "PICXPATH")
    public String picXpath;
    @DI(name = "PICPTS")
    public String picPts;
    @DI(name = "PICREGEX")
    public String picRegex;
    @DI(name = "PROPERTYXPATH", desc = "属性")
    public String propertyXpath;
    @DI(name = "PROPERTYPTS")
    public String propertyPts;
    @DI(name = "DESCXPATH", desc = "描述")
    public String descXpath;
    @DI(name = "DESCPTS")
    public String descPts;
    @DI(name = "DESCMUST")
    public String descMust;
    @DI(name = "PICDESCXPATH", desc = "图文描述")
    public String picDescXpath;
    @DI(name = "CRAWLER")
    public String crawlerType;
    @DI(name = "SITEID", desc = "起始URL的站点ID")
    public String siteId;
    @DI(name = "COMMODITYS", desc = "需要保存的商品列表")
    public List<Commodity> commoditys;
    @DI(name = "CFG", desc = "商品类型配置")
    public String conf;
    @DI(name = "BRAND", desc = "品牌所包含的字段")
    public String brands;
    @DI(name = "MODEL", desc = "型号所包含的字段")
    public String models;
    @DI(name = "BRANDXPATH", desc = "品牌")
    public String brandXpath;
    @DI(name = "BRANDPTS")
    public String brandPts;
    @DI(name = "MODELXPATH", desc = "型号")
    public String modelXpath;
    @DI(name = "MODELPTS")
    public String modelPts;
    @DO(name = "OUT", relativeEvents = {"EO"})
    public String out;
    private CommodityMapper commodityMapper;

    @Algorithm(name = "ADD", desc = "将添加链接保存至表commodity")
    public void addList() {
        out = conf;
        commodityMapper = getContext().getSpringBean("commodityMapper", CommodityMapper.class);
        assert (null != commoditys && null != siteId && null != commodityMapper);
        saveCommoditys(commoditys, siteId);
        triggerEvent("EO");
    }

    public void saveCommoditys(List<Commodity> commoditys, String siteId) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String channel = "";
        String column = "";
        String category = "";
        try {
            List<Map<String, String>> list = PojoMapper.fromJson(conf, List.class);
            for (Map<String, String> map : list) {
                if (map.containsKey("channel")) {
                    channel = map.get("channel");
                    if (null != channel) {
                        channel = channel.replaceAll("[(]\\d+[)]|（\\d+）", "");
                    } else {
                        channel = "";
                    }
                }
                if (map.containsKey("column")) {
                    column = map.get("column");
                    if (null != column) {
                        column = column.replaceAll("[(]\\d+[)]|（\\d+）", "");
                    } else {
                        column = "";
                    }
                }
                if (map.containsKey("category")) {
                    category = map.get("category");
                    if (null != category) {
                        category = category.replaceAll("[(]\\d+[)]|（\\d+）", "");
                    } else {
                        category = "";
                    }
                }
            }
        } catch (Exception e) {
            l.error("channel column category parser error", e);
        }

        List<CommodityRowkey> listCommRowkey = new ArrayList<CommodityRowkey>();
        List<Commodity> listComm = new ArrayList<Commodity>();
        for (Commodity comm : commoditys) {
            if (null == comm) {
                continue;
            }

            try {
                String url = comm.getUrl();
                URL link = getURL(url);
                if (link == null) {
                    continue;
                }

                String price = null;
                if (comm.getPriceurl() == null) {
                    price = comm.getPrice();
                    if (price == null || price.length() == 0) {
                        continue;
                    } else {
                        price = marchPrice(comm.getPrice());
                        if (price.length() == 0) {
                            continue;
                        }
                    }
                } else {
                    price = getPriceOCR(comm.getPriceurl());
                    if (price == null) {
                        continue;
                    }
                }
                String rowKey = UrlLinkUtil.getInstance().convertCommodityToRowKey(link, siteId);

                Commodity newComm = new Commodity();
                newComm.setRowKey(rowKey);
                newComm.setDomain(link.getHost());
                newComm.setJobId(new Integer(jobId));
                newComm.setDate(sdf.format(new Date()));
                newComm.setTitle(comm.getTitle());
                newComm.setUrl(comm.getUrl());
                newComm.setCreateTime(comm.getCreateTime());
                newComm.setParenturl("");
                newComm.setPicurl(comm.getPicurl());
                newComm.setPicData(getPicSrcData(comm.getPicurl()));
//                newComm.setPriceurl(comm.getPriceurl());
                newComm.setSiteId(siteId);
                newComm.setCategory(category);
                newComm.setColumn(column);
                newComm.setChannel(channel);
//                newComm.setEvaluate(matchNumber(comm.getEvaluate()));
//                newComm.setReputation(matchNumber(comm.getReputation()));
                Document doc = getDoc(comm.getUrl());

                Map descMap = getDescMap(url, doc);
                List propertyMap = getPropertyMap(url, doc);
                if (descMap != null) {
                    newComm.setDescription(PojoMapper.toJson(descMap));
                }
                if (propertyMap != null) {
                    newComm.setProperty(PojoMapper.toJson(propertyMap));
                }

                if (descMap != null || propertyMap != null) {
                    newComm.setBrand(getBrand(descMap, propertyMap, brands));
                    newComm.setModel(getBrand(descMap, propertyMap, models));
                }
                if (newComm.getBrand() == null || newComm.getBrand().isEmpty()) {
                    newComm.setBrand(getBrandFromXpath(url, doc));
                }
                if (newComm.getModel() == null || newComm.getModel().isEmpty()) {
                    newComm.setModel(getModelFromXpath(url, doc));
                }
                newComm.setPrice(price);
                int picurlmapSize = 0;
                picurlmapSize = getPicJson(url, doc, comm.getPicurl()).length();
                newComm.setPicurlmap(getPicJson(url, doc, comm.getPicurl()));

                boolean isPut = true;
                if (commodityMapper.exists(rowKey)) {
                    Commodity oldComm = commodityMapper.get(rowKey);
                    if (price != null && price.equals(oldComm.getPrice())) {
                        if (oldComm.getPicData() != null && newComm.getPicData() != null
                                && oldComm.getPicData().length() >= newComm.getPicData().length()
                                && oldComm.getPicurlmap() != null
                                && newComm.getPicurlmap() != null
                                && oldComm.getPicurlmap().length() >= newComm.getPicurlmap().length()) {
                            isPut = false;
                        }
                    }
                }

                if (isPut) {
                    listComm.add(newComm);
                    l.debug("#####new commodity :" + newComm);
                } else {
                    l.debug("该商品已经存在！");
                }
                newComm = null;
                doc = null;
            } catch (Exception e) {
                l.error("put commodity ", e);
            }
        }
        if (!listComm.isEmpty()) {
            commodityMapper.put(listComm);
        }
        System.gc();
    }

    /**
     * 得到Document
     *
     * @param link
     * @return
     * @throws Exception
     */
    public Document getDoc(String link) throws Exception {
//        if (crawler == null) {
//            crawler = this.createCrawler();
//        }
        Crawler crawler = this.createCrawler();
        Document doc = crawler.crawl(link)[0];
//        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
//        domFactory.setNamespaceAware(true); // never forget this!
//        DocumentBuilder builder = domFactory.newDocumentBuilder();
//        doc = builder.parse(DocumentUtil.getXMLString(doc));

        return doc;
    }

    /**
     * 得到型号
     *
     * @param url
     * @param doc
     * @return
     * @throws Exception
     */
    public String getModelFromXpath(String url, Document doc) throws Exception {
        String model = null;
        if (modelXpath != null) {
            List<Map<String, String>> conf = PojoMapper.fromJson(modelPts, List.class);
            FBUnitExtractor ue = new FBUnitExtractor();
            ue.doc = doc;
            if (ue.doc == null) {
                return null;
            }
            ue.url = url;
            ue.points = conf;
            ue.unitXPath = modelXpath;
            ue.extractUnits();
            List<Map<String, String>> list = ue.table;

            if (list.isEmpty()) {
                model = "";
            } else {
                model = list.get(0).get("model");
            }
        }

        return model;
    }

    /**
     * 得到品牌
     *
     * @param url
     * @param doc
     * @return
     * @throws Exception
     */
    public String getBrandFromXpath(String url, Document doc) throws Exception {
        String brand = null;
        if (null != brandXpath) {
            List<Map<String, String>> conf = PojoMapper.fromJson(brandPts, List.class);
            FBUnitExtractor ue = new FBUnitExtractor();
            ue.doc = doc;
            if (ue.doc == null) {
                return null;
            }
            ue.url = url;
            ue.points = conf;
            ue.unitXPath = brandXpath;
            ue.extractUnits();
            List<Map<String, String>> list = ue.table;


            if (list.isEmpty()) {
                brand = "";
            } else {
                brand = list.get(0).get("brand");
            }
        }
        return brand;
    }

    /**
     * 得到产品介绍的map json
     *
     * @param url
     * @return
     * @throws Exception
     */
    public Map getDescMap(String url, Document doc) throws Exception {
        StringBuffer sb = new StringBuffer();
        List<Map<String, String>> conf = PojoMapper.fromJson(descPts, List.class);
        FBUnitExtractor ue = new FBUnitExtractor();
        ue.doc = doc;
        if (ue.doc == null) {
            return null;
        }
        ue.url = url;
        ue.points = conf;
        ue.unitXPath = descXpath;
        ue.extractUnits();
        List<Map<String, String>> list = ue.table;
        if (list == null || list.size() == 0) {
            return null;
        }

        for (Map<String, String> map : list) {
            sb.append(map.get("txt"));
        }
        String r = sb.toString();
        return marchDesc(r, descMust);
    }

    public String getBrand(Map descMap, List<Map<String, String>> propertyMap, String brands) throws Exception {
        String value = "";
        String[] brand = brands.split(",");
        for (int i = 0; i < brand.length; i++) {
            if (propertyMap != null) {
                for (Map<String, String> map : propertyMap) {
                    if (map.containsKey(brand[i])) {
                        value = (String) map.get(brand[i]);
                        break;
                    }
                }
            } else if (descMap != null && descMap.containsKey(brand[i])) {
                value = (String) descMap.get(brand[i]);
                break;
            }
        }
        return value;
    }

    /**
     * 得到商品属性
     *
     * @param url
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> getPropertyMap(String url, Document doc) throws Exception {

        ArrayList<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        List<Map<String, String>> conf = PojoMapper.fromJson(propertyPts, List.class);
        FBUnitExtractor ue = new FBUnitExtractor();
        ue.doc = doc;
        if (ue.doc == null) {
            return null;
        }
        ue.url = url;
        ue.points = conf;
        ue.unitXPath = propertyXpath;
        ue.extractUnits();
        List<Map<String, String>> list = ue.table;
        if (list == null || list.size() == 0) {
            return null;
        }

        for (Map<String, String> map : list) {
            if (map.size() == 2) {
                Map<String, String> ret = new HashMap<String, String>();
                String title = map.get("title").replaceAll("\n|\r|\t|:|：| ", "");
                String value = map.get("value").replaceAll("\n|\r|\t|:|：| ", "");
                ret.put(title, value);
                listMap.add(ret);
            }
        }
        return listMap;
    }

    /**
     * 得到大小的图片的url
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String getPicJson(String url, Document doc, String baseUrl) throws Exception {
        Map<String, String> ret = new HashMap<String, String>();
        String[] regex = this.picRegex.split(";");
        ArrayList<String[]> regexList = new ArrayList<String[]>();
        for (int i = 0; i < regex.length; i++) {
            String[] temp = regex[i].split(":");
            regexList.add(temp);
        }
        List<Map<String, String>> conf = PojoMapper.fromJson(picPts, List.class);
        FBUnitExtractor ue = new FBUnitExtractor();
        ue.doc = doc;
        if (ue.doc == null) {
            return "";
        }
        ue.url = url;
        ue.points = conf;
        ue.unitXPath = picXpath;
        ue.extractUnits();
        List<Map<String, String>> list = ue.table;
//        if (list == null || list.size() == 0) {
//            return "";
//        }

        for (Map<String, String> map : list) {
            String sUrl = map.get("picsrc");
            for (String[] r : regexList) {
                if (sUrl.contains(r[0])) {
                    String bUrl = sUrl.replaceAll(r[0], r[1]);
                    ret.put(getPicSrcData(sUrl), getPicSrcData(bUrl));
                }
            }
        }
        if (ret.isEmpty()) {
            String sUrl = baseUrl;
            for (String[] r : regexList) {
                if (sUrl.contains(r[0])) {
                    String bUrl = sUrl.replaceAll(r[0], r[1]);
                    ret.put(getPicSrcData(sUrl), getPicSrcData(bUrl));
                }
            }
        }
        String r = PojoMapper.toJson(ret);
//        l.debug(r);
        return r;
    }

    /**
     * 得到图片的二进制数据
     *
     * @param link
     * @return
     */
    public String getPicSrcData(String link) throws NullPointerException {
        byte[] buf = null;

        try {
            URL url = getURL(link);
            if (url != null) {
                BufferedImage srcImage = ImageIO.read(url.openStream());
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                ImageIO.write(srcImage, link.substring(link.lastIndexOf(".") + 1, link.length()), bao);
                buf = bao.toByteArray();
                bao.close();
            }

        } catch (MalformedURLException e) {
            l.error("pic data", e);
        } catch (IOException e) {
            l.error("pic data", e);
        }
        if (buf == null) {
            throw new NullPointerException("picdata null");
        } else {
            return Base64.encodeBytes(buf);
        }
    }

    /**
     * 得到价格图片的字符串
     *
     * @param link
     * @return
     */
    public String getPriceOCR(String link) throws Exception {
        String s = "";
        URL url = getURL(link);
        if (url == null) {
            throw new NullPointerException("price ocr null");
        } else {
            try {
                s = OCR.getInstance().recognize(url);
            } catch (Exception e) {
                l.error("no price", e);
            }
        }

        if (s == null) {
            throw new NullPointerException("price ocr null");
        } else {
            return marchPrice(s);

        }

    }

    /**
     * 解析产品介绍的map json
     *
     * @param property
     * @param regex
     * @return
     * @throws Exception
     */
    public Map marchDesc(String property, String regex) throws Exception {
        HashMap descMap = new HashMap();
        String[] reg = regex.split(",");

        StringBuffer regBuf = new StringBuffer();
        for (int i = 0; i < reg.length - 1; i++) {

            regBuf.append(reg[i]);
            regBuf.append("|");
        }
        StringBuffer proBuffer = new StringBuffer();
        String[] pro = property.split(regBuf.toString());
        for (int i = 0; i < pro.length; i++) {
            proBuffer.append(pro[i]);
        }
//        l.debug(proBuffer.toString().trim().substring(1));

        String[] proper = proBuffer.toString().trim().substring(1).split(reg[reg.length - 1]);
        if (reg.length == 2) {
            descMap.put(reg[0], proper[0]);
        } else {
            for (int i = 0; i < reg.length - 2; i++) {
                descMap.put(reg[i], proper[i]);
//            l.debug(proper[i]);
            }
        }
//        l.debug(descMap);

        return descMap;

    }

    public String marchPrice(String price) throws Exception {
//        Pattern pattern = Pattern.compile("\\d+\\.?\\d*");//[0-9]*\\.[0-9]*
        String ret = "";
//
//        Matcher matcher = pattern.matcher(price);
//
//        if (matcher.find()) {
//            ret = matcher.group();
//        }
        ret = price.replaceAll(" |￥", "");

        return ret;
    }

    /**
     *
     * @param src
     * @return
     */
    public String matchNumber(String src) throws Exception {
        if (src == null) {
            return "";
        }
        Pattern pattern = Pattern.compile("[0-9]+");
        String ret = "";

        Matcher matcher = pattern.matcher(src);

        if (matcher.find()) {
            ret = matcher.group();
        }

        return ret;
    }

    /**
     * 功能：检测当前URL是否可连接或是否有效, 描述：最多连接网络 5 次, 如果 5 次都不成功，视为该地址不可用
     *
     * @param urlStr 指定URL网络地址
     * @return URL
     */
    private URL getURL(String urlStr) {
        int counts = 0;
        if (urlStr == null || urlStr.length() <= 0) {
            return null;
        }
        URL url = null;
        HttpURLConnection con = null;
        while (counts < 3) {
            try {
                url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();
                int state = con.getResponseCode();
//                System.out.println(counts + "= " + state);
                if (state == 200) {
//                    l.debug("URL可用！");
                }
                break;
            } catch (Exception ex) {
                counts++;
                l.error("URL不可用，连接第 " + counts + " 次" + urlStr, ex);
                urlStr = null;
                continue;
            } finally {
                con.disconnect();
            }
        }
        return url;
    }

    private Crawler createCrawler() {
        Crawler crawler = null;
        if (null == crawler) {

            try {
                Class clazz = Class.forName(crawlerType);
                crawler = (Crawler) clazz.newInstance();
            } catch (Exception ex) {
                l.error("create crawler", ex);
            }
        }
        return crawler;
    }
}
