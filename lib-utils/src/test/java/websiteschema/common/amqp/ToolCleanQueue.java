/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.common.amqp;

import org.junit.Test;

/**
 *
 * @author ray
 */
public class ToolCleanQueue {

    public void cleanQueue(String server, String queueName) {
        RabbitQueue<Message> queue = new RabbitQueue<Message>(server, queueName);
        Message msg = queue.poll(Message.class);
        while (msg != null) {
            msg = queue.poll(Message.class);
        }
    }

    public static void main(String args[]) {
        String server = "192.168.4.122";
        String queueName = "url_queue";
        new ToolCleanQueue().cleanQueue(server, queueName);
    }
}
