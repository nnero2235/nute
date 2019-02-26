package nnero.netty.im.protocol.serialize;

/**
 * Author: NNERO
 * Time : 下午5:02 19-2-19
 */
public interface Serializer {

    Byte getSerializeAlgorithm();

    <T> byte[] serialize(T o);

    <T> T deserialize(byte[] bytes,Class<T> clazz);
}
