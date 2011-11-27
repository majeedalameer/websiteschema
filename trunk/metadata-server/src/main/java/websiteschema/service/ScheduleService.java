/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import java.io.IOException;
import websiteschema.rest.SchedulerController;
import java.util.Map;
import websiteschema.dwr.response.ListRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.model.domain.PageInfo;
import websiteschema.model.domain.Schedule;
import websiteschema.persistence.rdbms.ScheduleMapper;
import static websiteschema.persistence.rdbms.utils.ParameterUtil.*;

/**
 *
 * @author ray
 */
@Service
public class ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;
    private SchedulerController schedulerController;

    ScheduleService() {
        schedulerController = new SchedulerController();
    }

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(scheduleMapper.getSchedules(params).toArray());
        listRange.setTotalSize(scheduleMapper.getTotalResults(params));
        return listRange;
    }

    public Schedule getById(long id) {
        return scheduleMapper.getById(id);
    }

    @Transactional
    public void insert(Schedule sche) {
        scheduleMapper.insert(sche);
    }

    @Transactional
    public void update(Schedule sche) {
        scheduleMapper.update(sche);
    }

    @Transactional
    public void deleteRecord(Schedule sche) {
        System.out.println("delete " + sche.getId());
        scheduleMapper.delete(sche);
    }

    public boolean launchScheduler() throws IOException {
        schedulerController.setScheduleMapper(scheduleMapper);
        return schedulerController.start();
    }

    public boolean shutdownScheduler() throws IOException {
        schedulerController.setScheduleMapper(scheduleMapper);
        return schedulerController.stop();
    }
}
