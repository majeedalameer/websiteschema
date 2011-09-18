/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import websiteschema.model.domain.HBaseBean;

/**
 *
 * @author ray
 */
public class WebsiteschemaMapper<T extends HBaseBean> extends HBaseMapperImpl {

    Class<T> clazz;
    JavaBeanWrapper wrapper = new JavaBeanWrapper();

    public WebsiteschemaMapper(String tableName, Class<T> clazz) {
        super(tableName);
        this.clazz = clazz;
        createTableIfNotExists();
    }

    public final void createTableIfNotExists() {
        try {
            String[] cfs = clazz.newInstance().getFamilyColumns();
            if (!admin.tableExists(getTableName())) {
                HTableDescriptor tableDesc = new HTableDescriptor(getTableName());
                for (int i = 0; i < cfs.length; i++) {
                    tableDesc.addFamily(new HColumnDescriptor(cfs[i]));
                }
                admin.createTable(tableDesc);
                l.debug("表 " + getTableName() + " 创建成功！");
            } else {
                l.debug("表 " + getTableName() + " 已经存在！");
            }
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public T get(String rowKey) {
        Result result = select(rowKey);
        return wrapper.getBean(result, clazz);
    }

    public void scan(Function<T> func) {
        ResultScanner rs = scan();
        for (Result r : rs) {
            T obj = wrapper.getBean(r, clazz);
            func.invoke(obj);
        }
    }

    public void put(T obj) {
        Map<String, String> record = wrapper.getMap(obj, clazz);
        write(obj.getRowKey(), record);
    }
}
