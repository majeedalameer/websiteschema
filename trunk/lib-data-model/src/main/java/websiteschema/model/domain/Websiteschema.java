/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.model.domain;

/**
 *
 * @author ray
 */
public class Websiteschema implements HBaseBean {
    //SiteId
    String rowKey;
    //ClassId
    String classId;
    //updateTime
    String updateTime = String.valueOf(System.currentTimeMillis());

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String[] getFamilyColumns() {
        return  new String[]{"classid","updatetime"};
    }
}
