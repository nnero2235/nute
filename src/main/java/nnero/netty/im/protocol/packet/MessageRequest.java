package nnero.netty.im.protocol.packet;

import lombok.Data;

/**
 * Author: NNERO
 * Time : 上午10:26 19-2-20
 */
@Data
public class MessageRequest implements Packet {

    private String messageId;

    private String content;

    private int groupId;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST.getCommand();
    }
}
