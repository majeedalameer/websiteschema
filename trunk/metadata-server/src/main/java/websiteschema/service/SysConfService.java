/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import websiteschema.model.domain.SysConf;
import websiteschema.persistence.rdbms.SysConfMapper;
import java.util.Map;
import java.util.Date;
import websiteschema.dwr.response.ListRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static websiteschema.persistence.rdbms.utils.ParameterUtil.*;

/**
 *
 * @author ray
 */
@Service
public class SysConfService {

    @Autowired
    private SysConfMapper sysConfMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(sysConfMapper.getRows(params).toArray());
        listRange.setTotalSize(sysConfMapper.getTotalResults(params));
        return listRange;
    }

    public SysConf getById(long id) {
        return sysConfMapper.getById(id);
    }

    @Transactional
    public void insert(SysConf cate) {
        cate.setCreateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cate.setCreateUser(userDetails.getUsername());
        cate.setCreateTime(new Date());
        sysConfMapper.insert(cate);
    }

    @Transactional
    public void update(SysConf conf) {
        conf.setUpdateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        conf.setLastUpdateUser(userDetails.getUsername());
        conf.setUpdateTime(new Date());
        sysConfMapper.update(conf);
    }

    @Transactional
    public void deleteRecord(SysConf conf) {
        sysConfMapper.delete(conf);
    }

    @Transactional
    public void deleteById(long id) {
        sysConfMapper.deleteById(id);
    }
}
