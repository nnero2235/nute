package nnero.netty.im.client.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import nnero.netty.im.client.guice.ClientHandlerInjector;
import nnero.netty.im.protocol.MessageCodeC;
import nnero.netty.im.protocol.Spliter;

/**
 * Author: NNERO
 * Time : 上午10:56 19-2-25
 */
public class ClientChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline().addLast(new Spliter());
        ch.pipeline().addLast(ClientHandlerInjector.getInstance(MessageCodeC.class));
        ch.pipeline().addLast(ClientHandlerInjector.getInstance(ClientInputHandler.class));
        ch.pipeline().addLast(ClientHandlerInjector.getInstance(ClientIMHandler.class));
    }
}
