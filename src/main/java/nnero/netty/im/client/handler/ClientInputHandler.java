package nnero.netty.im.client.handler;

import com.google.inject.Inject;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.client.console.ConsoleInput;

/**
 * Author: NNERO
 * Time : 下午2:44 19-2-21
 */
@Slf4j
@Single
@ChannelHandler.Sharable
public class ClientInputHandler extends ChannelDuplexHandler {

    @Inject
    private ConsoleInput input;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Input Handler...: "+input.toString());
        input.bindChannel(ctx.channel());
        input.startInputThread();
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("connection close.");
        input.unbindChannel();
        super.channelInactive(ctx);
    }

}
