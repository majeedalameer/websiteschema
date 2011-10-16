/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.persistence.rdbms;

import java.util.List;
import websiteschema.model.domain.Job;
import websiteschema.model.domain.PageInfo;

/**
 *
 * @author ray
 */
public interface JobMapper {

    public long getTotalResults();

    public List<Job> getJobs(PageInfo pageInfo);

    public Job getById(long id);

    public void update(Job job);

    public void insert(Job job);

    public void delete(Job job);

    public void deleteById(long id);
}
