/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

/**
 *
 * @author ray
 */
public class Mapper {

    Logger l = Logger.getRootLogger();
    String tableName;
    Configuration conf;

    public Mapper(String tableName) {
        this.tableName = tableName;
        conf = HBaseConf.getHBaseConfiguration();
    }

    public String getTableName() {
        return tableName;
    }

    public void delete(String rowKey) {
        try {
            HTable table = new HTable(conf, getTableName());
            List list = new ArrayList();
            Delete d1 = new Delete(rowKey.getBytes());
            list.add(d1);
            table.delete(list);
            l.debug("删除表 " + getTableName() + ", 行 " + rowKey + " 成功！");
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
        }
    }

    public void write(String rowKey, Map<String, String> record) {
        try {
            HTable table = new HTable(conf, getTableName());
            Put put = new Put(Bytes.toBytes(rowKey));
            for (String key : record.keySet()) {
                if (!"rowKey".equalsIgnoreCase(key)) {
                    put.add(Bytes.toBytes(key),
                            Bytes.toBytes(String.valueOf(1)),
                            Bytes.toBytes(record.get(key)));
                    table.put(put);
                }
            }
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
        }
    }

    public void write(List<Row> rows) {
        try {
            HTable table = new HTable(conf, getTableName());
            table.batch(rows);
        } catch (InterruptedException ex) {
            l.error("Table " + getTableName(), ex);
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
        }
    }

    public Result select(String rowKey) {
        try {
            HTable table = new HTable(conf, getTableName());
            Get g = new Get(rowKey.getBytes());
            return table.get(g);
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
            return null;
        }
    }

    public ResultScanner scan() {
        try {
            HTable table = new HTable(conf, getTableName());
            Scan s = new Scan();
            return table.getScanner(s);
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
            return null;
        }
    }

    public ResultScanner scan(Filter filter) {
        try {
            HTable table = new HTable(conf, getTableName());
            Scan s = new Scan();
            s.setFilter(filter);
            return table.getScanner(s);
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
            return null;
        }
    }

    public ResultScanner scan(String rangeStart, String rangeEnd) {
        try {
            HTable table = new HTable(conf, getTableName());
            Scan s = new Scan(Bytes.toBytes(rangeStart), Bytes.toBytes(rangeEnd));
            return table.getScanner(s);
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
            return null;
        }
    }

    public ResultScanner scan(String rangeStart) {
        try {
            HTable table = new HTable(conf, getTableName());
            Scan s = new Scan(Bytes.toBytes(rangeStart));
            return table.getScanner(s);
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
            return null;
        }
    }

    public ResultScanner scan(String rangeStart, String rangeEnd, Filter filter) {
        try {
            HTable table = new HTable(conf, getTableName());
            Scan s = new Scan(Bytes.toBytes(rangeStart), Bytes.toBytes(rangeEnd));
            s.setCaching(0);
            s.setFilter(filter);
            return table.getScanner(s);
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
            return null;
        }
    }
}
