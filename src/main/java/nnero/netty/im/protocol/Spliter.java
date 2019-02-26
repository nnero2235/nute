package nnero.netty.im.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Author: NNERO
 * Time : 上午10:36 19-2-21
 */
@Slf4j
public class Spliter extends LengthFieldBasedFrameDecoder {

    public Spliter() {
        super(Integer.MAX_VALUE,7,4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        int magic = in.getInt(in.readerIndex());
        if(magic != CodeC.MAGIC){
            log.debug("magic is wrong:"+magic+" abandon packet!");
            return null;
        }
        return super.decode(ctx, in);
    }
}
