/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase;

import websiteschema.persistence.hbase.core.HBaseMapperFactory;
import websiteschema.persistence.hbase.core.HBaseMapper;
import org.junit.Test;
import websiteschema.model.domain.Websiteschema;

/**
 *
 * @author ray
 */
public class WebsiteschemaTest {

    HBaseMapper<Websiteschema> mapper =
            HBaseMapperFactory.getInstance().create(Websiteschema.class);
    String rowKey = "test_sohu_com_2";

    @Test
    public void test() {
        put();

        get();

        delete();
        
//        deleteTable();
    }

    public void deleteTable() {
        HBaseMapperFactory.getInstance().deleteTable(mapper);
    }

    public void put() {
        Websiteschema record = new Websiteschema();
        record.setRowKey(rowKey);
        record.setValid(false);
        mapper.put(record);
    }

    public void get() {
        Websiteschema record = mapper.get(rowKey);
        record.setRowKey(rowKey);
        System.out.println("    " + record.getRowKey());
        System.out.println("    " + record.isValid());
        System.out.println("    " + record.getLastUpdateTime());
    }

    public void delete() {
        mapper.delete(rowKey);
    }
}
