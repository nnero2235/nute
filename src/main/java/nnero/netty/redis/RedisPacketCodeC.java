package nnero.netty.redis;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import nnero.netty.redis.protocol.RedisPacket;

import javax.inject.Singleton;
import java.util.List;

/**
 * Author: NNERO
 * Time : 上午11:22 19-2-26
 */
@ChannelHandler.Sharable
@Singleton
public class RedisPacketCodeC extends MessageToMessageCodec<ByteBuf, RedisPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RedisPacket msg, List<Object> out) throws Exception {

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {

    }
}
