/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device.job;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;
import websiteschema.common.amqp.Message;
import websiteschema.common.amqp.RabbitQueue;
import websiteschema.common.base.Function;
import websiteschema.device.DeviceContext;
import websiteschema.device.handler.WrapperHandler;
import websiteschema.fb.core.Application;
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
        if ("FB".equals(wrapper.getWrapperType())) {
            Application app = new Application();
            RuntimeContext runtimeContext = app.getContext();
            InputStream is = convert(appConfig);
            if (null != is) {
                runtimeContext.loadConfigure(is);
                DeviceContext.getInstance().getAppRuntime().startup(app);
            }
        }
    }

    public InputStream convert(String content) {
        try {
            return new ByteArrayInputStream(content.getBytes("UTF-8"));
        } catch (Exception ex) {
            return null;
        }
    }
}
