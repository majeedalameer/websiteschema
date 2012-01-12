/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain;

import java.util.Date;

/**
 *
 * @author ray
 */
public class Task implements java.io.Serializable {

    public final static int CREATED = 0;
    public final static int SENT = 1;
    public final static int UNSENT = 2;
    public final static int STARTED = 3;
    public final static int TIMEOUT = 4;
    public final static int FINISHED = 5;
    public final static int EXCEPTION = 6;
    long id = 0;
    long scheduleId = 0;
    int status = CREATED;
    String message = null;
    Date createTime;
    Date updateTime;

    public Task() {
    }

    public Task(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (null != message && message.length() > 1000) {
            message = message.substring(0, 1000);
        }
        this.message = message;
    }
}
