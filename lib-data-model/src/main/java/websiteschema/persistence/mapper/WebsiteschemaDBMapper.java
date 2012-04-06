/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.persistence.mapper;

import java.util.List;
import java.util.Map;

/**
 *
 * @author ray
 */
public interface WebsiteschemaDBMapper {

    public long getTotalResults();

    public List<Map> getResults(Map params);

    public Map getById(long id);

    public void update(Map job);

    public void insert(Map job);

    public void delete(Map job);

    public void deleteById(long id);

}
