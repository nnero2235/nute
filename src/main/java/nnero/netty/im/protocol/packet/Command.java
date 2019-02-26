package nnero.netty.im.protocol.packet;

/**
 * Author: NNERO
 * Time : 下午4:58 19-2-19
 */
public enum Command {
    LOGIN_REQUEST(1,LoginRequest.class),
    LOGIN_RESPONSE(2,LoginResponse.class),
    MESSAGE_REQUEST(3,MessageRequest.class),
    MESSAGE_RESPONSE(4,MessageResponse.class),
    LOGOUT_REQUEST(5,LogoutRequest.class),
    LOGOUT_RESPONSE(6,LogoutResponse.class),
    CREATE_GROUP_REQUEST(7,CreateGroupRequest.class),
    CREATE_GROUP_RESPONSE(8,CreateGroupResponse.class),
    JOIN_GROUP_REQUEST(9,JoinGroupRequest.class),
    JOIN_GROUP_RESPONSE(10,JoinGroupResponse.class),
    HEART_BEAT_REQUEST(11,HeartBeatRequest.class),
    HEART_BEAT_RESPONSE(12,HeartBeatResponse.class),
    QUIT_GROUP_REQUEST(13,QuitGroupRequest.class),
    QUIT_GROUP_RESPONSE(14,QuitGroupResponse.class);

    private byte command;

    private Class<? extends Packet> clazz;

    Command(int b,Class<? extends Packet> c){
        command = (byte)b;
        clazz = c;
    }

    public byte getCommand(){
        return command;
    }

    public Class<? extends Packet> getClazz(){
        return clazz;
    }
}
