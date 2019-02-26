package nnero.net;

import java.io.IOException;

/**
 * Author: NNERO
 * Time : 下午2:40 19-2-12
 * 抽象网络传输过程
 */
public interface Transport {

    /**
     * 发送 bytes 数组的所有数据
     * @param bytes
     */
    void send(byte[] bytes) throws IOException;

    /**
     * 按offset 和len 进行发送
     * @param bytes
     * @param offset
     * @param len
     */
    void send(byte[] bytes,int offset,int len)throws IOException;

    /**
     * 发送string:按utf8 解码发送
     * @param data
     */
    void send(String data)throws IOException;

    /**
     * 收取数据，若没有数据过来 会一直阻塞
     * @return read counts
     */
    int receive(byte[] buf,int offset,int len)throws IOException;

    /**
     * 关闭传输
     */
    void close() throws IOException;

    /**
     * 传输是否关闭
     */
    boolean isClosed();
}
