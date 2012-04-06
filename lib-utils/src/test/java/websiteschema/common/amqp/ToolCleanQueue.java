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
        Message msg = queue.poll(Message.class, 1000);
        while (msg != null) {
            msg = queue.poll(Message.class, 1000);
        }
        queue.close();
    }

    public static void main(String args[]) {
        String server = "localhost";
        String queueName = "url_queue";
        new ToolCleanQueue().cleanQueue(server, queueName);
    }
}
