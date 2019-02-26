package nnero.netty.im.client.console;

import nnero.netty.im.protocol.packet.Packet;

/**
 * Author: NNERO
 * Time : 下午2:58 19-2-25
 */
public interface IConsoleCommand {

    Packet doBusiness(ConsoleInput input);
}
