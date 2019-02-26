package nnero.netty.im.server.handler.im;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.protocol.packet.Command;
import nnero.netty.im.server.ServerIM;
import nnero.netty.im.server.entity.Session;
import nnero.netty.im.server.ServerAttrs;
import nnero.netty.im.server.GroupManager;
import nnero.netty.im.protocol.packet.MessageRequest;
import nnero.netty.im.protocol.packet.MessageResponse;
import nnero.netty.im.thread.ThreadPool;

/**
 * Author: NNERO
 * Time : 下午6:00 19-2-20
 */
@Slf4j
@Single
@ChannelHandler.Sharable
@ServerIM(Command.MESSAGE_REQUEST)
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequest msg) throws Exception {
        ThreadPool.exec(()->{
            log.debug("message server handle...");
            MessageResponse response = new MessageResponse();
            Channel channel = ctx.channel();
            Session session = channel.attr(ServerAttrs.SESSION).get();
            log.info("["+session.getUsername()+"]:"+msg.getContent()+" ---> group:"+msg.getGroupId());

            ChannelGroup channelGroup = GroupManager.getChannelGroup(msg.getGroupId());
            if(channelGroup != null){
                response.setSuccess(true);
                response.setReason("OK");
                response.setContent(msg.getContent());
                response.setUsername(session.getUsername());
                response.setMessageId(String.valueOf(System.currentTimeMillis()));
                channelGroup.writeAndFlush(response);
            } else {
                response.setSuccess(false);
                response.setReason("group:"+msg.getGroupId()+" is not exits!");
                response.setContent("");
                response.setMessageId(String.valueOf(System.currentTimeMillis()));
                ctx.channel().writeAndFlush(response);
            }
        });
    }
}
