package nnero.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: NNERO
 * Time : 上午11:13 19-2-15
 */
public class RedisConnectionPool {

    private static final Logger LOG = LoggerFactory.getLogger(RedisConnectionPool.class);

    private String host;

    private int port;

    private String auth;

    private int db;

    private int createdConns;

    private int maxConns;

    private ReadWriteLock lock;

    private BlockingQueue<RedisConnection> availableConnQueue;

    private List<RedisConnection> allConns;

    private boolean max;

    public RedisConnectionPool(int maxConns,String host,int port,String auth,int db){
        this.host = host;
        this.port = port;
        this.auth = auth;
        this.db = db;
        this.maxConns = maxConns;
        lock = new ReentrantReadWriteLock();
        availableConnQueue = new LinkedBlockingDeque<>();
        allConns = new ArrayList<>();
    }

    public RedisConnection getConnection(){
        while (true) {
            try {
                if(!max) {
                    synchronized (RedisConnectionPool.class) {
                        if (!max) {
                            RedisConnection connection = new RedisConnection(host, port, auth, db);
                            createdConns++;
                            if(createdConns == maxConns){
                                max = true;
                            }
                            allConns.add(connection);
                            return connection;
                        }
                    }
                }
                RedisConnection connection = availableConnQueue.take();
                return connection;
            } catch (InterruptedException e) {
                LOG.error("", e);
            }
        }
    }

//    public RedisConnection getConnection(){
//        while (true) {
//            try {
//                Lock read = lock.readLock();
//                read.lock();
//                if(!max) {
//                    read.unlock();
//                    Lock write = lock.writeLock();
//                    write.lock();
//                    if(!max) {
//                        RedisConnection connection = new RedisConnection(host, port, auth, db);
//                        createdConns++;
//                        if (createdConns == maxConns) {
//                            max = true;
//                        }
//                        allConns.add(connection);
//                        write.unlock();
//                        return connection;
//                    }
//                    write.unlock();
//                } else {
//                    read.unlock();
//                }
//                RedisConnection connection = availableConnQueue.take();
//                return connection;
//            } catch (InterruptedException e) {
//                LOG.error("", e);
//            }
//        }
//    }

    public void recycle(RedisConnection connection) {
        try {
            availableConnQueue.put(connection);
        } catch (InterruptedException e) {
            LOG.error("",e);
        }
    }

    public void close() throws IOException {
        if(allConns != null && allConns.size() > 0){
            for(RedisConnection conn : allConns){
                conn.close();
            }
        }
    }
}
