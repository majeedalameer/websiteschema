/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import org.codehaus.jackson.map.JsonMappingException;
import websiteschema.common.amqp.Message;
import org.junit.Test;
import static websiteschema.utils.PojoMapper.*;

/**
 *
 * @author ray
 */
public class JSONTest {

    //{"url":"http://localhost:8080/docs","depth":0,"createTime":1323147713158,"jobId":4,"startURLId":1,"configure":"b=c\n# Test Info","wrapperId":1}
    @Test
    public void testMessage() {
        try {
            String json = "{\"url\":\"http://localhost:8080/docs\",\"depth\":0,\"createTime\":1323147713158,\"jobId\":4,\"startURLId\":1,\"configure\":\"b=c\\n# Test Info\",\"wrapperId\":1}";
            Message msg = fromJson(json, Message.class);
            System.out.println(msg.getConfigure());
            assert (msg.getJobId() == 4L);
        } catch (Exception ex) {
            ex.printStackTrace();
            assert (false);
        }
    }
}
