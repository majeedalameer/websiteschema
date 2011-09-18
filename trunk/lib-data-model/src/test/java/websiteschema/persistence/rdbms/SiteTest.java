package websiteschema.persistence.rdbms;

import java.util.Date;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.model.domain.Site;

public class SiteTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
    SiteMapper siteMapper = ctx.getBean("siteMapper", SiteMapper.class);

    @Test
    public void insert() {
        Site site = new Site();
        site.setSiteId("test_www_sohu_com_2");
        site.setSiteName("搜狐");
        site.setSiteType("general");
        site.setSiteDomain("www.sohu.com");
        site.setUrl("http://www.sohu.com");

        siteMapper.insert(site);
    }

    @Test
    public void selectAndUpdate() {
        Site site = siteMapper.getSiteBySiteId("test_www_sohu_com_2");

        site.setUrl("http://www.sohu.com/");
        site.setLastUpdateUser("system");
        site.setUpdateTime(new Date());
        siteMapper.update(site);
    }

    @Test
    public void delete() {
        Site site = siteMapper.getSiteBySiteId("test_www_sohu_com_2");
        siteMapper.delete(site);
    }
}
