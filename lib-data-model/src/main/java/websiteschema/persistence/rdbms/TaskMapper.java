/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.Task;

/**
 *
 * @author ray
 */
public interface TaskMapper {

    public long getTotalResults(Map params);

    public List<Task> getTasks(Map params);

    public Task getById(long id);

    public void update(Task task);

    public void insert(Task task);

    public void delete(Task task);

    public void archive(int before);
}
