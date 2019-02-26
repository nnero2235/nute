package nnero.netty.im.server;

import io.netty.channel.Channel;
import nnero.netty.im.server.entity.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: NNERO
 * Time : 下午3:50 19-2-21
 */
public class SessionManager {

    private static final Map<Integer, Channel> SESSION_CHANNEL_MAP = new ConcurrentHashMap<>();


    public static void bind(Integer uid,Channel channel){
        SESSION_CHANNEL_MAP.putIfAbsent(uid,channel);
    }

    public static void unBind(Integer uid){
        SESSION_CHANNEL_MAP.remove(uid);
    }

    public static Channel getChannel(Integer uid){
        return SESSION_CHANNEL_MAP.get(uid);
    }

    public static boolean hasLogin(Channel ch){
        Session session = ch.attr(ServerAttrs.SESSION).get();
        return session != null;
    }

    public static String getUserName(Channel ch){
        Session session = ch.attr(ServerAttrs.SESSION).get();
        return session != null ? session.getUsername() : "null";
    }
}
