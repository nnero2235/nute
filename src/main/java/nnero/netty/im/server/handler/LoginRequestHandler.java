package nnero.netty.im.server.handler;

import com.google.inject.Inject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.server.db.DataBase;
import nnero.netty.im.server.entity.Session;
import nnero.netty.im.server.entity.User;
import nnero.netty.im.server.ServerAttrs;
import nnero.netty.im.server.SessionManager;
import nnero.netty.im.protocol.packet.LoginRequest;
import nnero.netty.im.protocol.packet.LoginResponse;
import nnero.netty.im.thread.ThreadPool;

/**
 * Author: NNERO
 * Time : 下午5:54 19-2-20
 */
@Slf4j
@Single
@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequest> {

    @Inject
    private DataBase dataBase;

    private User getUser(LoginRequest request){
        User u = dataBase.getUser(request.getUsername(),request.getPassword());
        return u;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequest msg) throws Exception {
        ThreadPool.exec(()->{
            log.debug("login server handle...");
            LoginResponse response = new LoginResponse();
            Channel channel = ctx.channel();
            User u = getUser(msg);
            if(u != null){
                response.setSuccess(true);
                response.setMsg("OK");
                response.setUid(u.getUid());
                Session session = new Session();
                session.setUid(u.getUid());
                session.setUsername(u.getUsername());
                channel.attr(ServerAttrs.SESSION).set(session);
                SessionManager.bind(u.getUid(),ctx.channel());
                log.info("Welcome User:"+msg.getUsername()+" Login in");
            } else {
                response.setSuccess(false);
                response.setMsg("username or password is wrong!");
                log.info("User:"+msg.getUsername()+" valid fail!");
            }
            ctx.writeAndFlush(response);
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Session session = ctx.channel().attr(ServerAttrs.SESSION).get();
        if(session != null) {
            SessionManager.unBind(session.getUid());
        }
        super.channelInactive(ctx);
    }
}
