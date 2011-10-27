/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import java.util.Map;
import websiteschema.dwr.response.ListRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.model.domain.PageInfo;
import websiteschema.model.domain.Scheduler;
import websiteschema.persistence.rdbms.SchedulerMapper;
import static websiteschema.persistence.rdbms.utils.ParameterUtil.*;

/**
 *
 * @author ray
 */
@Service
public class SchedulerService {

    @Autowired
    private SchedulerMapper schedulerMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(schedulerMapper.getSchedulers(params).toArray());
        listRange.setTotalSize(schedulerMapper.getTotalResults());
        return listRange;
    }

    public Scheduler getById(long id) {
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
    public void deleteRecord(Scheduler sche) {
        System.out.println("delete " + sche.getId());
        schedulerMapper.delete(sche);
    }
}
