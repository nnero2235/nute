package nnero.netty.im.client;

import io.netty.util.AttributeKey;
import nnero.netty.im.server.entity.Group;

/**
 * Author: NNERO
 * Time : 下午2:27 19-2-22
 */
public interface ClientAttrs {

    AttributeKey<Integer> LOGIN = AttributeKey.valueOf("LOGIN");

    AttributeKey<Integer> GROUP = AttributeKey.valueOf("GROUP");
}
