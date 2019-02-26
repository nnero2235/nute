package nnero.netty.im.protocol.packet;

/**
 * Author: NNERO
 * Time : 下午3:10 19-2-21
 */
public class LogoutRequest implements Packet {

    @Override
    public Byte getCommand() {
        return Command.LOGOUT_REQUEST.getCommand();
    }
}
