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
import websiteschema.model.domain.Job;
import websiteschema.model.domain.PageInfo;
import websiteschema.persistence.rdbms.JobMapper;

/**
 *
 * @author ray
 */
@Service
public class JobService {

    @Autowired
    private JobMapper jobMapper;

    public ListRange getJobs(int start, int count, String orderBy) {
        ListRange listRange = new ListRange();
        PageInfo pageInfo = new PageInfo(start, count);
        listRange.setData(jobMapper.getJobs(pageInfo).toArray());
        listRange.setTotalSize(jobMapper.getTotalResults());
        return listRange;
    }

    public Job getJobById(long id) {
        return jobMapper.getById(id);
    }

    @Transactional
    public void insert(Job job) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        job.setCreateUser(userDetails.getUsername());
        job.setLastUpdateUser(job.getCreateUser());
        jobMapper.insert(job);
    }

    @Transactional
    public void update(Job job) {
        job.setUpdateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        job.setLastUpdateUser(userDetails.getUsername());
        jobMapper.update(job);
    }

    @Transactional
    public void deleteJob(Job job) {
        jobMapper.delete(job);
    }
}
