/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.schedule;

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
import websiteschema.schedule.job.JobAMQPQueueV1;
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
        JobDetail job =
                newJob(JobAMQPQueueV1.class).
                withIdentity(String.valueOf(sche.getId()), group).
                usingJobData("schedulerId", sche.getId()).
                usingJobData("jobId", sche.getJobId()).
                usingJobData("startURLId", sche.getStartURLId()).
                build();
        return job;
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

    public void setSchedulerMapper(ScheduleMapper scheduleMapper) {
        this.scheduleMapper = scheduleMapper;
    }
}
