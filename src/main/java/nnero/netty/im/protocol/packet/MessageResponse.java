package nnero.netty.im.protocol.packet;

import lombok.Data;

/**
 * Author: NNERO
 * Time : 下午4:46 19-2-20
 */
@Data
public class MessageResponse implements Packet {

    private String messageId;

    private String content;

    private String username;

    private boolean isSuccess;

    private String reason;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_RESPONSE.getCommand();
    }
}
