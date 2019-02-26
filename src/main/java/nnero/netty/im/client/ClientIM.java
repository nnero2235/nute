package nnero.netty.im.client;

import nnero.netty.im.protocol.packet.Command;

import java.lang.annotation.*;

/**
 * Author: NNERO
 * Time : 上午10:59 19-2-25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ClientIM {

    Command value();
}
