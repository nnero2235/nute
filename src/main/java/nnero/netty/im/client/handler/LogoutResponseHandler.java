package nnero.netty.im.client.handler;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.client.ClientIM;
import nnero.netty.im.client.console.ConsoleInput;
import nnero.netty.im.protocol.packet.Command;
import nnero.netty.im.protocol.packet.LogoutResponse;

import java.util.concurrent.Future;

/**
 * Author: NNERO
 * Time : 下午3:29 19-2-21
 */
@Slf4j
@Single
@ChannelHandler.Sharable
@ClientIM(Command.LOGOUT_RESPONSE)
public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponse> {

    @Inject
    private ConsoleInput input;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponse msg) throws Exception {
        if(msg.isSuccess()){
            log.info("logout success. connection close...");
            ctx.channel().close();
            input.shutdown();
        } else {
            log.info("logout fail.Reason:"+msg.getReason());
        }
    }
}
