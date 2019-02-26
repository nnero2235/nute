package nnero.netty.im.protocol.packet;

import lombok.Data;

import java.util.List;

/**
 * Author: NNERO
 * Time : 下午5:19 19-2-21
 */
@Data
public class JoinGroupResponse implements Packet{

    private boolean isSuccess;

    private String reason;

    private List<String> onlineUsers;

    private String groupName;

    private int groupId;

    @Override
    public Byte getCommand() {
        return Command.JOIN_GROUP_RESPONSE.getCommand();
    }
}
