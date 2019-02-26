package nnero.netty.im.server.handler.im;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.protocol.packet.Command;
import nnero.netty.im.server.ServerIM;
import nnero.netty.im.server.db.DataBase;
import nnero.netty.im.server.entity.Group;
import nnero.netty.im.protocol.packet.CreateGroupRequest;
import nnero.netty.im.protocol.packet.CreateGroupResponse;
import nnero.netty.im.thread.ThreadPool;

import java.util.Date;

/**
 * Author: NNERO
 * Time : 下午5:00 19-2-21
 */
@Slf4j
@ChannelHandler.Sharable
@Single
@ServerIM(Command.CREATE_GROUP_REQUEST)
public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequest> {

    @Inject
    private DataBase dataBase;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequest msg) throws Exception {
        ThreadPool.exec(()->{
            CreateGroupResponse response = new CreateGroupResponse();
            if(Strings.isNullOrEmpty(msg.getGroupName()) || msg.getMaxPersons() < 2){
                log.info("group create fail...");
                response.setSuccess(false);
                response.setReason("Group Name must not be null. MaxPersons must be >= 2");
            } else {
                Group group = new Group();
                group.setGroup_name(msg.getGroupName());
                group.setMax_persons(msg.getMaxPersons());
                group.setCreate_date(new Date());
                dataBase.save(group);
                log.info("group create success!");
                response.setSuccess(true);
                response.setReason("OK");
            }
            ctx.writeAndFlush(response);
        });
    }
}
