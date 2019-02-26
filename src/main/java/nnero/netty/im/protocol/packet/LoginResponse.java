package nnero.netty.im.protocol.packet;

import lombok.Data;

/**
 * Author: NNERO
 * Time : 下午7:29 19-2-19
 */
@Data
public class LoginResponse implements Packet {

    private boolean isSuccess;

    private String msg;

    private int uid;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE.getCommand();
    }
}
