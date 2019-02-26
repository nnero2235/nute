package nnero.netty.im.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.protocol.packet.Packet;

import java.util.List;

/**
 * Author: NNERO
 * Time : 上午11:06 19-2-22
 */
@Slf4j
@Single
public class MessageCodeC extends MessageToMessageCodec<ByteBuf, Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
        log.debug("encode...");
        ByteBuf byteBuf = CodeC.encode(ctx.alloc().buffer(),msg);
        out.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        log.debug("decode...");
        Packet packet = CodeC.decode(msg);
        out.add(packet);
    }
}
