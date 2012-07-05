/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.fb;

import com.apc.indextask.idx.Idx;
import com.autonomy.aci.exceptions.IndexingException;
import com.autonomy.aci.services.IndexingService;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import websiteschema.common.base.Function;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.utils.FileUtil;
import websiteschema.utils.MD5;

/**
 *
 * @author ray
 */
@EO(name = {"EO"})
@EI(name = {"EI:SEND", "IDX:SEND_IDX", "CHK:CHECK"})
public class DreAddDataFB extends FunctionBlock {

    @DI(name = "IN")
    public String content = null;
    @DI(name = "IDX")
    public Idx idx = null;
    @DI(name = "TEMPDIR", desc = "存放Unsent数据的目录")
    public String tempDir = "temp/unsent";
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

                if (!sendIdxFile(sb.toString(), servers)) {
                    throw new RuntimeException("DreAddDataFB: can not sent IDX.");
                }
            } catch (Exception ex) {
                l.error(ex.getMessage(), ex);
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
                if (!sendIdxFile(content, servers)) {
                    throw new RuntimeException("DreAddDataFB: can not sent content.");
                }
            } catch (Exception ex) {
                l.error(ex.getMessage(), ex);
                throw new RuntimeException(ex.getMessage());
            }
        } else {
            throw new RuntimeException("DreAddDataFB: input content is null");
        }
        triggerEvent("EO");
    }

    @Algorithm(name = "CHECK")
    public void checkUnsentData() {
        scanUnsentData(tempDir);
        triggerEvent("EO");
    }

    private IndexingService[] createIndexingServices(List<String> servers) {
        IndexingService[] indexServices = new IndexingService[servers.size()];
        for (int i = 0; i < servers.size(); i++) {
            String server = servers.get(i);
            indexServices[i] = createIndexingService(server);
        }
        return indexServices;
    }

    private IndexingService createIndexingService(String server) {
        String host_port[] = server.split(":");
        if (host_port.length == 2) {
            String host = host_port[0];
            int port = Integer.valueOf(host_port[1]);

            IndexingService indexServices = new IndexingService(host, port);
            indexServices.setIndexEncoding("UTF-8");
            return indexServices;
        }
        return null;
    }

    private boolean sendIdxFile(String content, List<String> servers) {
        boolean success = false;
        IndexingService[] indexServices = createIndexingServices(servers);
        l.debug("created indexing service.");
        for (IndexingService service : indexServices) {
            try {
                indexNumber = service.dreAdd(content, new ArrayList());
            } catch (Exception ex) {
                saveUnsentData(service.getIndexHost() + ":" + service.getIndexPort(), content, tempDir);
                l.error(ex.getMessage(), ex);
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

    private void saveUnsentData(String server, String content, String tempDir) {
        if (null != server && !server.isEmpty() && null != content && !content.isEmpty()) {
            String data = null;
            try {
                data = server + "\n" + content;
                String md5 = MD5.getMD5(content.getBytes("UTF-8"));
                File dir = new File(tempDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File f = new File(dir.getAbsoluteFile() + File.separator + md5);
                if (!f.exists()) {
                    FileUtil.save(f, data);
                }
            } catch (Exception ex) {
                l.error(ex.getMessage(), ex);
            }
        }
    }

    private void scanUnsentData(String tempDir) {
        File dir = new File(tempDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileUtil.scan(dir, new Function<File>() {

            @Override
            public void invoke(File arg) {
                String data = FileUtil.read(arg);
                if (data.contains("\n")) {
                    String server = data.substring(0, data.indexOf("\n")).trim();
                    String content = data.substring(data.indexOf("\n")).trim();
                    IndexingService indexingService = createIndexingService(server);
                    try {
                        long indexNumber = indexingService.dreAdd(content, new ArrayList());
                        if (indexNumber > 0) {
                            arg.delete();
                        }
                    } catch (IndexingException ex) {
                        l.error(ex.getMessage(), ex);
                    }
                }
            }
        });
    }
}
