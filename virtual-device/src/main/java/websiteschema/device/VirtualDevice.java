/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package websiteschema.device;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import websiteschema.device.job.JobMessageReceiver;

public class VirtualDevice {

    Server server = null;
    JobMessageReceiver receiver = new JobMessageReceiver();
    boolean stop = false;

    public static void main(String[] args) throws Exception {
        VirtualDevice device = new VirtualDevice();
        device.startJetty();
        while (!device.isStop()) {
            Thread t = new Thread(device.getReceiver());
            t.setDaemon(true);
            t.start();

            t.join();
            //如果receiver结束了，则等待120秒，重新尝试连接rabbitmq
            sleep(120000);
        }
        device.stopJetty();
    }

    private static void sleep(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    public JobMessageReceiver getReceiver() {
        return receiver;
    }

    public boolean isStop() {
        return stop;
    }

    public void startJetty() throws Exception {
        Handler handler = new AbstractHandler() {

            @Override
            public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch)
                    throws IOException, ServletException {
                String path = request.getPathInfo();
                System.out.println(path);
                if ("/action=reload".equalsIgnoreCase(path)) {
                    DeviceContext.getInstance().load();
                    response.setContentType("text/plain");
                    response.getWriter().println("{\"success\":\"true\"}");
                    response.setStatus(HttpServletResponse.SC_OK);
                    ((Request) request).setHandled(true);
                } else if ("/action=getstatus".equalsIgnoreCase(path)) {
                    response.setContentType("text/xml");
                    response.getWriter().println(getStatus());
                    response.setStatus(HttpServletResponse.SC_OK);
                    ((Request) request).setHandled(true);
                } else {
                    response.setContentType("text/xml");
                    response.getWriter().println(getUnknownAction(path));
                    response.setStatus(HttpServletResponse.SC_OK);
                    ((Request) request).setHandled(true);
                }
            }
        };

        server = new Server(DeviceContext.getInstance().getConf().getIntProperty("Device", "port", 12207));
        server.setHandler(handler);
        server.start();
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();

        sb.append("<?xml version=\"1.0\"?>").
                append("<response>").
                append("<action>GETSTATUS</action>").
                append("<response>SUCCESS</response>").
                append("<responsedata>").
                append("<product>VIRTUAL-DEVICE</product>").
                append("<serviceport>").
                append(DeviceContext.getInstance().getConf().getProperty("Device", "port", "12207")).
                append("</serviceport>").
                append("</responsedata>").
                append("</response>");
        return sb.toString();
    }

    public String getUnknownAction(String action) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\"?>").
                append("<response>").
                append("<action>").
                append(action).
                append("</action>").
                append("<response>ERROR</response>").
                append("<responsedata>").
                append("<error>").
                append("<errorstring>The action you attempted is not recognized</errorstring>").
                append("<errorcode>UNKNOWN</errorcode>").
                append("</error>").
                append("</responsedata>").
                append("</response>");
        return sb.toString();
    }

    public void stopJetty() throws Exception {
        server.stop();
    }
}
