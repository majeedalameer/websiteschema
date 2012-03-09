/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.schedule;

import java.io.IOException;
import org.apache.log4j.Logger;
import websiteschema.common.amqp.Message;
import websiteschema.common.amqp.RabbitQueue;
import websiteschema.metadata.utils.MetadataServerContext;

/**
 *
 * @author ray
 */
public class TaskHandler {

    private static TaskHandler ins = new TaskHandler();

    public static TaskHandler getInstance() {
        return ins;
    }
    RabbitQueue<Message> queue;
    private Logger l = Logger.getLogger(TaskHandler.class);

    TaskHandler() {
        String host = MetadataServerContext.getInstance().getConf().
                getProperty("URLQueue", "ServerHost", "localhost");
        String queueName = MetadataServerContext.getInstance().getConf().
                getProperty("URLQueue", "QueueName", "url_queue");
        queue = new RabbitQueue<Message>(host, queueName);
    }

    public RabbitQueue<Message> getQueue() {
        return queue;
    }
}
