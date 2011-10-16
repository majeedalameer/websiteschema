/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import websiteschema.dwr.response.ListRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.model.domain.PageInfo;
import websiteschema.model.domain.Scheduler;
import websiteschema.persistence.rdbms.SchedulerMapper;

/**
 *
 * @author ray
 */
@Service
public class SchedulerService {

    @Autowired
    private SchedulerMapper schedulerMapper;

    public ListRange getSchedulers(int start, int count, String orderBy) {
        ListRange listRange = new ListRange();
        PageInfo pageInfo = new PageInfo(start, count);
        listRange.setData(schedulerMapper.getSchedulers(pageInfo).toArray());
        listRange.setTotalSize(schedulerMapper.getTotalResults());
        return listRange;
    }

    public Scheduler getSchedulerById(long id) {
        return schedulerMapper.getById(id);
    }

    @Transactional
    public void insert(Scheduler sche) {
        schedulerMapper.insert(sche);
    }

    @Transactional
    public void update(Scheduler sche) {
        schedulerMapper.update(sche);
    }

    @Transactional
    public void deleteScheduler(Scheduler sche) {
        System.out.println("delete " + sche.getId());
        schedulerMapper.delete(sche);
    }
}
