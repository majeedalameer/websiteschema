package websiteschema.persistence.rdbms;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.model.domain.User;

public class UserTest {

    @Test
    public void curd() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");

        UserMapper mapper = ctx.getBean("userMapper", UserMapper.class);
        User user = new User();
        user.setName("1");
        user.setPasswd("1");
        user.setEmail("1@1.com");

        mapper.insert(user);
    }
}
