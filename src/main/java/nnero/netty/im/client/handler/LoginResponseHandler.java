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
import nnero.netty.im.server.ServerAttrs;
import nnero.netty.im.protocol.packet.HeartBeatRequest;
import nnero.netty.im.protocol.packet.LoginResponse;

import java.util.concurrent.TimeUnit;

/**
 * Author: NNERO
 * Time : 下午6:02 19-2-20
 */
@Slf4j
@Single
@ChannelHandler.Sharable
@ClientIM(Command.LOGIN_RESPONSE)
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponse> {

    private static final int HEART_BEAT_DURATION = 15;

    public LoginResponseHandler(){
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponse msg) throws Exception {
        log.debug("login client handle...");
        if(msg.isSuccess()){
            log.info("Login success!");
            ctx.channel().attr(ClientAttrs.LOGIN).set(msg.getUid());
            sendHeartBeatMsg(ctx,msg.getUid());
        } else {
            log.info("Login fail.Reason is:"+msg.getMsg());
        }
    }

    private void sendHeartBeatMsg(ChannelHandlerContext ctx,int uid){
        ctx.executor().schedule(()->{
            if(ctx.channel().isActive()) {
                HeartBeatRequest request = new HeartBeatRequest();
                request.setUid(uid);
                ctx.writeAndFlush(request);
                sendHeartBeatMsg(ctx,uid);
            }
        },HEART_BEAT_DURATION, TimeUnit.SECONDS);
    }
}
