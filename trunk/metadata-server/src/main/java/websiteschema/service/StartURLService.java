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
import websiteschema.model.domain.StartURL;
import websiteschema.persistence.rdbms.StartURLMapper;
import static websiteschema.persistence.rdbms.utils.ParameterUtil.*;

/**
 *
 * @author ray
 */
@Service
public class StartURLService {

    @Autowired
    private StartURLMapper startURLMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(startURLMapper.getStartURLs(params).toArray());
        listRange.setTotalSize(startURLMapper.getTotalResults(params));
        return listRange;
    }

    public StartURL getById(long id) {
        return startURLMapper.getById(id);
    }

    @Transactional
    public void insert(StartURL url) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        url.setCreateUser(userDetails.getUsername());
        url.setLastUpdateUser(url.getCreateUser());
        startURLMapper.insert(url);
    }

    @Transactional
    public void update(StartURL url) {
        url.setUpdateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        url.setLastUpdateUser(userDetails.getUsername());
        startURLMapper.update(url);
    }

    @Transactional
    public void deleteRecord(StartURL url) {
        startURLMapper.delete(url);
    }
}
