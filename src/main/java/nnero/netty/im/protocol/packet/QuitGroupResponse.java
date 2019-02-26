package nnero.netty.im.protocol.packet;

import lombok.Data;

/**
 * Author: NNERO
 * Time : 上午11:08 19-2-25
 */
@Data
public class QuitGroupResponse implements Packet{

    private boolean isSuccess;

    private String reason;

    @Override
    public Byte getCommand() {
        return Command.QUIT_GROUP_RESPONSE.getCommand();
    }
}
