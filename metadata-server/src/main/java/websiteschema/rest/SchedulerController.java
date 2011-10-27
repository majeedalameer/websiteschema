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
import websiteschema.persistence.rdbms.SchedulerMapper;
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
    SchedulerMapper schedulerMapper;

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public void start(HttpServletResponse response) throws IOException {
        try {
            scheduler.setSchedulerMapper(schedulerMapper);
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
            response.getWriter().print("{success:true}");
        } catch (SchedulerException e) {
            e.printStackTrace();
            response.getWriter().print("{success:false}");
        }
    }

    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    public void stop(HttpServletResponse response) throws IOException {
        l.info("stop");
        try {
            scheduler.shutdown();
            response.getWriter().print("{success:true}");
        } catch (SchedulerException e) {
            response.getWriter().print("{success:false}");
        }
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public void status(HttpServletRequest request, HttpServletResponse response) throws IOException {
        l.debug("status");
        try {
            int status = scheduler.status();
            response.getWriter().print("{success:true,status:" + status + "}");
        } catch (SchedulerException e) {
            response.getWriter().print("{success:false}");
        }
    }
}
