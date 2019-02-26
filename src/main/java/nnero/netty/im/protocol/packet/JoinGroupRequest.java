package nnero.netty.im.protocol.packet;

import lombok.Data;

/**
 * Author: NNERO
 * Time : 下午5:13 19-2-21
 */
@Data
public class JoinGroupRequest implements Packet {

    private int groupId;

    @Override
    public Byte getCommand() {
        return Command.JOIN_GROUP_REQUEST.getCommand();
    }
}
