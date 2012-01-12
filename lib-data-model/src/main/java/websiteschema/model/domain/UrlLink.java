/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain;

import java.util.Date;
import websiteschema.persistence.hbase.annotation.ColumnFamily;
import websiteschema.persistence.hbase.annotation.RowKey;

/**
 *
 * @author ray
 */
public class UrlLink implements HBaseBean {

    public final static int New = 0;
    @RowKey
    String rowKey;
    @ColumnFamily(family = "c")
    String content = null;
    @ColumnFamily
    int status = New;
    @ColumnFamily
    String url = null;
    @ColumnFamily
    Date lastUpdateTime = new Date();
    @ColumnFamily
    Date createTime = null;

    @Override
    public String getRowKey() {
        return rowKey;
    }

    @Override
    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
