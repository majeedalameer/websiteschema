/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author ray
 */
public class SpringContext {

    private static final ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");

    public static ApplicationContext getContext() {
        return ctx;
    }
}
