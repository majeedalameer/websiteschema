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
public class Scheduler implements java.io.Serializable {

    public final static int CRON = 0;
    public final static int StartEndTimes = 1;
    public final static int Invalid = -1;
    long id;
    long startURLId;
    long jobId;
    long priority = 0;
    String schedule;
    int scheduleType = CRON;
    Date createTime = new Date();

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public long getStartURLId() {
        return startURLId;
    }

    public void setStartURLId(long startURLId) {
        this.startURLId = startURLId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(int scheduleType) {
        this.scheduleType = scheduleType;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }
}
