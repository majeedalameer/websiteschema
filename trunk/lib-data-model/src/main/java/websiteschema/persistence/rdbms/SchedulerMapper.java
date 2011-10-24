/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.Scheduler;

/**
 *
 * @author ray
 */
public interface SchedulerMapper {

    public long getTotalResults();

    public List<Scheduler> getSchedulers(Map params);

    public List<Scheduler> getAll();

    public List<Scheduler> getSchedulersByStartURL(long startURL);

    public Scheduler getById(long id);

    public void update(Scheduler scheduler);

    public void insert(Scheduler scheduler);

    public void delete(Scheduler scheduler);

    public void deleteById(long id);

}
