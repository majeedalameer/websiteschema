/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
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

    @Test
    public void testMap() {
        try {
            Map<String,String> map = new HashMap<String,String>();
            map.put("a", "b");
            map.put("c", "d");
            List list = new ArrayList();
            list.add(map);
            String json = toJson(list);
            System.out.println(json);
            List<Map<String,String>> res = fromJson(json, List.class);
            System.out.println(res);
            assert(res.get(0).get("a").equals("b"));
        } catch (Exception ex) {
            ex.printStackTrace();
            assert (false);
        }
    }
}
