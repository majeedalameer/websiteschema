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

    public JobMessageReceiver() {
        String host = DeviceContext.getInstance().getConf().
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
        String appConfig = wrapper.getApplication();
        if (Wrapper.TYPE_FB.equals(wrapper.getWrapperType())) {
            Application app = new Application(msg.getTaskId());
            RuntimeContext runtimeContext = app.getContext();
            InputStream is = convertToInputStream(appConfig);
            if (null != is) {
                //加载Wrapper，将Job的配置转为Map，并设置为Filter。
                runtimeContext.loadConfigure(is, convertToMap(msg.getConfigure(), msg.getUrl()));
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

    public Map<String, String> convertToMap(String properties, String url) {
        try {
            InputStream is = convertToInputStream(properties);
            Properties prop = new Properties();
            prop.load(is);
            Map<String, String> ret = new HashMap<String, String>(prop.size());
            for (String key : prop.stringPropertyNames()) {
                ret.put(key, prop.getProperty(key));
            }
            if (null != url) {
                ret.put("URL", url);
            }
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
