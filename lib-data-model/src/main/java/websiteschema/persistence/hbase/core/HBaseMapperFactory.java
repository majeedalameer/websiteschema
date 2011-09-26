/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase.core;

import websiteschema.persistence.hbase.core.HBaseMapper;
import websiteschema.persistence.hbase.annotation.ColumnFamily;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import websiteschema.model.domain.HBaseBean;

/**
 *
 * @author ray
 */
public class HBaseMapperFactory {

    private static final HBaseMapperFactory instance = new HBaseMapperFactory();
    private Map<Class, HBaseMapper> map = new HashMap<Class, HBaseMapper>();
    private Logger l = Logger.getLogger(HBaseMapperFactory.class);
    private HBaseAdmin admin;

    public static HBaseMapperFactory getInstance() {
        return instance;
    }

    HBaseMapperFactory() {
        Configuration conf = HBaseConfiguration.create();
        try {
            admin = new HBaseAdmin(conf);
        } catch (MasterNotRunningException ex) {
            l.error("MasterNotRunningException", ex);
        } catch (ZooKeeperConnectionException ex) {
            l.error("ZooKeeperConnectionException", ex);
        }
    }

    public <T extends HBaseBean> HBaseMapper<T> create(Class<T> clazz) {
        if (map.containsKey(clazz)) {
            return map.get(clazz);
        } else {
            HBaseMapper<T> mapper = new HBaseMapper<T>(clazz);
            createTableIfNotExists(mapper, clazz);
            map.put(clazz, mapper);
            return mapper;
        }
    }

    private void createTableIfNotExists(HBaseMapper mapper, Class clazz) {
        try {
            if (!admin.tableExists(mapper.getTableName())) {

                HTableDescriptor tableDesc = new HTableDescriptor(mapper.getTableName());
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ColumnFamily.class)) {
                        tableDesc.addFamily(new HColumnDescriptor(field.getName()));
                    }
                }
                admin.createTable(tableDesc);
                l.debug("表 " + mapper.getTableName() + " 创建成功！");
            } else {
                l.debug("表 " + mapper.getTableName() + " 已经存在！");
                Field[] fields = clazz.getDeclaredFields();
                HTableDescriptor tableDesc = admin.getTableDescriptor(Bytes.toBytes(mapper.getTableName()));
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ColumnFamily.class)) {
                        if (!tableDesc.hasFamily(Bytes.toBytes(field.getName()))) {
                            admin.addColumn(mapper.getTableName(), new HColumnDescriptor(field.getName()));
                            l.debug("表 " + mapper.getTableName() + " 添加了列： " + field.getName());
                        }
                    }
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteTable(HBaseMapper mapper) {
        try {
            admin.disableTable(mapper.getTableName());
            admin.deleteTable(mapper.getTableName());
            l.debug("删除表 " + mapper.getTableName() + " 成功！");
        } catch (IOException ex) {
            l.error("Table " + mapper.getTableName(), ex);
        }
    }
}
