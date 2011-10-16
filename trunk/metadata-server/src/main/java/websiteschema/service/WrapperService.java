/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import java.util.Date;
import websiteschema.dwr.response.ListRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.model.domain.PageInfo;
import websiteschema.model.domain.Wrapper;
import websiteschema.persistence.rdbms.WrapperMapper;

/**
 *
 * @author ray
 */
@Service
public class WrapperService {

    @Autowired
    private WrapperMapper wrapperMapper;

    public ListRange getWrappers(int start, int count, String orderBy) {
        ListRange listRange = new ListRange();
        PageInfo pageInfo = new PageInfo(start, count);
        listRange.setData(wrapperMapper.getWrappers(pageInfo).toArray());
        listRange.setTotalSize(wrapperMapper.getTotalResults());
        return listRange;
    }

    public Wrapper getWrapperById(long id) {
        return wrapperMapper.getById(id);
    }

    @Transactional
    public void insert(Wrapper wrapper) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        wrapper.setCreateUser(userDetails.getUsername());
        wrapper.setLastUpdateUser(wrapper.getCreateUser());
        wrapperMapper.insert(wrapper);
    }

    @Transactional
    public void update(Wrapper wrapper) {
        wrapper.setUpdateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        wrapper.setLastUpdateUser(userDetails.getUsername());
        wrapperMapper.update(wrapper);
    }

    @Transactional
    public void deleteWrapper(Wrapper wrapper) {
        wrapperMapper.delete(wrapper);
    }
}
