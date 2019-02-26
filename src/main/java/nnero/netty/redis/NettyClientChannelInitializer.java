package nnero.netty.redis;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Author: NNERO
 * Time : 上午11:16 19-2-26
 */
public class NettyClientChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline().addLast(new RedisSpliter());
    }
}
