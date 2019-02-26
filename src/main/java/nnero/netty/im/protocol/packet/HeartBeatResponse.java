package nnero.netty.im.protocol.packet;

import lombok.Data;

/**
 * Author: NNERO
 * Time : 上午11:29 19-2-22
 */
@Data
public class HeartBeatResponse implements Packet {

    @Override
    public Byte getCommand() {
        return Command.HEART_BEAT_RESPONSE.getCommand();
    }
}
