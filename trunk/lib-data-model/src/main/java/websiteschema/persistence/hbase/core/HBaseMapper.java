/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import websiteschema.model.domain.HBaseBean;

/**
 *
 * @author ray
 */
public class HBaseMapper<T extends HBaseBean> extends Mapper {

    private Class<T> clazz;
    private JavaBeanWrapper wrapper = JavaBeanWrapper.getInstance();

    public HBaseMapper(Class<T> clazz) {
        super(clazz.getName().replaceAll(".*\\.", "").toLowerCase());
        this.clazz = clazz;
        HBaseMapperFactory.getInstance().createTableIfNotExists(getTableName(), clazz);
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

    public void scan(String start, Function<T> func) {
        if (null != start && "".equals(start)) {
            ResultScanner rs = scan(start);
            for (Result r : rs) {
                T obj = wrapper.getBean(r, clazz);
                func.invoke(obj);
            }
        }
    }

    public List<T> getList(String start, String end) {
        if (null != start && !"".equals(start)) {
            ResultScanner rs = scan(start, end);
            List<T> ret = new ArrayList<T>();
            for (Result r : rs) {
                T obj = wrapper.getBean(r, clazz);
                ret.add(obj);
            }
            return ret;
        } else {
            return null;
        }
    }

    public void put(T obj) {
        Map<String, String> record = wrapper.getMap(obj, clazz);
        write(obj.getRowKey(), record);
    }
}