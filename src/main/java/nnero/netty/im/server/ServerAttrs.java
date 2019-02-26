package nnero.netty.im.server;

import io.netty.util.AttributeKey;
import nnero.netty.im.server.entity.Group;
import nnero.netty.im.server.entity.Session;

/**
 * Author: NNERO
 * Time : 下午3:14 19-2-21
 */
public interface ServerAttrs {

    AttributeKey<Session> SESSION = AttributeKey.valueOf("SESSION");

    AttributeKey<Group> GROUP = AttributeKey.valueOf("GROUP");
}
