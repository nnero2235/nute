package nnero.netty.im.server.handler.im;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.protocol.packet.Command;
import nnero.netty.im.protocol.packet.MessageResponse;
import nnero.netty.im.protocol.packet.QuitGroupRequest;
import nnero.netty.im.protocol.packet.QuitGroupResponse;
import nnero.netty.im.server.GroupManager;
import nnero.netty.im.server.ServerAttrs;
import nnero.netty.im.server.ServerIM;
import nnero.netty.im.server.entity.Group;
import nnero.netty.im.server.entity.Session;

/**
 * Author: NNERO
 * Time : 上午11:15 19-2-25
 */
@Slf4j
@Single
@ChannelHandler.Sharable
@ServerIM(Command.QUIT_GROUP_REQUEST)
public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupRequest msg) throws Exception {
        Group group = ctx.channel().attr(ServerAttrs.GROUP).get();
        Session session = ctx.channel().attr(ServerAttrs.SESSION).get();
        QuitGroupResponse response = new QuitGroupResponse();
        if(group == null){
            response.setSuccess(false);
            response.setReason("You are not joined any group!");
        } else {
            GroupManager.removeChannel(group.getGroup_id(),group.getGroup_name(),ctx.channel());

            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setSuccess(true);
            messageResponse.setReason("OK");
            messageResponse.setUsername(session.getUsername());
            messageResponse.setContent("quit Group!");
            ChannelGroup channelGroup = GroupManager.getChannelGroup(group.getGroup_id());
            channelGroup.writeAndFlush(messageResponse);

            response.setSuccess(true);
            response.setReason("OK");
        }
        ctx.writeAndFlush(response);
    }
}
