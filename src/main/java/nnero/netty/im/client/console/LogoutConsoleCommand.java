package nnero.netty.im.client.console;

import nnero.netty.im.Single;
import nnero.netty.im.protocol.packet.LogoutRequest;
import nnero.netty.im.protocol.packet.Packet;

/**
 * Author: NNERO
 * Time : 下午3:25 19-2-25
 */
@Single
public class LogoutConsoleCommand implements IConsoleCommand {
    @Override
    public Packet doBusiness(ConsoleInput input) {
        return new LogoutRequest();
    }
}
