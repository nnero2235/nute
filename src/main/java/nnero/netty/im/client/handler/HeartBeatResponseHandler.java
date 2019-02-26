package nnero.netty.im.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.client.ClientIM;
import nnero.netty.im.protocol.packet.Command;
import nnero.netty.im.protocol.packet.HeartBeatResponse;

/**
 * Author: NNERO
 * Time : 上午11:34 19-2-22
 */
@Slf4j
@Single
@ChannelHandler.Sharable
@ClientIM(Command.HEART_BEAT_RESPONSE)
public class HeartBeatResponseHandler extends SimpleChannelInboundHandler<HeartBeatResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatResponse msg) throws Exception {
        log.debug("receive server heartBeat...");
    }
}
