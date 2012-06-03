/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.CommodityRowkey;
import websiteschema.model.domain.SysConf;

/**
 *
 * @author mupeng
 */
public interface CommodityRowkeyMapper {
    public long getTotalResults(Map map);

    public List<CommodityRowkey> getByRowkey(String rowkey);

    public void insert(CommodityRowkey comm);

    public void update(CommodityRowkey comm);

    public SysConf getById(long id);

    public void delete(CommodityRowkey comm);

    public void deleteById(long id);
}
