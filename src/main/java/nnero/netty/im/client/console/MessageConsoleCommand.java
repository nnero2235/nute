package nnero.netty.im.client.console;

import nnero.netty.im.Single;
import nnero.netty.im.client.ClientAttrs;
import nnero.netty.im.protocol.packet.MessageRequest;
import nnero.netty.im.protocol.packet.Packet;

/**
 * Author: NNERO
 * Time : 下午3:17 19-2-25
 */
@Single
public class MessageConsoleCommand implements IConsoleCommand {

    @Override
    public Packet doBusiness(ConsoleInput input) {
        String msg = input.getInput();
        MessageRequest request = new MessageRequest();
        request.setMessageId(String.valueOf(System.currentTimeMillis()));
        request.setContent(msg);
        Integer id = input.getChannelAttr(ClientAttrs.GROUP);
        request.setGroupId(id == null ? 0 : id);
        return request;
    }
}
