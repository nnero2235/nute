package nnero.netty.im.protocol.packet;

import lombok.Data;

/**
 * Author: NNERO
 * Time : 上午11:07 19-2-25
 */
@Data
public class QuitGroupRequest implements Packet {


    @Override
    public Byte getCommand() {
        return Command.QUIT_GROUP_REQUEST.getCommand();
    }
}
