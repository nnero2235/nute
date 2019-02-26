package nnero.netty.im.protocol.packet;

/**
 * Author: NNERO
 * Time : 下午4:56 19-2-19
 */
public interface Packet {

    byte VERSION = 1;

    Byte getCommand();
}
