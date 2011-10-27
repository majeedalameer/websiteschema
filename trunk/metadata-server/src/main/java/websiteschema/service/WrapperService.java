/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import java.util.Map;
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
import static websiteschema.persistence.rdbms.utils.ParameterUtil.*;

/**
 *
 * @author ray
 */
@Service
public class WrapperService {

    @Autowired
    private WrapperMapper wrapperMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(wrapperMapper.getWrappers(params).toArray());
        listRange.setTotalSize(wrapperMapper.getTotalResults());
        return listRange;
    }

    public Wrapper getById(long id) {
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
    public void deleteRecord(Wrapper wrapper) {
        wrapperMapper.delete(wrapper);
    }
}
