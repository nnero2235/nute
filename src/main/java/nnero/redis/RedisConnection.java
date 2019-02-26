package nnero.redis;

import com.google.common.base.Strings;
import nnero.net.Transport;
import nnero.net.impl.TCPTransport;
import nnero.redis.impl.RedisProtocol;
import nnero.redis.impl.RedisResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Author: NNERO
 * Time : 下午3:04 19-2-14
 * 封装protocol transport
 */
public class RedisConnection {

    private static final Logger LOG = LoggerFactory.getLogger(RedisConnection.class);

    private String host;

    private int port;

    private String auth;

    private int db;

    private Transport transport;

    private Protocol protocol;

    public RedisConnection(String host, int port, String auth, int db){
        this.host = host;
        this.port = port;
        this.auth = auth;
        this.db = db;
    }

    private void checkConnect() throws IOException {
        if(transport == null){
            synchronized (RedisConnection.class) {
                if(transport == null) {
                    transport = new TCPTransport(host, port);
                }
            }
        }
        if(protocol == null){
            synchronized (RedisConnection.class) {
                if (protocol == null) {
                    protocol = new RedisProtocol(transport);
                    if (!Strings.isNullOrEmpty(auth)) {
                        RedisResponse response = protocol.sendCommand(buildCommand("AUTH", auth));
                        if (response.getType() != ResponseType.STATUS || !"OK".equals(response.getStrData())) {
                            throw new RuntimeException("redis connect auth failure! auth:" + auth);
                        } else {
                            LOG.debug("auth success");
                        }
                    }
                    RedisResponse response = protocol.sendCommand(buildCommand("SELECT", String.valueOf(db)));
                    if (response.getType() != ResponseType.STATUS || !"OK".equals(response.getStrData())) {
                        throw new RuntimeException("redis db select failure! db:" + db + " detail:" + response.getStrData());
                    } else {
                        LOG.debug("select db:" + db);
                    }
                }
            }
        }
    }

    private void checkClose(){
        if(transport != null && transport.isClosed()){
            throw new RuntimeException("can't send data to server.connection was closed");
        }
    }

    private String buildCommand(String c,String... args){
        StringBuilder sb = new StringBuilder();
        sb.append(c);
        if(args != null && args.length > 0){
            for(String a : args){
                sb.append(" ").append(a);
            }
        }
        return sb.toString();
    }

    public synchronized RedisResponse sendCommand(String c,String... args) throws IOException {
        checkClose();
        checkConnect();
        RedisResponse response = protocol.sendCommand(buildCommand(c,args));
        return response;
    }

    public synchronized String debug(String c,String... args) throws IOException {
        checkClose();
        checkConnect();
        return protocol.debugCommand(buildCommand(c,args));
    }

    public void close() throws IOException {
        if(transport != null && !transport.isClosed()){
            transport.close();
        }
    }
}
