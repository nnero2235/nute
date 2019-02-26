package nnero.netty.im.server;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: NNERO
 * Time : 下午5:22 19-2-21
 */
public class GroupManager {

    private static final Map<Integer, ChannelGroup> GROUP_MAP = new ConcurrentHashMap<>();

    private static final Map<ChannelGroup, List<String>> USER_NAME_MAP = new ConcurrentHashMap<>();

    public static void addGroup(int groupId,ChannelGroup channels){
        GROUP_MAP.putIfAbsent(groupId,channels);
    }

    public static ChannelGroup getChannelGroup(int groupId){
        return GROUP_MAP.get(groupId);
    }

    public synchronized static void addUserName(ChannelGroup group,String name){
        List<String> list = USER_NAME_MAP.get(group);
        if(list == null){
            list = new ArrayList<>();
            USER_NAME_MAP.putIfAbsent(group,list);
        }
        list.add(name);
    }

    public synchronized static void removeUserName(ChannelGroup group,String name){
        List<String> list = USER_NAME_MAP.get(group);
        if(list == null){
            return;
        }
        list.remove(name);
    }

    public synchronized static void removeChannel(int groupId,String name, Channel channel){
        ChannelGroup channelGroup = GROUP_MAP.get(groupId);
        if(channelGroup != null){
            channelGroup.remove(channel);
            List<String> list = USER_NAME_MAP.get(channelGroup);
            if(list != null){
                list.remove(name);
            }
        }
    }

    public static List<String> getUserNameList(ChannelGroup group){
        return USER_NAME_MAP.get(group);
    }
}
