/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase;

import org.junit.Test;
import websiteschema.model.domain.Websiteschema;

/**
 *
 * @author ray
 */
public class WebsiteschemaTest {

    WebsiteschemaMapper<Websiteschema> mapper =
            new WebsiteschemaMapper<Websiteschema>("websiteschema", Websiteschema.class);
    String rowKey = "test_sohu_com_2";

    @Test
    public void test() {
        deleteTable();

        mapper.createTableIfNotExists();
        put();

        get();

        delete();
    }

    public void deleteTable() {
        mapper.deleteTable();
    }

    public void put() {
        Websiteschema record = new Websiteschema();
        record.setRowKey(rowKey);
        record.setClassId("1");
        mapper.put(record);
    }

    public void get() {
        Websiteschema record = mapper.get(rowKey);
        record.setRowKey(rowKey);
        System.out.println(record.getRowKey());
        System.out.println(record.getClassId());
        System.out.println(record.getUpdateTime());
    }

    public void delete() {
        mapper.delete(rowKey);
    }
}
