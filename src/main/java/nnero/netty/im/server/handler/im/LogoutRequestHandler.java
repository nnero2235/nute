package nnero.netty.im.server.handler.im;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.protocol.packet.Command;
import nnero.netty.im.server.ServerIM;
import nnero.netty.im.server.entity.Session;
import nnero.netty.im.server.ServerAttrs;
import nnero.netty.im.protocol.packet.LogoutRequest;
import nnero.netty.im.protocol.packet.LogoutResponse;
import nnero.netty.im.thread.ThreadPool;

/**
 * Author: NNERO
 * Time : 下午3:13 19-2-21
 */
@Slf4j
@Single
@ChannelHandler.Sharable
@ServerIM(Command.LOGOUT_REQUEST)
public class LogoutRequestHandler extends SimpleChannelInboundHandler<LogoutRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequest msg) throws Exception {
        ThreadPool.exec(()->{
            Session session = ctx.channel().attr(ServerAttrs.SESSION).get();
            LogoutResponse response = new LogoutResponse();
            if(session == null){
                log.info("user is not login.skip");
                response.setSuccess(false);
                response.setReason("You are not login!");
            } else {
                log.info("handle logout.Set session null");
                ctx.channel().attr(ServerAttrs.SESSION).set(null);
                response.setSuccess(true);
                response.setReason("OK");
            }
            ctx.writeAndFlush(response);
        });
    }
}
