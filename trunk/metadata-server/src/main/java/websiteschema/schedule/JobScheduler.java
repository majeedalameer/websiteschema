/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.schedule;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import websiteschema.persistence.rdbms.SchedulerMapper;
import websiteschema.schedule.job.SimpleJob;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
//import static org.quartz.SimpleScheduleBuilder.*;
//import static org.quartz.CronScheduleBuilder.*;
//import static org.quartz.CalendarIntervalScheduleBuilder.*;
//import static org.quartz.TriggerBuilder.*;
//import static org.quartz.DateBuilder.*;

/**
 *
 * @author ray
 */
public class JobScheduler {

    public final static int Error = 0;
    public final static int Started = 1;
    public final static int Stopped = 2;
    public final static int Standby = 3;
    Scheduler sched = null;
    private SchedulerMapper schedulerMapper = null;
    private final java.util.Random random = new java.util.Random();
    // 创建调度者工厂
    private SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    public JobScheduler() {
    }

    public void load() throws SchedulerException {
        // 用工厂创建一个调度者
        sched = schedulerFactory.getScheduler();

        List<websiteschema.model.domain.Scheduler> all = schedulerMapper.getAll();
        for (websiteschema.model.domain.Scheduler sche : all) {
            JobDetail job = createJob(sche);
            Trigger trigger = createTrigger(sche);
            if (null != job && null != trigger) {
                sched.scheduleJob(job, trigger);
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

    private JobDetail createJob(websiteschema.model.domain.Scheduler sche) {
        JobDetail job = newJob(SimpleJob.class).withIdentity(String.valueOf(sche.getId()), "group1").usingJobData("schedulerId", sche.getId()).usingJobData("jobId", sche.getJobId()).usingJobData("startURLId", sche.getStartURLId()).build();
        return job;
    }

    private Trigger createTrigger(websiteschema.model.domain.Scheduler sche) {
        if (websiteschema.model.domain.Scheduler.CRON == sche.getScheduleType()) {
            try {
                String schedule = getRandomSec() + " " + sche.getSchedule();
                Trigger trigger = newTrigger().withIdentity(String.valueOf(sche.getId()), "group1").withSchedule(cronSchedule(schedule)).forJob(String.valueOf(sche.getId()), "group1").startNow().build();
                return trigger;
            } catch (ParseException ex) {
                Logger.getLogger(JobScheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private int getRandomSec() {
        return random.nextInt(60);
    }

    public void setSchedulerMapper(SchedulerMapper schedulerMapper) {
        this.schedulerMapper = schedulerMapper;
    }
}
