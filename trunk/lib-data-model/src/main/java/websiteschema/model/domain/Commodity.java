/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import websiteschema.persistence.hbase.annotation.ColumnFamily;
import websiteschema.persistence.hbase.annotation.RowKey;
import websiteschema.utils.UrlLinkUtil;

/**
 *
 * @author mupeng
 */
public class Commodity implements HBaseBean {
    private static Pattern pat = Pattern.compile("([A-z0-9_]+)\\+([0-9:\\- ]+)\\+(.+)");
    @RowKey(desc = "使用siteID+URL作为RowKey，URL的host部分要倒置")
    private String rowKey;
    @ColumnFamily
    private long createTime = System.currentTimeMillis();
    @ColumnFamily
    private int jobId;
    @ColumnFamily
    private String domain;
    @ColumnFamily
    private String date;
    @ColumnFamily
    private String siteId;
    @ColumnFamily
    private String url;
    @ColumnFamily
    private String title;
    @ColumnFamily
    private String channel;
    @ColumnFamily
    private String column;
    @ColumnFamily
    private String category;
    @ColumnFamily
    private String brand;
    @ColumnFamily
    private String model;
    @ColumnFamily
    private String parenturl;
    @ColumnFamily
    private String picurl;
    @ColumnFamily
    private String picData;
    @ColumnFamily
    private String price;
    @ColumnFamily
    private String priceurl;
    @ColumnFamily
    private String evaluate;
    @ColumnFamily
    private String reputation;
    @ColumnFamily
    private String invalide;
    @ColumnFamily
    private String picurlmap;
    @ColumnFamily
    private String property;
    @ColumnFamily
    private String description;
    @ColumnFamily
    private String comment;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    
    public String getPicData() {
        return picData;
    }

    public void setPicData(String picData) {
        this.picData = picData;
    }
    

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvalide() {
        return invalide;
    }

    public void setInvalide(String invalide) {
        this.invalide = invalide;
    }

    public String getPicurlmap() {
        return picurlmap;
    }

    public void setPicurlmap(String picurlmap) {
        this.picurlmap = picurlmap;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }    
    
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
    

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReputation() {
        return reputation;
    }

    public void setReputation(String reputation) {
        this.reputation = reputation;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }
    
    
    
    public String getPriceurl() {
        return priceurl;
    }

    public void setPriceurl(String priceurl) {
        this.priceurl = priceurl;
    }
    
    

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setParenturl(String parentUrl) {
        this.parenturl = parentUrl;
    }

    public void setPicurl(String picUrl) {
        this.picurl = picUrl;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
    

    public String getCategory() {
        return category;
    }

    public String getParenturl() {
        return parenturl;
    }

    public String getPicurl() {
        return picurl;
    }

    public String getPrice() {
        return price;
    }
    
    
    
    public Commodity(){
    }
    
    public Commodity(String url,String price) throws MalformedURLException{
        URL link = new URL(url);
        rowKey = UrlLinkUtil.getInstance().convertCommodityToRowKey(link, price);
    }
    
    public Commodity(URL url,String price) throws MalformedURLException{
        rowKey = UrlLinkUtil.getInstance().convertCommodityToRowKey(url, price);
    }
    
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    
    @Override
    public String getRowKey() {
        return rowKey;
    }

    @Override
    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }
    
    public String getURLRowKey() {
        Matcher m = pat.matcher(rowKey);
        if (m.matches()) {
            return m.group(3);
        } else {
            return null;
        }
    }

    public Date getCreateDate() {
        return new Date(createTime);
    }
    
    public String toString(){
        return "Commodity:[" 
                + "rowKey="+rowKey
                + "\n######jobId="+jobId
                + "\n######nurl=" +url
                + "\n######title=" + title
                + "\n######channel=" + channel
                + "\n######column=" + column
                + "\n######category="+category
                + "\n######comment=" + comment
                + "\n######description=" + description
                + "\n######brand="+brand
                + "\n######model="+model
                + "\n######picurl="+picurl
                + "\n######picData size =" +picData.length()
                + "\n######picrulmap size =" + picurlmap.length()
                + "\n######price ="+price
                + "\n######priceurl=" + priceurl
                + "\n######property=" + property
                +"]";
    }
}
