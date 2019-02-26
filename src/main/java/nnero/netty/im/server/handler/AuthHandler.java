package nnero.netty.im.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.server.entity.Session;
import nnero.netty.im.server.ServerAttrs;

/**
 * Author: NNERO
 * Time : 下午2:26 19-2-21
 */
@Slf4j
@Single
@ChannelHandler.Sharable
public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("Auth protocol...");
        Session session = ctx.channel().attr(ServerAttrs.SESSION).get();
        if(session == null){
            ctx.channel().close();
        } else {
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }
}
