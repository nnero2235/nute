package nnero.netty.im.protocol.serialize;

import com.alibaba.fastjson.JSON;

/**
 * Author: NNERO
 * Time : 下午5:04 19-2-19
 */
public class JSONSerializer implements Serializer {


    @Override
    public Byte getSerializeAlgorithm() {
        return SerializeAlgorithm.JSON.getAlgorithm();
    }

    @Override
    public <T> byte[] serialize(T o) {
        if(o == null){
            return null;
        }
        return JSON.toJSONBytes(o);
    }

    @Override
    public <T> T deserialize(byte[] bytes,Class<T> clazz) {
        if(bytes == null){
            return null;
        }
        return JSON.parseObject(bytes,clazz);
    }
}
