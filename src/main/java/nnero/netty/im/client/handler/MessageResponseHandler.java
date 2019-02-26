package nnero.netty.im.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.client.Client;
import nnero.netty.im.client.ClientIM;
import nnero.netty.im.protocol.packet.Command;
import nnero.netty.im.protocol.packet.MessageResponse;

/**
 * Author: NNERO
 * Time : 下午6:02 19-2-20
 */
@Slf4j
@Single
@ChannelHandler.Sharable
@ClientIM(Command.MESSAGE_RESPONSE)
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponse> {


    public MessageResponseHandler(){
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponse msg) throws Exception {
        log.debug("message client handle...");
        if(msg.isSuccess()){
            log.info("["+msg.getUsername()+"]:"+msg.getContent());
        } else {
            log.error("send fail Reason:"+msg.getReason());
        }
    }
}
