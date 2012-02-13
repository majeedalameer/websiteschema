/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device.job;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import websiteschema.common.amqp.Message;
import websiteschema.common.amqp.RabbitQueue;
import websiteschema.common.base.Function;
import websiteschema.device.DeviceContext;
import websiteschema.device.handler.WrapperHandler;
import websiteschema.fb.core.app.Application;
import websiteschema.fb.core.RuntimeContext;
import websiteschema.model.domain.Wrapper;

/**
 *
 * @author ray
 */
public class JobMessageReceiver implements Runnable {

    private RabbitQueue<Message> queue;
    private boolean isStop = false;
    private Logger l = Logger.getLogger(JobMessageReceiver.class);
    private String host = null;

    public JobMessageReceiver() {
        host = DeviceContext.getInstance().getConf().
                getProperty("URLQueue", "ServerHost", "localhost");
        String queueName = DeviceContext.getInstance().getConf().
                getProperty("URLQueue", "QueueName", "url_queue");
        try {
            queue = new RabbitQueue<Message>(host, queueName, false);
        } catch (IOException ex) {
            l.error("can not initialize URL Queue.", ex);
        }
    }

    public void stop() {
        isStop = true;
    }

    @Override
    public void run() {
        while (!isStop && null != queue) {
            Message message = queue.poll(Message.class, new Function<Message>() {

                /**
                 * 保存消息，然后将消息添加到AppRuntime中运行。
                 */
                @Override
                public void invoke(Message msg) {
                    // 获取Wrapper
                    long wrapperId = msg.getWrapperId();
                    Wrapper wrapper = WrapperHandler.getInstance().getWrapper(wrapperId);
                    // 创建任务并执行
                    createApplication(msg, wrapper);
                }
            });
        }
    }

    private void createApplication(Message msg, Wrapper wrapper) {
        if (Wrapper.TYPE_FB.equals(wrapper.getWrapperType())) {
            Application app = new Application(msg.getTaskId());
            // 功能块网络运行5分钟超时
            int timeout = DeviceContext.getInstance().getConf().
                    getIntProperty("Device", "AppTimeout", 5 * 60 * 1000);
            app.setTimeout(timeout);
            RuntimeContext runtimeContext = app.getContext();
            String appConfig = wrapper.getApplication();
            InputStream is = convertToInputStream(appConfig);
            if (null != is) {
                //加载Wrapper，将Job的配置转为Map，并设置为Filter。
                runtimeContext.loadConfigure(is, convertToMap(msg));
                boolean inserted = DeviceContext.getInstance().getAppRuntime().startup(app);
                while (!inserted) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                    }
                    inserted = DeviceContext.getInstance().getAppRuntime().startup(app);
                }
            }
        }
    }

    public InputStream convertToInputStream(String content) {
        try {
            return new ByteArrayInputStream(content.getBytes("UTF-8"));
        } catch (Exception ex) {
            return null;
        }
    }

    private Map<String, String> getDefaultConfig() {
        return DeviceContext.getInstance().getConf().
                getAllPropertiesInField("FBApp");
    }

    public Map<String, String> convertToMap(Message msg) {
        try {
            String properties = msg.getConfigure();
            String url = msg.getUrl();
            String siteId = msg.getSiteId();
            String jobname = msg.getJobname();
            InputStream is = convertToInputStream(properties);
            Properties prop = new Properties();
            prop.load(is);
            Map<String, String> def = getDefaultConfig();
            Map<String, String> ret = null;
            if (null == def) {
                ret = new HashMap<String, String>(prop.size());
            } else {
                ret = new HashMap<String, String>(def);
            }
            for (String key : prop.stringPropertyNames()) {
                ret.put(key, prop.getProperty(key));
            }
            if (null != url) {
                ret.put("URL", url);
            }
            if (null != siteId) {
                ret.put("SITEID", siteId);
            }
            if (null != jobname) {
                ret.put("JOBNAME", jobname);
            }
            ret.put("STARTURLID", String.valueOf(msg.getStartURLId()));
            ret.put("JOBID", String.valueOf(msg.getJobId()));
            ret.put("SCHEID", String.valueOf(msg.getScheId()));
            ret.put("WRAPPERID", String.valueOf(msg.getWrapperId()));
            ret.put("QUEUE_SERVER", host);
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
