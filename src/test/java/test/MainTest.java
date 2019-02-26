package test;

import nnero.net.Transport;
import nnero.net.impl.TCPTransport;
import nnero.redis.*;
import nnero.redis.impl.RedisProtocol;
import nnero.redis.impl.RedisResponse;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Author: NNERO
 * Time : 下午2:46 19-2-13
 */
public class MainTest {

    private final static Logger LOG = LoggerFactory.getLogger(MainTest.class);

    @Test
    public void transport() throws IOException {
        Transport transport = new TCPTransport("192.168.1.254",6379);
        transport.send("AUTH ddkj\r\n");
        byte[] buf = new byte[128];
        int c = transport.receive(buf, 0, buf.length);
        LOG.info("C:"+c);
        LOG.info(new String(buf,0,c, Charset.defaultCharset()));
        transport.close();
        Assert.assertTrue(transport.isClosed());
    }

    @Test
    public void redisProtocol() throws IOException {
        Transport transport = new TCPTransport("192.168.1.254",6379);
        Protocol protocol = new RedisProtocol(transport);
        RedisResponse r = protocol.sendCommand("AUTH ddkj");
        Assert.assertEquals(r.getType(), ResponseType.STATUS);
        Assert.assertEquals(r.getStrData(),"OK");

        r = protocol.sendCommand("get nnero1");
        Assert.assertEquals(r.getType(), ResponseType.LEN);
        Assert.assertEquals(r.getStrData(),"1");

        r = protocol.sendCommand("get nnero99");
        Assert.assertEquals(r.getType(), ResponseType.LEN);
        Assert.assertEquals(r.getStrData(),"nil");

        r = protocol.sendCommand("rrr xxx");
        Assert.assertEquals(r.getType(), ResponseType.ERROR);
        LOG.info(r.getStrData());

        r = protocol.sendCommand("incr nnero");
        Assert.assertEquals(r.getType(),ResponseType.INT);
        LOG.info("Int: "+r.getIntData());

        r = protocol.sendCommand("mget nnero1 nnero2 nnero3");
        Assert.assertEquals(r.getType(), ResponseType.ARGC);
        List<String> data = r.getStrListData();
        for(String s : data){
            LOG.info(s);
        }

        r = protocol.sendCommand("lrange nnero10 0 -1");
        Assert.assertEquals(r.getType(), ResponseType.ARGC);
        data = r.getStrListData();
        for(String s : data){
            LOG.info(s);
        }

        transport.close();
        Assert.assertTrue(transport.isClosed());
    }

    @Test
    public void testRedisClient() throws IOException {
        RedisClient client = new RedisClient(1,"192.168.1.254",6379,"ddkj");
        LOG.info(client.get("nnero1"));
        List<String> values = client.mGet("nnero1","nnero2","nnero3","nnero4");
        for(String v : values){
            LOG.info(v);
        }
        LOG.info(client.set("nnero4","4")+"");
        values = client.keys("nnero*");
        for(String v : values){
            LOG.info(v);
        }
        String cursor = "-1";
        int round = 1;
        while (!cursor.equals("0")) {
            if(cursor.equals("-1")){
                cursor = "0";
            }
            ScanResult r = client.scan(cursor);
            cursor = r.getCursor();
            for(String v : r.getResult()){
                LOG.info(round+":"+v);
                round++;
            }
        }

        client.close();
    }

    @Test
    public void testSingleThread() throws IOException {
        Instant start = TestUtils.getNow();
        RedisClient client = new RedisClient(1,"192.168.1.254",6379,"ddkj");
        for(int i=0;i<10000;i++){
            LOG.info(client.get("nnero1"));
        }
        LOG.info(TestUtils.getCost(start));
    }

    @Test
    public void testMultiThread() throws InterruptedException {
        RedisClient client = new RedisClient(20,"192.168.1.254",6379,"ddkj");
        ExecutorService pool = Executors.newFixedThreadPool(20);
        for(int i=0;i<100000;i++){
            pool.execute(()->{
                try {
                    LOG.info(client.get("nnero1"));
                } catch (IOException e) {
                    LOG.error("",e);
                }
            });
        }
        pool.awaitTermination(7, TimeUnit.SECONDS);
    }

    @Test
    public void debugRedisClient() throws IOException {
        RedisClient client = new RedisClient(1,"192.168.1.254",6379,"ddkj");
        LOG.info(client.debug("SCAN","0"));
        client.close();
    }
}
