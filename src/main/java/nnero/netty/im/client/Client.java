package nnero.netty.im.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.client.handler.ClientChannelInitializer;
import nnero.netty.im.client.handler.ClientIMHandler;
import nnero.netty.im.client.handler.ClientInputHandler;
import nnero.netty.im.protocol.MessageCodeC;
import nnero.netty.im.protocol.Spliter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Author: NNERO
 * Time : 下午7:39 19-2-19
 */
@Slf4j
public class Client {

    public Client(){
    }

    private void run(int port){
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(10);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer())
                    .option(ChannelOption.TCP_NODELAY,true)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,10);

            connect(bootstrap,port,0);
        } catch (InterruptedException e) {
            log.error("",e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private void connect(Bootstrap bootstrap,int port,int retry) throws InterruptedException {
        ChannelFuture f = bootstrap.connect("localhost",port).sync();
        f.addListener(future -> {
            if(future.isSuccess()){
                log.info("connect success:"+retry);
            } else {
                log.info("connect fail:"+retry);
//                bootstrap.config().group().schedule(()->{
//                    connect(bootstrap,port,retry+1);
//                },1, TimeUnit.SECONDS);
            }
        });
        f.channel().closeFuture().sync();
        log.info("final");
    }

    public static void main(String[] args) {
        new Client().run(8000);
    }
}
