/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.schedule.job;

import javax.servlet.http.*;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import websiteschema.common.amqp.Message;
import websiteschema.common.amqp.RabbitQueue;
import websiteschema.metadata.utils.MetadataServerContext;
import websiteschema.model.domain.StartURL;
import websiteschema.persistence.rdbms.*;
import websiteschema.schedule.TaskHandler;

/**
 *
 * @author ray
 */
public class JobAMQPQueueV1 implements Job {

    private long jobId;
    private long schedulerId;
    private long startURLId;
    private JobMapper jobMapper;
    private WrapperMapper wrapperMapper;
    private StartURLMapper startURLMapper;
    Logger l = Logger.getLogger(JobAMQPQueueV1.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey key = context.getJobDetail().getKey();
//        jobMapper = (JobMapper) context.getJobDetail().getJobDataMap().get("jobMapper");
//        wrapperMapper = (WrapperMapper) context.getJobDetail().getJobDataMap().get("wrapperMapper");
//        startURLMapper = (StartURLMapper) context.getJobDetail().getJobDataMap().get("startURLMapper");

        l.debug("Instance " + key + " of schedulerId: " + schedulerId + ", and jobId is: " + jobId + ", and startURLId is: " + startURLId);
        websiteschema.model.domain.Job job = jobMapper.getById(jobId);
        l.debug(job.getConfigure());
        RabbitQueue<Message> queue = TaskHandler.getInstance().getQueue();
        queue.offer(create(job));
        l.debug("Message about Job " + jobId + " has been emitted to queue: " + queue.getQueueName());
    }

    private Message create(websiteschema.model.domain.Job job) {
        StartURL startURL = startURLMapper.getById(startURLId);
        return new Message(jobId, startURLId, job.getWrapperId(), startURL.getStartURL(), job.getConfigure());
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public long getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(long schedulerId) {
        this.schedulerId = schedulerId;
    }

    public long getStartURLId() {
        return startURLId;
    }

    public void setStartURLId(long startURLId) {
        this.startURLId = startURLId;
    }

    public JobMapper getJobMapper() {
        return jobMapper;
    }

    public void setJobMapper(JobMapper jobMapper) {
        this.jobMapper = jobMapper;
    }

    public StartURLMapper getStartURLMapper() {
        return startURLMapper;
    }

    public void setStartURLMapper(StartURLMapper startURLMapper) {
        this.startURLMapper = startURLMapper;
    }

    public WrapperMapper getWrapperMapper() {
        return wrapperMapper;
    }

    public void setWrapperMapper(WrapperMapper wrapperMapper) {
        this.wrapperMapper = wrapperMapper;
    }
}
