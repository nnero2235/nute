package nnero.netty.im.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.protocol.MessageCodeC;
import nnero.netty.im.protocol.Spliter;
import nnero.netty.im.server.handler.*;

/**
 * Author: NNERO
 * Time : 下午7:21 19-2-19
 */
@Slf4j
public class Server {

    public void run(int port){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(10);

        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class) //使用nio 而非bio
                    .childHandler(new ServerChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.TCP_NODELAY,true);

            ChannelFuture f = server.bind(port).sync();
            f.addListener(future -> {
                if(future.isSuccess()){
                    log.info("bind success");
                } else {
                    log.info("bind fail");
                }
            });
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("",e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new Server().run(8000);
    }
}
