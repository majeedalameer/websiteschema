/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import websiteschema.dwr.response.ListRange;
import websiteschema.model.domain.User;
import websiteschema.persistence.rdbms.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ray
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public ListRange getUsers(int start, int count, String orderBy) {
        ListRange listRange = new ListRange();
        listRange.setData(userMapper.getUsers().toArray());
        listRange.setTotalSize(userMapper.getTotalResults());
        return listRange;
    }

    @Transactional
    public void insert(User user) {
        userMapper.insert(user);
    }

    @Transactional
    public void update(User user) {
        userMapper.update(user);
    }

    @Transactional
    public void deleteUser(User user) {
        userMapper.delete(user);
    }
}
