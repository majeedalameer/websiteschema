/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websiteschema.common.base.Function;
import websiteschema.common.wrapper.SQLBeanWrapper;
import websiteschema.model.domain.Commodity;
import websiteschema.persistence.Mapper;
import websiteschema.persistence.rdbms.CommodityDBMapper;
import websiteschema.utils.DateUtil;

/**
 *
 * @author ray
 */
@Service
public class CommodityMapper implements Mapper<Commodity> {

    private static Pattern pat = Pattern.compile("([A-z0-9_ .]+)\\+([0-9:\\- ]+).*");
    @Autowired
    CommodityDBMapper commodityDBMapper;

    @Override
    public boolean exists(String rowKey) {
        int count = commodityDBMapper.exists(rowKey);
        return count > 0;
    }

    @Override
    public Commodity get(String rowKey) {
        Map map = commodityDBMapper.getByRowKey(rowKey);
        return SQLBeanWrapper.getBean(map, Commodity.class, false);
    }

    @Override
    public List<Commodity> getList(String start, String end) {
        return getList(start, end, null);
    }

    @Override
    public List<Commodity> getList(String start, String end, String family) {
        return getList(start, end, family, -1);
    }

    @Override
    public List<Commodity> getList(String start, String end, String family, int maxResults) {
        String siteId = getSiteId(start);
        Date startDate = getDate(start);
        Date endDate = getDate(end);
        boolean getAll = null == family;

        Map req = new HashMap();
        req.put("siteId", siteId);
        req.put("s_date", startDate);
        req.put("e_date", endDate);

        List res = null;
        if (getAll) {
            res = commodityDBMapper.getResults(req);
        } 

        if (null != res && !res.isEmpty()) {
            List<Commodity> ret = new ArrayList<Commodity>();
            for (Object obj : res) {
                ret.add(SQLBeanWrapper.getBean((Map) obj, Commodity.class, false));
            }
            return ret;
        }
        return null;
    }

    private String getSiteId(String rowKey) {
        Matcher m = pat.matcher(rowKey);
        if (m.matches()) {
            return m.group(1);
        } else {
            return rowKey;
        }
    }

    private Date getDate(String rowKey) {
        if (null != rowKey) {
            Matcher m = pat.matcher(rowKey);
            if (m.matches()) {
                String date = m.group(2);
                Date d = null;
                if (date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
                    d = DateUtil.parseDate(date, "yyyy-MM-dd");
                } else if (date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}")) {
                    d = DateUtil.parseDate(date, "yyyy-MM-dd HH:mm");
                }
                return d;
            }
        }
        return null;
    }

    @Override
    public void put(Commodity obj) {
        if (null != obj) {
            if (exists(obj.getRowKey())) {
                commodityDBMapper.update(SQLBeanWrapper.getMap(obj, Commodity.class));
            } else {
                commodityDBMapper.insert(SQLBeanWrapper.getMap(obj, Commodity.class));
            }
        }
    }

    @Override
    public void put(List<Commodity> lst) {
        if (null != lst) {
            for (Commodity obj : lst) {
                put(obj);
            }
        }
    }

    @Override
    public void delete(String rowKey) {
        commodityDBMapper.deleteByRowKey(rowKey);
    }

    @Override
    public void scan(String start, String end, Function<Commodity> func) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void batchScan(String start, String end, int batchSize, Function<List<Commodity>> func) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
