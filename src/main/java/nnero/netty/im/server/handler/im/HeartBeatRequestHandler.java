package nnero.netty.im.server.handler.im;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.protocol.packet.Command;
import nnero.netty.im.protocol.packet.HeartBeatRequest;
import nnero.netty.im.protocol.packet.HeartBeatResponse;
import nnero.netty.im.server.ServerIM;

/**
 * Author: NNERO
 * Time : 上午11:32 19-2-22
 */
@Slf4j
@ChannelHandler.Sharable
@Single
@ServerIM(Command.HEART_BEAT_REQUEST)
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequest msg) throws Exception {
        log.info("receive heart beat: "+msg.getUid());
        HeartBeatResponse response = new HeartBeatResponse();
        ctx.writeAndFlush(response);
    }
}
