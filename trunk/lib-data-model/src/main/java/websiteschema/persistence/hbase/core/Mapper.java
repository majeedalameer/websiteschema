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
    HTable table = null;

    public Mapper(String tableName) {
        this.tableName = tableName;
        conf = HBaseConf.getHBaseConfiguration();
        try {
            table = new HTable(conf, tableName);
        } catch (Exception ex) {
            ex.printStackTrace();
            table = null;
        }
    }

    protected HTable getTable() {
        if (null != table) {
            try {
                table = new HTable(conf, tableName);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return table;
    }

    public String getTableName() {
        return tableName;
    }

    public void delete(String rowKey) {
        try {
            List list = new ArrayList();
            Delete d1 = new Delete(rowKey.getBytes());
            list.add(d1);
            getTable().delete(list);
            l.debug("删除表 " + getTableName() + ", 行 " + rowKey + " 成功！");
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
        }
    }

    public void write(String rowKey, Map<String, String> record) {
        try {
            Put put = new Put(Bytes.toBytes(rowKey));
            for (String key : record.keySet()) {
                if (!"rowKey".equalsIgnoreCase(key)) {
                    if (key.indexOf(':') > 0) {
                        String cf[] = key.split(":");
                        put.add(Bytes.toBytes(cf[0]),
                                Bytes.toBytes(cf[1]),
                                Bytes.toBytes(record.get(key)));
                        getTable().put(put);
                    } else {
                        put.add(Bytes.toBytes(key),
                                Bytes.toBytes(String.valueOf(1)),
                                Bytes.toBytes(record.get(key)));
                        getTable().put(put);
                    }
                }
            }
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
        }
    }

    public void write(List<Row> rows) {
        try {
            getTable().batch(rows);
        } catch (InterruptedException ex) {
            l.error("Table " + getTableName(), ex);
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
        }
    }

    public Result select(String rowKey) {
        try {
            Get g = new Get(rowKey.getBytes());
            return getTable().get(g);
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
            return null;
        }
    }

    public Result select(String rowKey, String family) {
        try {
            Get g = new Get(rowKey.getBytes());
            g.addFamily(Bytes.toBytes(family));
            return getTable().get(g);
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
            return null;
        }
    }

    public boolean exists(String rowKey) {
        try {
            Get g = new Get(rowKey.getBytes());
            return getTable().exists(g);
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
            return false;
        }
    }

    public ResultScanner scan() {
        try {
            Scan s = new Scan();
            return getTable().getScanner(s);
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
            return null;
        }
    }

    public ResultScanner scan(Filter filter) {
        try {
            Scan s = new Scan();
            s.setFilter(filter);
            return getTable().getScanner(s);
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
            return null;
        }
    }

    public ResultScanner scan(String rangeStart, String rangeEnd) {
        try {
            Scan s = new Scan(Bytes.toBytes(rangeStart), Bytes.toBytes(rangeEnd));
            return getTable().getScanner(s);
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
            return null;
        }
    }

    public ResultScanner scan(String rangeStart, String rangeEnd, String family) {
        try {
            Scan s = new Scan(Bytes.toBytes(rangeStart), Bytes.toBytes(rangeEnd));
            s.addFamily(Bytes.toBytes(family));
            return getTable().getScanner(s);
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
            return null;
        }
    }

    public ResultScanner scan(String rangeStart) {
        try {
            Scan s = new Scan(Bytes.toBytes(rangeStart));
            return getTable().getScanner(s);
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
            return null;
        }
    }

    public ResultScanner scan(String rangeStart, String rangeEnd, Filter filter) {
        try {
            Scan s = new Scan(Bytes.toBytes(rangeStart), Bytes.toBytes(rangeEnd));
            s.setCaching(0);
            s.setFilter(filter);
            return getTable().getScanner(s);
        } catch (IOException ex) {
            l.error("Table " + getTableName(), ex);
            return null;
        }
    }
}
