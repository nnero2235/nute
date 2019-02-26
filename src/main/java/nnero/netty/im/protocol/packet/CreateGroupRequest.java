package nnero.netty.im.protocol.packet;

import lombok.Data;

/**
 * Author: NNERO
 * Time : 下午4:56 19-2-21
 */
@Data
public class CreateGroupRequest implements Packet {

    private String groupName;

    private int maxPersons;

    @Override
    public Byte getCommand() {
        return Command.CREATE_GROUP_REQUEST.getCommand();
    }
}
