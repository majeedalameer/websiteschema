/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.fb.task;

import java.io.*;
import websiteschema.util.FBConfigureUtil;

/**
 *
 * @author ray
 */
public class Task {


    public static void main(String args[]) throws IOException {
        Task task = new Task();
        String file = task.getClass().getClassLoader().getResource("").toString() + "sample" + File.separator + "sample.xml";
        file = file.substring(file.indexOf("/"));
        System.out.println(file);
        SubTask t = FBConfigureUtil.getFBConfigureUtil().initTask(file);
        t.StartTask();
    }
}
