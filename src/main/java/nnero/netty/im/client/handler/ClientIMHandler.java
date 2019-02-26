package nnero.netty.im.client.handler;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.client.ClientIM;
import nnero.netty.im.client.console.ConsoleInput;
import nnero.netty.im.client.guice.ClientHandlerInjector;
import nnero.netty.im.protocol.packet.Packet;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: NNERO
 * Time : 上午11:15 19-2-22
 */
@Slf4j
@Single
@ChannelHandler.Sharable
public class ClientIMHandler extends SimpleChannelInboundHandler<Packet> {

    private static final Map<Byte,SimpleChannelInboundHandler<? extends Packet>> HANDLER_MAP = new HashMap<>();

    @Inject
    private ConsoleInput input;

    static {
        Reflections reflections = new Reflections("nnero.netty.im.client");
        Set<Class<? extends SimpleChannelInboundHandler>> handlers =
                reflections.getSubTypesOf(SimpleChannelInboundHandler.class);
        if(handlers != null && handlers.size() > 0){
            for(Class<? extends SimpleChannelInboundHandler> clazz : handlers){
                ClientIM im = clazz.getAnnotation(ClientIM.class);
                if(im != null) {
                    HANDLER_MAP.put(im.value().getCommand(), ClientHandlerInjector.getInstance(clazz));
                }
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        SimpleChannelInboundHandler<? extends Packet> handler = HANDLER_MAP.get(msg.getCommand());
        handler.channelRead(ctx,msg);
        log.debug("unlock ...: "+input.toString());
        input.unlockWaiting();
    }
}
