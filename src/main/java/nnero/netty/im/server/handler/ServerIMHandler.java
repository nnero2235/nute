package nnero.netty.im.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.protocol.packet.Command;
import nnero.netty.im.protocol.packet.Packet;
import nnero.netty.im.server.ServerIM;
import nnero.netty.im.server.guice.ServerHandlerInjector;
import nnero.netty.im.server.handler.im.*;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: NNERO
 * Time : 上午11:09 19-2-22
 */
@Slf4j
@Single
@ChannelHandler.Sharable
public class ServerIMHandler extends SimpleChannelInboundHandler<Packet> {

    private static final Map<Byte,SimpleChannelInboundHandler<? extends Packet>> HANDLER_MAP = new HashMap<>();

    static {
        Reflections reflections = new Reflections("nnero.netty.im.server");
        Set<Class<? extends SimpleChannelInboundHandler>> handlers =
                reflections.getSubTypesOf(SimpleChannelInboundHandler.class);
        if(handlers != null && handlers.size() > 0){
            for(Class<? extends SimpleChannelInboundHandler> clazz : handlers){
                ServerIM im = clazz.getAnnotation(ServerIM.class);
                if(im != null) {
                    HANDLER_MAP.put(im.value().getCommand(), ServerHandlerInjector.getInstance(clazz));
                }
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        SimpleChannelInboundHandler<? extends Packet> handler = HANDLER_MAP.get(msg.getCommand());
        handler.channelRead(ctx,msg);
    }
}
