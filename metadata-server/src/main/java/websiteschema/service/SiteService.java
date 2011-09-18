/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import java.util.Date;
import websiteschema.dwr.response.ListRange;
import websiteschema.model.domain.Site;
import websiteschema.persistence.rdbms.SiteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.model.domain.PageInfo;

/**
 *
 * @author ray
 */
@Service
public class SiteService {

    @Autowired
    private SiteMapper siteMapper;

    public ListRange getSites(int start, int count, String orderBy) {
        ListRange listRange = new ListRange();
        PageInfo pageInfo = new PageInfo(start, count);
        listRange.setData(siteMapper.getSites(pageInfo).toArray());
        listRange.setTotalSize(siteMapper.getTotalResults());
        return listRange;
    }

    public Site getSiteById(long id) {
        return siteMapper.getSiteById(id);
    }

    @Transactional
    public void insert(Site site) {
        site.setCreateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        site.setCreateUser(userDetails.getUsername());
        site.setUpdateTime(site.getCreateTime());
        site.setLastUpdateUser(site.getCreateUser());
        siteMapper.insert(site);
    }

    @Transactional
    public void update(Site site) {
        siteMapper.update(site);
    }

    @Transactional
    public void deleteSite(Site site) {
        siteMapper.delete(site);
    }
}
