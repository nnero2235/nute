package nnero.netty.im.protocol.packet;

import lombok.Data;

/**
 * Author: NNERO
 * Time : 下午4:57 19-2-19
 */
@Data
public class LoginRequest implements Packet {

    private int userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST.getCommand();
    }
}
