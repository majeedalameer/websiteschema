/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.schedule;

import websiteschema.persistence.rdbms.TaskMapper;
import org.quartz.Job;
import org.quartz.JobDataMap;
import websiteschema.persistence.rdbms.JobMapper;
import websiteschema.persistence.rdbms.StartURLMapper;
import websiteschema.persistence.rdbms.WrapperMapper;
import org.apache.log4j.Logger;
import websiteschema.model.domain.Schedule;
import java.text.ParseException;
import java.util.List;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import websiteschema.persistence.rdbms.ScheduleMapper;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;

/**
 *
 * @author ray
 */
public class JobScheduler {

    public final static int Error = 0;
    public final static int Started = 1;
    public final static int Stopped = 2;
    public final static int Standby = 3;
    private Scheduler sched = null;
    private ScheduleMapper scheduleMapper = null;
    private JobMapper jobMapper = null;
    private WrapperMapper wrapperMapper = null;
    private TaskMapper taskMapper = null;
    private StartURLMapper startURLMapper = null;
    private final java.util.Random random = new java.util.Random();
    private final Logger l = Logger.getLogger(JobScheduler.class);
    private final String group = "group1";
    // 创建调度者工厂
    private SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    public JobScheduler() {
    }

    public void load() throws SchedulerException {
        // 用工厂创建一个调度者
        sched = schedulerFactory.getScheduler();

        List<Schedule> all = scheduleMapper.getAll();
        for (Schedule sche : all) {
            long jobId = sche.getJobId();
            long startURLId = sche.getStartURLId();
            if (jobId > 0 && startURLId > 0) {
                JobDetail job = createJob(sche);
                Trigger trigger = createTrigger(sche);
                if (null != job && null != trigger) {
                    sched.scheduleJob(job, trigger);
                }
            } else {
                l.debug("schedule: " + sche.getId() + " is invalid. "
                        + "which jobId is " + sche.getJobId() + " and startURLId is " + sche.getStartURLId());
            }
        }
    }

    public void startup() throws SchedulerException {
        sched.start();
    }

    public int status() throws SchedulerException {
        if (null != sched) {
            if (sched.isShutdown()) {
                return Stopped;
            } else if (sched.isStarted()) {
                return Started;
            } else if (sched.isInStandbyMode()) {
                return Standby;
            }
        }
        return Error;
    }

    public void shutdown() throws SchedulerException {
        sched.clear();
        sched.shutdown();
    }

    private JobDetail createJob(Schedule sche) {
        JobDataMap jobDataMap = new JobDataMap();
        String jobType = jobMapper.getById(sche.getJobId()).getJobType();
        l.debug("jobId: " + sche.getJobId() + " jobType: " + jobType);
        jobDataMap.put("startURLMapper", startURLMapper);
        jobDataMap.put("jobMapper", jobMapper);
        jobDataMap.put("wrapperMapper", wrapperMapper);
        jobDataMap.put("taskMapper", taskMapper);
        jobDataMap.put("schedulerId", sche.getId());
        jobDataMap.put("jobId", sche.getJobId());
        jobDataMap.put("startURLId", sche.getStartURLId());
        try {
            Class<? extends Job> jobClazz = (Class<? extends Job>) Class.forName(jobType);

            // 创建JobDetail
            JobDetail job =
                    newJob(jobClazz).
                    withIdentity(String.valueOf(sche.getId()), group).
                    usingJobData(jobDataMap).
                    build();
            return job;
        } catch (Exception ex) {
            l.error(ex);
        }
        return null;
    }

    private Trigger createTrigger(Schedule sche) {
        if (Schedule.CRON == sche.getScheduleType()) {
            try {
                String schedule = getRandomSec() + " " + sche.getSchedule();
                Trigger trigger =
                        newTrigger().
                        withIdentity(String.valueOf(sche.getId()), group).
                        withSchedule(cronSchedule(schedule)).
                        forJob(String.valueOf(sche.getId()), group).
                        startNow().
                        build();
                return trigger;
            } catch (ParseException ex) {
                l.error(ex);
            }
        }
        return null;
    }

    private int getRandomSec() {
        return random.nextInt(60);
    }

    public void setScheduleMapper(ScheduleMapper scheduleMapper) {
        this.scheduleMapper = scheduleMapper;
    }

    public void setJobMapper(JobMapper jobMapper) {
        this.jobMapper = jobMapper;
    }

    public void setStartURLMapper(StartURLMapper startURLMapper) {
        this.startURLMapper = startURLMapper;
    }

    public void setWrapperMapper(WrapperMapper wrapperMapper) {
        this.wrapperMapper = wrapperMapper;
    }

    public void setTaskMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }
}
