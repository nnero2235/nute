package nnero.netty.redis;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.nio.charset.Charset;

/**
 * Author: NNERO
 * Time : 上午11:06 19-2-26
 */
public class RedisSpliter extends DelimiterBasedFrameDecoder {

    public RedisSpliter() {
        super(Integer.MAX_VALUE, true, Unpooled.copiedBuffer("\r\n".getBytes(Charset.defaultCharset())));
    }
}
