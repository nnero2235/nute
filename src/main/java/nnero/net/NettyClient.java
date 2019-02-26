package nnero.net;

import com.google.common.base.Strings;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Author: NNERO
 * Time : 下午7:16 19-2-18
 */
public class NettyClient {

    private final static Logger LOG = LoggerFactory.getLogger(NettyClient.class);

    private final static int WORKER_THREADS = 1;

    private NioEventLoopGroup workerGroup;

    private Bootstrap bootstrap;

    private String host;

    private int port;

    private Channel nettyChannel;

    public NettyClient(String host,int port){
        if(Strings.isNullOrEmpty(host) || port <= 0 || port > 65535){
            throw new RuntimeException("host or port is wrong.host:"+host+"->port:"+port);
        }
        this.host = host;
        this.port = port;
        bootstrap = new Bootstrap();
        workerGroup = new NioEventLoopGroup(WORKER_THREADS);

        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new StringEncoder(Charset.defaultCharset()));
                        ch.pipeline().addLast(new NettyClient.ClientDataReceiveHandler());
                    }
                });
        nettyChannel = bootstrap.connect(host,port).channel();
    }

    public void send(String data) throws IOException {
        nettyChannel.writeAndFlush(data);
    }


    public void close() throws IOException {
        if(nettyChannel != null){
            try {
                nettyChannel.close().sync();
                workerGroup.shutdownGracefully();
            } catch (InterruptedException e) {
                throw new IOException(e.getCause());
            }
        }
    }

    public boolean isClosed() {
        return nettyChannel != null;
    }

    private static class ClientDataReceiveHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf m = (ByteBuf) msg;
            try {
                long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
                System.out.println(new Date(currentTimeMillis));
            } finally {
                m.release();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            LOG.error("",cause);
            ctx.close();
        }
    }
}
