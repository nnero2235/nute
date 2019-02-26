package nnero.netty.im.server;

import nnero.netty.im.protocol.packet.Command;

import java.lang.annotation.*;

/**
 * Author: NNERO
 * Time : 下午7:18 19-2-22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ServerIM {

    Command value();
}
