/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.schedule.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author ray
 */
public class JobCheckTask implements Job {

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
