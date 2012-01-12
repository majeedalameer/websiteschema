/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.rest;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import websiteschema.persistence.rdbms.JobMapper;
import websiteschema.persistence.rdbms.ScheduleMapper;
import websiteschema.persistence.rdbms.StartURLMapper;
import websiteschema.persistence.rdbms.TaskMapper;
import websiteschema.persistence.rdbms.WrapperMapper;
import websiteschema.schedule.JobScheduler;

/**
 *
 * @author ray
 */
@Controller
@RequestMapping(value = "/scheduler")
public class SchedulerController {

    Logger l = Logger.getRootLogger();
    private static final JobScheduler scheduler = new JobScheduler();
    @Autowired
    ScheduleMapper scheduleMapper;
    @Autowired
    JobMapper jobMapper;
    @Autowired
    WrapperMapper wrapperMapper;
    @Autowired
    StartURLMapper startURLMapper;
    @Autowired
    TaskMapper taskMapper;

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

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public void start(HttpServletResponse response) throws IOException {
        boolean ok = start();
        if (ok) {
            response.getWriter().print("{success:true}");
        } else {
            response.getWriter().print("{success:false}");
        }
    }

    public boolean start() throws IOException {
        try {
            scheduler.setScheduleMapper(scheduleMapper);
            scheduler.setJobMapper(jobMapper);
            scheduler.setStartURLMapper(startURLMapper);
            scheduler.setWrapperMapper(wrapperMapper);
            scheduler.setTaskMapper(taskMapper);
            int status = scheduler.status();
            if (JobScheduler.Stopped == status
                    || JobScheduler.Error == status) {
                l.info("load");
                scheduler.load();
                l.info("start");
                scheduler.startup();
            } else if (JobScheduler.Started == status) {
                // do nothing
                l.info("already started");
            } else if (JobScheduler.Standby == status) {
                l.info("start");
                scheduler.load();
                scheduler.startup();
            }
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    public void stop(HttpServletResponse response) throws IOException {
        l.info("stop");
        boolean ok = stop();
        if (ok) {
            response.getWriter().print("{success:true}");
        } else {
            response.getWriter().print("{success:false}");
        }
    }

    public boolean stop() throws IOException {
        try {
            scheduler.shutdown();
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public void status(HttpServletRequest request, HttpServletResponse response) throws IOException {
        l.debug("status");
        try {
            int status = status();
            response.getWriter().print("{success:true,status:" + status + "}");
        } catch (SchedulerException e) {
            response.getWriter().print("{success:false}");
        }
    }

    public int status() throws SchedulerException {
        return scheduler.status();
    }
}
