package nnero.netty.im.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import nnero.netty.im.protocol.MessageCodeC;
import nnero.netty.im.protocol.Spliter;
import nnero.netty.im.server.guice.ServerHandlerInjector;

/**
 * Author: NNERO
 * Time : 下午2:41 19-2-22
 */
public class ServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline().addLast(new IdleHandler());
        ch.pipeline().addLast(new Spliter());
        ch.pipeline().addLast(ServerHandlerInjector.getInstance(MessageCodeC.class));
        ch.pipeline().addLast(ServerHandlerInjector.getInstance(LoginRequestHandler.class));
        ch.pipeline().addLast(ServerHandlerInjector.getInstance(AuthHandler.class));
        ch.pipeline().addLast(ServerHandlerInjector.getInstance(ServerIMHandler.class));
    }
}
