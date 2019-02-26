package nnero.redis;

import nnero.redis.impl.RedisResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Author: NNERO
 * Time : 上午11:11 19-2-15
 */
public class RedisClient {

    private static final Logger LOG = LoggerFactory.getLogger(RedisClient.class);

    private RedisConnectionPool pool;

    public RedisClient(int maxConns,String host, int port){
        this(maxConns,host,port,null,0);
    }

    public RedisClient(int maxConns,String host, int port, String auth){
        this(maxConns,host,port,auth,0);
    }

    public RedisClient(int maxConns,String host, int port, String auth, int db){
        pool = new RedisConnectionPool(maxConns,host,port,auth,db);
    }

    public String debug(String c,String key) throws IOException {
        RedisConnection conn = null;
        try {
            conn = pool.getConnection();
            return conn.debug(c,key);
        } finally {
            pool.recycle(conn);
        }
    }

    public String get(String key) throws IOException {
        RedisConnection conn = null;
        try {
            conn = pool.getConnection();
            RedisResponse response = conn.sendCommand("GET",key);
            return response.getStrData();
        } finally {
            pool.recycle(conn);
        }
    }

    public List<String> mGet(String... keys) throws IOException {
        RedisConnection conn = null;
        try {
            conn = pool.getConnection();
            RedisResponse response = conn.sendCommand("MGET", keys);
            return response.getStrListData();
        }finally {
            pool.recycle(conn);
        }
    }

    public boolean set(String key,String value) throws IOException {
        RedisConnection conn = null;
        try {
            conn = pool.getConnection();
            RedisResponse response = conn.sendCommand("SET", key, value);
            return "OK".equals(response.getStrData());
        }finally {
            pool.recycle(conn);
        }
    }

    public List<String> keys(String pattern) throws IOException {
        RedisConnection conn = null;
        try {
            conn = pool.getConnection();
            RedisResponse response = conn.sendCommand("KEYS", pattern);
            return response.getStrListData();
        }finally {
            pool.recycle(conn);
        }
    }

    public ScanResult scan(String cursor) throws IOException {
        RedisConnection conn = null;
        try {
            conn = pool.getConnection();
            RedisResponse response = conn.sendCommand("SCAN", cursor);
            List<String> r = response.getStrListData();
            return new ScanResult(r.get(0), r.subList(1, r.size()));
        }finally {
            pool.recycle(conn);
        }
    }

    public void close(){
        if(pool != null){
            try {
                pool.close();
            } catch (IOException e) {
                LOG.error("",e);
            }
        }
    }
}
