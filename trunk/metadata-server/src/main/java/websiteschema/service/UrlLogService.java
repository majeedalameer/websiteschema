/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import websiteschema.dwr.response.ListRange;
import websiteschema.model.domain.UrlLink;
import websiteschema.model.domain.UrlLog;
import websiteschema.persistence.hbase.UrlLinkMapper;
import websiteschema.persistence.hbase.UrlLogMapper;
import websiteschema.utils.DateUtil;

/**
 *
 * @author ray
 */
@Service
public class UrlLogService {

    private UrlLogMapper urlLogMapper = new UrlLogMapper();
    private UrlLinkMapper urlLinkMapper = new UrlLinkMapper();

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        String jobname = (String) map.get("jobname");
        String startTime = (String) map.get("startTime");
        String startRow = jobname + "+" + startTime;
        String endRow = jobname + "+" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm");
        List<UrlLog> res = urlLogMapper.getList(startRow, endRow, null, 20);
        listRange.setData(res.toArray());
        listRange.setTotalSize(Long.valueOf(res.size()));
        return listRange;
    }

    public UrlLink getUrlLink(UrlLog log) {
        return urlLinkMapper.get(log.getURLRowKey());
    }
}
