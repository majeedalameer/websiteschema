/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.fb;

import com.apc.indextask.idx.Idx;
import com.autonomy.aci.services.IndexingService;
import java.util.ArrayList;
import java.util.List;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EO(name = {"EO"})
@EI(name = {"EI:SEND", "IDX:SEND_IDX"})
public class DreAddDataFB extends FunctionBlock {

    @DI(name = "IN")
    public String content = null;
    @DI(name = "IDX")
    public Idx idx = null;
    @DI(name = "SERVER")
    public List<String> servers = null;
    @DO(name = "INDEX", relativeEvents = {"EO"})
    public long indexNumber = 0;

    @Algorithm(name = "SEND_IDX")
    public void sendIdx() {
        if (null != idx) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(idx.toString()).append("\r\n\r\n");

                if (!sendIdxFile(sb.toString())) {
                    throw new RuntimeException("DreAddDataFB: can not sent IDX.");
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        } else {
            throw new RuntimeException("DreAddDataFB: input IDX is null");
        }
        triggerEvent("EO");
    }

    @Algorithm(name = "SEND")
    public void send() {
        if (null != content) {
            try {
                if (!sendIdxFile(content)) {
                    throw new RuntimeException("DreAddDataFB: can not sent content.");
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        } else {
            throw new RuntimeException("DreAddDataFB: input content is null");
        }
        triggerEvent("EO");
    }

    private IndexingService[] createIndexingServices() {
        IndexingService[] indexServices = new IndexingService[servers.size()];
        for (int i = 0; i < servers.size(); i++) {
            String server = servers.get(i);
            String host_port[] = server.split(":");
            if (host_port.length == 2) {
                String host = host_port[0];
                int port = Integer.valueOf(host_port[1]);

                indexServices[i] = new IndexingService(host, port);
                indexServices[i].setIndexEncoding("UTF-8");
            }
        }
        return indexServices;
    }

    private boolean sendIdxFile(String content) {
        boolean success = false;
        IndexingService[] indexServices = createIndexingServices();
        l.debug("created indexing service.");
        for (IndexingService service : indexServices) {
            try {
                indexNumber = service.dreAdd(content, new ArrayList());
            } catch (Exception ex) {
                //ex.printStackTrace();
            } finally {
                if (indexNumber > 0) {
                    l.info("Sent file : index=" + indexNumber);
                    //至少发送出去一个为成功。
                    success = true;
                } else {
                    l.warn("can not send file to " + service.getIndexHost() + ":" + service.getIndexPort());
                }
            }
        }
        return success;
    }
}
