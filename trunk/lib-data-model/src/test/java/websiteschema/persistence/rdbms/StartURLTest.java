/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.persistence.rdbms;

import java.util.List;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.model.domain.PageInfo;
import websiteschema.model.domain.StartURL;

/**
 *
 * @author ray
 */
public class StartURLTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
    StartURLMapper startURLMapper = ctx.getBean("startURLMapper", StartURLMapper.class);
    String jobname = "test_www_sohu_com_2";

    @Test
    public void test() {
        insert();
        selectAndUpdate();
        delete();
    }

    public void insert() {
        StartURL url = new StartURL();
        url.setJobname(jobname);
        url.setSiteId(jobname);
        url.setStartURL("http://test/");
        System.out.println("id(before insert): " + url.getId());
        startURLMapper.insert(url);
        System.out.println("id(after insert): " + url.getId());
    }

    public void selectAndUpdate() {
        PageInfo pageInfo = new PageInfo(1, 10);
        List<StartURL> list = startURLMapper.getStartURLs(pageInfo);
        for(StartURL url : list) {
            System.out.println(url.getStartURL());
        }

        StartURL url = startURLMapper.getByJobname(jobname);

        startURLMapper.update(url);
    }

    public void delete() {
        StartURL url = startURLMapper.getByJobname(jobname);
        startURLMapper.delete(url);
    }

}
