/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase;

import java.util.Map;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.Filter;

/**
 *
 * @author ray
 */
public interface HBaseMapper {

    public String getTableName();

    public void delete(String rowKey);

    public void write(String rowKey, Map<String, String> record);

    public Result select(String rowKey);

    public ResultScanner scan();

    public ResultScanner scan(Filter filter);

    public ResultScanner scan(String rangeStart, String rangeEnd);

    public ResultScanner scan(String rangeStart);

    public ResultScanner scan(String rangeStart, String rangeEnd, Filter filter);
}
