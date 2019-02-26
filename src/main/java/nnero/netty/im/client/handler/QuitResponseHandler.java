package nnero.netty.im.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.client.Client;
import nnero.netty.im.client.ClientAttrs;
import nnero.netty.im.client.ClientIM;
import nnero.netty.im.protocol.packet.Command;
import nnero.netty.im.protocol.packet.QuitGroupResponse;

/**
 * Author: NNERO
 * Time : 上午11:14 19-2-25
 */
@Single
@Slf4j
@ChannelHandler.Sharable
@ClientIM(Command.QUIT_GROUP_RESPONSE)
public class QuitResponseHandler extends SimpleChannelInboundHandler<QuitGroupResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupResponse msg) throws Exception {
        if(msg.isSuccess()){
            log.info("Quit success.");
            ctx.channel().attr(ClientAttrs.GROUP).set(null);
        } else {
            log.info("[system]: "+msg.getReason());
        }
    }
}
