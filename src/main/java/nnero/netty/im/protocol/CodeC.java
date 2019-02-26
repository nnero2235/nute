package nnero.netty.im.protocol;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.protocol.packet.*;
import nnero.netty.im.protocol.serialize.JSONSerializer;
import nnero.netty.im.protocol.serialize.SerializeAlgorithm;
import nnero.netty.im.protocol.serialize.Serializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: NNERO
 * Time : 下午5:02 19-2-19
 */
@Slf4j
public class CodeC {

    public static final int MAGIC = 0x11aa22bb;

    private static final Map<Byte, Serializer> SERIALIZER_MAP = new HashMap<>();

    private static final Map<Byte,Class<? extends Packet>> COMMAND_MAP = new HashMap<>();

    static {
        SERIALIZER_MAP.put(SerializeAlgorithm.JSON.getAlgorithm(),new JSONSerializer());

        Command[] all = Command.values();
        if(all.length > 0){
           for(Command c : all){
               COMMAND_MAP.put(c.getCommand(),c.getClazz());
           }
        }
    }

    public static ByteBuf encode(ByteBuf byteBuf, Packet packet){
        Serializer serializer = SERIALIZER_MAP.get(SerializeAlgorithm.JSON.getAlgorithm());

        Byte command = packet.getCommand();
        byte[] data = serializer.serialize(packet);;

        if(data == null){
            return null;
        }

        byteBuf.writeInt(MAGIC);
        byteBuf.writeByte(Packet.VERSION);
        byteBuf.writeByte(command);
        byteBuf.writeByte(serializer.getSerializeAlgorithm());
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);

        return byteBuf;
    }

    public static Packet decode(ByteBuf byteBuf){
        int magic = byteBuf.readInt();
        if(magic != MAGIC){
            log.debug("magic is wrong:"+magic+" abandon packet!");
            return null;
        }
        byte version = byteBuf.readByte();
        if(version != Packet.VERSION){
            log.debug("version is wrong:"+version+" abandon packet!");
            return null;
        }
        byte command = byteBuf.readByte();

        byte serializeAlgorithm = byteBuf.readByte();
        Serializer serializer = SERIALIZER_MAP.get(serializeAlgorithm);
        if(serializer == null){
            log.debug("serializer algorithm is not exists.");
            return null;
        }
        int len = byteBuf.readInt();
        byte[] data = new byte[len];
        byteBuf.readBytes(data);

        Class<? extends Packet> clazz = COMMAND_MAP.get(command);
        if(clazz != null){
            return serializer.deserialize(data,clazz);
        }
        log.debug("unknown command:"+command);
        return null;
    }
}
