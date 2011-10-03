/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase.core;

import websiteschema.persistence.hbase.core.Mapper;
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
        ResultScanner rs = scan(start);
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
