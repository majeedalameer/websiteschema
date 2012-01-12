package websiteschema.common.amqp;

/**
 * Job在执行过程中，会生成消息(Message)，此消息包含有一些配置信息，告诉接收者如何执行。<br/>
 * 消息将被发往Virtual-Device，由虚拟设备运行。
 * @author ray
 */
public class Message implements java.io.Serializable {

    private int hash;
    private int depth;
    private long jobId;
    private long startURLId;
    private long wrapperId;
    private long taskId;
    private long createTime;
    private String url;
    private String configure;

    public Message() {
    }

    public Message(long jobId, long startURLId, long wrapperId, String url, String configure) {
        this.jobId = jobId;
        this.startURLId = startURLId;
        this.wrapperId = wrapperId;
        this.url = url;
        this.configure = configure;
        depth = 0;
        hash = (url + jobId).hashCode();
        createTime = System.currentTimeMillis();
    }

    public Message(long jobId, long startURLId, long wrapperId, String url, String configure, int depth) {
        this.jobId = jobId;
        this.startURLId = startURLId;
        this.wrapperId = wrapperId;
        this.url = url;
        this.configure = configure;
        this.depth = depth;
        hash = (url + jobId).hashCode();
        createTime = System.currentTimeMillis();
    }

    public String getConfigure() {
        return configure;
    }

    public long getCreateTime() {
        return createTime;
    }

    public int getDepth() {
        return depth;
    }

    public long getJobId() {
        return jobId;
    }

    public long getStartURLId() {
        return startURLId;
    }

    public String getUrl() {
        return url;
    }

    public long getWrapperId() {
        return wrapperId;
    }

    public void setConfigure(String configure) {
        this.configure = configure;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public void setStartURLId(long startURLId) {
        this.startURLId = startURLId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setWrapperId(long wrapperId) {
        this.wrapperId = wrapperId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Message other = (Message) obj;
        if (this.hash != other.hash) {
            return false;
        }
        return true;
    }
}
