/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.util.List;
import websiteschema.common.amqp.Message;
import websiteschema.common.amqp.RabbitQueue;
import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EI(name = {"NEW:ADDONE", "ADD:ADD"})
@EO(name = {"EO", "FATAL"})
@Description(desc = "将URL添加至URL Queue")
public class FBURLQueue extends FunctionBlock {

    @DI(name = "HOST", desc = "RabbitMQ server host")
    public String host;
    @DI(name = "PORT", desc = "RabbitMQ server port")
    public int port;
    @DI(name = "QUEUE", desc = "RabbitMQ server queue name")
    public String queueName;
    @DI(name = "URL", desc = "需要采集的链接")
    public String url;
    @DI(name = "LINKS", desc = "需要采集的链接")
    public List<String> links;
    @DI(name = "JOBNAME", desc = "起始URL的jobname")
    public String jobname;
    @DI(name = "SITEID", desc = "起始URL的站点ID")
    public String siteId;
    @DI(name = "SID", desc = "startURLId")
    public long startURLId;
    @DI(name = "WID", desc = "wrapperId")
    public long wrapperId;
    @DI(name = "JID", desc = "jobId")
    public long jobId;
    @DI(name = "CFG", desc = "configure")
    public String configure;
    @DI(name = "DEPTH", desc = "URL深度")
    public int depth;

    @Algorithm(name = "ADDONE", desc = "将添加链接保存至HBase存储")
    public void addOne() {
        try {
            RabbitQueue<Message> queue = new RabbitQueue<Message>(host, port, queueName, true);
            Message msg = new Message(jobId, startURLId, wrapperId,
                    siteId, jobname,
                    url, //URL
                    configure, depth);
            queue.offer(msg);
            triggerEvent("EO");
        } catch (Exception ex) {
            triggerEvent("FATAL");
        }
    }

    @Algorithm(name = "ADD", desc = "将添加链接保存至HBase存储")
    public void add() {
        try {
            if (null != links && links.size() > 0) {
                RabbitQueue<Message> queue = new RabbitQueue<Message>(host, port, queueName, true);
                for (String u : links) {
                    Message msg = new Message(jobId, startURLId, wrapperId,
                            siteId, jobname,
                            u, //URL
                            configure, depth);
                    queue.offer(msg);
                }
            }
            triggerEvent("EO");
        } catch (Exception ex) {
            triggerEvent("FATAL");
        }
    }
}
