package nnero.netty.im.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Author: NNERO
 * Time : 上午11:21 19-2-22
 */
@Slf4j
public class IdleHandler extends IdleStateHandler {

    private static final int READ_TIMEOUT = 30;

    public IdleHandler() {
        super(READ_TIMEOUT, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        log.info(READ_TIMEOUT+"s haven't read data. close connection!");
        ctx.channel().close();
    }
}
