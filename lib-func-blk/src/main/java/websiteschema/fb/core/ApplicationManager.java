/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.fb.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;

/**
 *
 * @author ray
 */
public class ApplicationManager implements ApplicationService {

    public static final long MaxTaskNumber = 100;
    List<Future> fList = new ArrayList<Future>();
    private int poolSize = 5;
    private ExecutorService pool = null;

    public ApplicationManager() {
        init(poolSize);
    }

    private void init(int size) {
        pool = Executors.newFixedThreadPool(size, new ThreadFactory() {

            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
        });
    }

    @Override
    public void finalize() {
        if (null != pool) {
            pool.shutdown();
        }
    }

    public boolean addTask(Runnable task) {
        int Running = getRunningThreadNumber();
        if (Running > MaxTaskNumber) {
            return false;
        } else {
            boolean ret = true;
            Future f = null;
            try {
                f = pool.submit(task);
                if (null != f) {
                    fList.add(f);
                }
            } catch (RejectedExecutionException ex) {
                ret = false;
            }
            return ret;
        }
    }

    public int getRunningThreadNumber() {
        for (Iterator<Future> it = fList.iterator(); it.hasNext();) {
            Future f = it.next();
            if (f.isDone()) {
                it.remove();
            }
        }
        return fList.size();
    }

    public void shutdown() {
        pool.shutdown();
    }

    public boolean isTerminated() {
        return pool.isTerminated();
    }

    public boolean isShutdown() {
        return pool.isShutdown();
    }

    public boolean startup(Application app) {
        return addTask(app);
    }
}
