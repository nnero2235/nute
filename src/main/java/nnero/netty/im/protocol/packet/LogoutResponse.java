package nnero.netty.im.protocol.packet;

import lombok.Data;

/**
 * Author: NNERO
 * Time : 下午3:20 19-2-21
 */
@Data
public class LogoutResponse implements Packet {

    private boolean isSuccess;

    private String reason;

    @Override
    public Byte getCommand() {
        return Command.LOGOUT_RESPONSE.getCommand();
    }
}
