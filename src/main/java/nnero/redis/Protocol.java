package nnero.redis;

import nnero.net.Transport;
import nnero.redis.impl.RedisRequest;
import nnero.redis.impl.RedisResponse;

import java.io.IOException;

/**
 * Author: NNERO
 * Time : 下午2:45 19-2-12
 * redis:协议抽象
 */
public interface Protocol {

    RedisResponse sendCommand(String command) throws IOException;

    String debugCommand(String command) throws IOException;
}
