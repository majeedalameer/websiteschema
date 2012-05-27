/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.rest;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import websiteschema.model.domain.Schedule;
import websiteschema.model.domain.Task;
import websiteschema.persistence.rdbms.ScheduleMapper;
import websiteschema.persistence.rdbms.StartURLMapper;
import websiteschema.persistence.rdbms.TaskMapper;

/**
 *
 * @author ray
 */
@Controller
@RequestMapping(value = "/report")
public class ReportController {

    Logger l = Logger.getRootLogger();
    @Autowired
    ScheduleMapper scheduleMapper;
    @Autowired
    StartURLMapper startURLMapper;
    @Autowired
    TaskMapper taskMapper;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void start(HttpServletResponse response) throws IOException {
        List scheStatus = taskMapper.getScheduleStatus();
        List sitesAndJobnames = startURLMapper.getSitesAndJobnames();
        List<Record> records = stat(scheStatus, sitesAndJobnames);
        if (null != records && !records.isEmpty()) {
            send(records, response);
        }
    }

    private List<Record> stat(List<Map> scheStatus, List<Map> sitesAndJobnames) {
        Map<String, Record> result = new HashMap<String, Record>();
        for (Map map : sitesAndJobnames) {
            String jobname = (String) map.get("jobname");
            String site = (String) map.get("siteId");
            String name = (String) map.get("name");
            Long scheId = (Long) map.get("id");
            Integer status = (Integer) map.get("status");
            Record r = new Record();
            r.jobname = jobname;
            r.site = site;
            r.name = name;
            r.scheId = null != scheId ? scheId : -1;
            r.scheStatus = null != status ? status : Schedule.STATUS_INVALID;
            String key = scheId != null ? scheId.toString() : jobname;
            result.put(key, r);
        }

        for (Map map : scheStatus) {
            Long scheId = (Long) map.get("scheId");
            Integer taskType = (Integer) map.get("taskType");
            Integer status = (Integer) map.get("status");
            Long count = (Long) map.get("count");
            Record r = result.get(scheId.toString());
            if (null != r) {
                if (taskType == Task.TYPE_LINK) {
                    r.scheTimes += count;
                } else if (status == Task.FINISHED) {
                    r.fetchResults += count;
                }
                if (status != Task.FINISHED) {
                    r.unfinished += count;
                } else {
                    r.finished += count;
                }
            }
        }
        List<Record> ret = new ArrayList<Record>(result.size());
        for (String key : result.keySet()) {
            ret.add(result.get(key));
        }
        return ret;
    }

    private void send(List<Record> records, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition",
                "attachment;filename=websiteschema-report.csv");

        ServletOutputStream out = response.getOutputStream();
        String encoding = "GBK";
        String title = "\"网站\",\"Jobname\",\"栏目名称\",\"是否配置\",\"状态\",\"调度次数\",\"采集数据\",\"执行成功率\",\"建议采集频率\",\"需关注\"";
        out.write((title + "\r\n").getBytes(encoding));
        for (Record r : records) {
            out.write((r.toString() + "\r\n").getBytes(encoding));
        }

        out.flush();
        out.close();
    }

    class Record {

        String site;
        String jobname;
        String name;
        long scheId = -1;
        int scheStatus = Schedule.STATUS_INVALID;
        int scheTimes = 0;
        int fetchResults = 0;
        int finished = 0;
        int unfinished = 0;

        @Override
        public String toString() {
            boolean isConfig = scheId >= 0;
            String ret = "\"" + site + "\",\"" + jobname + "\",\"" + name
                    + "\",\"" + (isConfig ? "已配置" : "未配置")
                    + "\",\"" + (scheStatus == Schedule.STATUS_VALID ? "有效" : "无效")
                    + "\",\"" + scheTimes + "\",\"" + fetchResults + "\"";
            if (scheTimes > 0 && isConfig) {
                double pre = (double) finished / (double) (scheTimes + fetchResults);
                DecimalFormat df = new DecimalFormat();
                String pat = "0.00 %";
                df.applyPattern(pat);
                ret += ",\"" + df.format(pre) + "\"";
                double freq = (double) fetchResults / (double) (scheTimes);
                if (freq < 0.1) {
                    ret += ",\"降低\"";
                } else {
                    ret += ",\"高\"";
                }
            } else {
                ret += ",\"" + 0 + "\"";
                ret += ",\"降低\"";
            }

            if (fetchResults == 0 && isConfig) {
                ret += ",\"是\"";
            } else {
                ret += ",\"否\"";
            }
            return ret;
        }
    }
}
