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
import websiteschema.utils.MD5;

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

    public User getUserById(String id) {
        return userMapper.getUserById(id);
    }

    @Transactional
    public void insert(User user) {
        String passwd = user.getPasswd();
        if (null == passwd || !"".equals(passwd)) {
            passwd = MD5.getMD5("123456".getBytes());
            user.setPasswd(passwd);
        }
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
