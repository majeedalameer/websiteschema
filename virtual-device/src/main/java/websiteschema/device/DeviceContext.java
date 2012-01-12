/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.conf.Configure;
import websiteschema.device.runtime.ApplicationServiceImpl;
import websiteschema.fb.core.app.ApplicationManager;
import websiteschema.fb.core.app.ApplicationService;
import websiteschema.persistence.rdbms.TaskMapper;

/**
 *
 * @author ray
 */
public class DeviceContext {

    private static final ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
    private static DeviceContext ins = new DeviceContext();

    public static DeviceContext getInstance() {
        return ins;
    }

    public static ApplicationContext getSpringContext() {
        return ctx;
    }
    private ApplicationService appRuntime;
    private Configure conf;
    private String home;
    private String tempDir;
    private String cacheDir;
    private Logger l = Logger.getLogger(DeviceContext.class);

    DeviceContext() {
        try {
            conf = Configure.createConfigure("configure-site.ini");
            if (null != conf) {
                home = conf.getProperty("home", "");
                tempDir = conf.getProperty("tempDir", "temp");
                cacheDir = conf.getProperty("cacheDir", "cache");
            } else {
                l.error("Can not load configuration file: configure-site.ini");
                System.exit(0);
            }
            ApplicationServiceImpl as = new ApplicationServiceImpl();
            as.setTaskMapper(ctx.getBean("taskMapper", TaskMapper.class));
            appRuntime = as;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ApplicationService getAppRuntime() {
        return appRuntime;
    }

    public String getCacheDir() {
        return cacheDir;
    }

    public Configure getConf() {
        return conf;
    }

    public String getHome() {
        return home;
    }

    public String getTempDir() {
        return tempDir;
    }
}
