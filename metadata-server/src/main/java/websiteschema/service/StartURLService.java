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
import websiteschema.model.domain.StartURL;
import websiteschema.persistence.rdbms.StartURLMapper;

/**
 *
 * @author ray
 */
@Service
public class StartURLService {

    @Autowired
    private StartURLMapper startURLMapper;

    public ListRange getStartURLs(int start, int count, String orderBy) {
        ListRange listRange = new ListRange();
        PageInfo pageInfo = new PageInfo(start, count);
        listRange.setData(startURLMapper.getStartURLs(pageInfo).toArray());
        listRange.setTotalSize(startURLMapper.getTotalResults());
        return listRange;
    }

    public StartURL getStartURLById(long id) {
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
    public void deleteStartURL(StartURL url) {
        startURLMapper.delete(url);
    }
}
