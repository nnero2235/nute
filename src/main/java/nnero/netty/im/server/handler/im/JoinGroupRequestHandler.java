package nnero.netty.im.server.handler.im;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.protocol.packet.Command;
import nnero.netty.im.server.ServerIM;
import nnero.netty.im.server.db.DataBase;
import nnero.netty.im.server.entity.Group;
import nnero.netty.im.server.entity.Session;
import nnero.netty.im.server.ServerAttrs;
import nnero.netty.im.server.GroupManager;
import nnero.netty.im.protocol.packet.JoinGroupRequest;
import nnero.netty.im.protocol.packet.JoinGroupResponse;
import nnero.netty.im.protocol.packet.MessageResponse;
import nnero.netty.im.thread.ThreadPool;

import java.util.List;

/**
 * Author: NNERO
 * Time : 下午5:18 19-2-21
 */
@Slf4j
@Single
@ChannelHandler.Sharable
@ServerIM(Command.JOIN_GROUP_REQUEST)
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequest> {

    @Inject
    private DataBase dataBase;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequest msg) throws Exception {
        ThreadPool.exec(()->{
            Group group = ctx.channel().attr(ServerAttrs.GROUP).get();
            JoinGroupResponse response = new JoinGroupResponse();
            if(group == null){
                group = dataBase.getGroup(msg.getGroupId());
                if(group == null){
                    response.setSuccess(false);
                    response.setReason("group is not exists!");
                } else {
                    ChannelGroup channelGroup = GroupManager.getChannelGroup(msg.getGroupId());
                    if(channelGroup == null) {
                        synchronized (JoinGroupRequestHandler.class){
                            if((channelGroup = GroupManager.getChannelGroup(msg.getGroupId())) == null){
                                channelGroup = new DefaultChannelGroup(ctx.executor());
                                GroupManager.addGroup(msg.getGroupId(),channelGroup);
                            }
                        }
                    }
                    List<String> names = GroupManager.getUserNameList(channelGroup);
                    Session session = ctx.channel().attr(ServerAttrs.SESSION).get();

                    MessageResponse message = new MessageResponse();
                    message.setSuccess(true);
                    message.setUsername("system");
                    message.setContent("["+session.getUsername()+"] join Group. Welcome!");
                    channelGroup.writeAndFlush(message);

                    channelGroup.add(ctx.channel());
                    GroupManager.addUserName(channelGroup,session.getUsername());
                    ctx.channel().attr(ServerAttrs.GROUP).set(group);

                    response.setSuccess(true);
                    response.setOnlineUsers(names);
                    response.setGroupName(group.getGroup_name());
                    response.setGroupId(group.getGroup_id());
                }
            } else {
                response.setSuccess(false);
                response.setReason("You already join a group.Quit first!");
            }
            ctx.writeAndFlush(response);
        });
    }
}
