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
import nnero.netty.im.protocol.packet.JoinGroupResponse;

import java.util.List;

/**
 * Author: NNERO
 * Time : 下午5:53 19-2-21
 */
@Slf4j
@Single
@ChannelHandler.Sharable
@ClientIM(Command.JOIN_GROUP_RESPONSE)
public class JoinGroupResponseHandler extends SimpleChannelInboundHandler<JoinGroupResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupResponse msg) throws Exception {
        if(msg.isSuccess()){
            List<String> names = msg.getOnlineUsers();
            StringBuilder sb = new StringBuilder();
            if(names == null || names.size() == 0){
                sb.append("[Group->").append(msg.getGroupName()).append(" members]: is only you!");
            } else {
                sb.append("[Group->").append(msg.getGroupName()).append(" members]: ");
                for(String name: names){
                    sb.append("[").append(name).append("] ");
                }
            }
            ctx.channel().attr(ClientAttrs.GROUP).set(msg.getGroupId());
            log.info(sb.toString());
        } else {
            log.info(msg.getReason());
        }
    }
}
