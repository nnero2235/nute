package nnero.netty.im.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: NNERO
 * Time : 上午11:51 19-2-22
 */
public class ThreadPool {

    private static final ExecutorService POOL = Executors.newFixedThreadPool(100);

    public static void exec(Runnable r){
        if(r == null){
            return;
        }
        POOL.execute(r);
    }

}
