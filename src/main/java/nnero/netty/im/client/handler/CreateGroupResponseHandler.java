package nnero.netty.im.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.client.Client;
import nnero.netty.im.client.ClientIM;
import nnero.netty.im.protocol.packet.Command;
import nnero.netty.im.protocol.packet.CreateGroupResponse;

/**
 * Author: NNERO
 * Time : 下午5:04 19-2-21
 */
@Slf4j
@Single
@ChannelHandler.Sharable
@ClientIM(Command.CREATE_GROUP_RESPONSE)
public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreateGroupResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResponse msg) throws Exception {
        if(msg.isSuccess()){
            log.info("Create success!");
        } else {
            log.info(msg.getReason());
        }
    }
}
