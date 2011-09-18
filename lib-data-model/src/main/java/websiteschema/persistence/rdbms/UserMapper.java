/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import websiteschema.model.domain.PageInfo;
import websiteschema.model.domain.User;

/**
 *
 * @author ray
 */
public interface UserMapper {

    public long getTotalResults();

    public List<User> getUsers(PageInfo pageInfo);

    public User getUserByUserId(long userId);

    public User getUserById(String id);

    public User getUserByName(String name);

    public void update(User user);

    public void insert(User user);

    public void delete(User user);

    public void deleteById(String id);
}
