/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.schedule;

import java.util.List;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;
import websiteschema.persistence.rdbms.SchedulerMapper;

/**
 *
 * @author ray
 */
public class TaskScheduler {

    Scheduler scheduler;
    SchedulerMapper schedulerMapper;

    public void init() throws SchedulerException {
        // 创建调度者工厂
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        // 用工厂创建一个调度者
        scheduler = schedulerFactory.getScheduler();
    }

    public void load() throws SchedulerException {
        List<websiteschema.model.domain.Scheduler> all = schedulerMapper.getAll();
        for (websiteschema.model.domain.Scheduler sche : all) {
            JobDetail job = createJob(sche);
            Trigger trigger = createTrigger(sche);
            if (null != job && null != trigger) {
                scheduler.scheduleJob(job, trigger);
            }
        }
    }

    public void startup() throws SchedulerException {
        scheduler.start();
    }

    public void shutdown() throws SchedulerException {
        scheduler.shutdown();
    }

    private JobDetail createJob(websiteschema.model.domain.Scheduler sche) {
        return null;
    }

    private Trigger createTrigger(websiteschema.model.domain.Scheduler sche) {
        return null;
    }
}
