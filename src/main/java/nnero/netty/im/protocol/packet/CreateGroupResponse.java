package nnero.netty.im.protocol.packet;

import lombok.Data;

/**
 * Author: NNERO
 * Time : 下午5:02 19-2-21
 */
@Data
public class CreateGroupResponse implements Packet {

    private boolean isSuccess;

    private String reason;

    @Override
    public Byte getCommand() {
        return Command.CREATE_GROUP_RESPONSE.getCommand();
    }
}
