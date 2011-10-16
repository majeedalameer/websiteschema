/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.persistence.rdbms;

import java.util.List;
import websiteschema.model.domain.PageInfo;
import websiteschema.model.domain.Scheduler;

/**
 *
 * @author ray
 */
public interface SchedulerMapper {

    public long getTotalResults();

    public List<Scheduler> getSchedulers(PageInfo pageInfo);

    public List<Scheduler> getAll();

    public List<Scheduler> getSchedulersByStartURL(long startURL);

    public Scheduler getById(long id);

    public void update(Scheduler scheduler);

    public void insert(Scheduler scheduler);

    public void delete(Scheduler scheduler);

    public void deleteById(long id);

}
