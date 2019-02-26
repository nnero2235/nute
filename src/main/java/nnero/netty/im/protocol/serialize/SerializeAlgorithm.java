package nnero.netty.im.protocol.serialize;

/**
 * Author: NNERO
 * Time : 下午5:00 19-2-19
 */
public enum SerializeAlgorithm {
    JSON(1);

    private Byte algorithm;

    SerializeAlgorithm(int type){
        algorithm = (byte)type;
    }

    public Byte getAlgorithm() {
        return algorithm;
    }
}
