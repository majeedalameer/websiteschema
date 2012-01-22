package websiteschema.common.amqp;

import websiteschema.common.base.Function;
import org.apache.log4j.Logger;
import java.io.IOException;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
import static websiteschema.utils.PojoMapper.*;

/**
 *
 * @author ray
 */
public class RabbitQueue<T> {

    ConnectionFactory factory = new ConnectionFactory();
    Connection connection;
    Channel channel;
    QueueingConsumer consumer;
    String queueName;
    String charset = "UTF-8";
    Logger l = Logger.getLogger(RabbitQueue.class);

    public RabbitQueue(String host, String queueName, boolean sender) throws IOException {
        this(host, -1, queueName, sender);
    }

    public RabbitQueue(String host, int port, String queueName, boolean producer) throws IOException {
        factory = new ConnectionFactory();
        factory.setHost(host);
        if (port > 0) {
            factory.setPort(port);
        }
        // 创建到RabbitMQ服务器的链接
        connection = factory.newConnection();
        // 创建一个通道
        channel = connection.createChannel();
        this.queueName = queueName;
        // 声明一个可持久化的队列
        channel.queueDeclare(queueName, true, false, false, null);

        if (!producer) {
            // 声明此队列的消费者
            channel.basicQos(1);
            consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, false, consumer);
        }
    }

    public boolean offer(T msg) {
        try {
            String json = toJson(msg);
            channel.basicPublish("", queueName,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    json.getBytes(charset));
            l.debug(" [x] Sent '" + json + "'");
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public T poll(Class<T> clazz) {
        if (null != consumer) {
            QueueingConsumer.Delivery delivery = null;
            T msg = null;
            try {
                // 等待服务器的消息。
                delivery = consumer.nextDelivery();

                String message = new String(delivery.getBody(), charset);

                l.debug(" [x] Received '" + message + "'");
                msg = fromJson(message, clazz);
                l.debug(" [x] Done");
            } catch (Exception ex) {
                ex.printStackTrace();
                l.error("could not wait for message: ", ex);
            } finally {
                // 应答服务器，表明已经收到消息。
                try {
                    if (null != delivery) {
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return msg;
        }
        return null;
    }

    public T poll(Class<T> clazz, Function<T> worker) {
        if (null != consumer) {
            QueueingConsumer.Delivery delivery = null;
            T msg = null;
            try {
                // 等待服务器的消息。
                delivery = consumer.nextDelivery();

                String message = new String(delivery.getBody(), charset);

                l.debug(" [x] Received '" + message + "'");
                msg = fromJson(message, clazz);
                // 对消息进行简单处理
                worker.invoke(msg);
                l.debug(" [x] Done");
            } catch (Exception ex) {
                ex.printStackTrace();
                l.error("could not wait for message: ", ex);
            } finally {
                // 应答服务器，表明已经收到消息。
                try {
                    if (null != delivery) {
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return msg;
        }
        return null;
    }

    public void close() throws IOException {
        channel.close();
        connection.close();
    }

    public String getQueueName() {
        return queueName;
    }
}
